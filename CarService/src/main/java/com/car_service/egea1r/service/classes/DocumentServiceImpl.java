package com.car_service.egea1r.service.classes;

import com.car_service.egea1r.CarServiceApplication;
import com.car_service.egea1r.persistance.entity.Document;
import com.car_service.egea1r.persistance.repository.interfaces.*;
import com.car_service.egea1r.service.interfaces.DocumentService;
import com.car_service.egea1r.web.data.DTO.DocumentDTO;
import com.car_service.egea1r.web.data.DTO.FinanceDTO;
import com.car_service.egea1r.web.data.DTO.ServiceDataDTO;
import com.car_service.egea1r.web.data.mapper.MapStructObjectMapper;
import com.car_service.egea1r.web.data.payload.request.DocumentRequest;
import com.car_service.egea1r.web.data.payload.response.FileAndCarResponse;
import com.car_service.egea1r.web.data.payload.response.ResponseFile;
import org.hibernate.engine.jdbc.StreamUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletResponse;
import javax.transaction.Transactional;
import java.io.*;
import java.math.RoundingMode;
import java.net.MalformedURLException;
import java.net.URLDecoder;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

@Service
public class DocumentServiceImpl implements DocumentService {

    private static final DecimalFormat df2 = new DecimalFormat("#.##");
    private static final String DATE_FORMAT = "yyyy.MM.dd";

    private final DocumentRepository documentRepository;
    private final FileSystemRepository fileSystemRepository;
    private final ServiceReservationRepository serviceReservationRepository;
    private final FinanceRepository financeRepository;
    private final ServiceDataRepository serviceDataRepository;
    private final MapStructObjectMapper mapStructObjectMapper;
    private static String decodedPath;
    private static final String DECODER = "UTF-8";


    static {
        try {
            decodedPath = (URLDecoder.decode(
                    CarServiceApplication.class.getProtectionDomain().getCodeSource().getLocation().getPath(), DECODER)
                    + "files/service/").substring(1);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    @PostConstruct
    public void init() throws IOException {
        Files.createDirectories(Paths.get(decodedPath));
        Files.createDirectories(Paths.get((URLDecoder.decode(CarServiceApplication.class.getProtectionDomain().getCodeSource().getLocation().getPath(), DECODER) + "files/client/").substring(1)));
    }

    @Autowired
    public DocumentServiceImpl(DocumentRepository documentRepository, FileSystemRepository fileSystemRepository,
                               ServiceReservationRepository serviceReservationRepository, FinanceRepository financeRepository,
                               ServiceDataRepository serviceDataRepository, MapStructObjectMapper mapStructObjectMapper) {
        this.documentRepository = documentRepository;
        this.fileSystemRepository = fileSystemRepository;
        this.serviceReservationRepository = serviceReservationRepository;
        this.financeRepository = financeRepository;
        this.serviceDataRepository = serviceDataRepository;
        this.mapStructObjectMapper = mapStructObjectMapper;
    }

    @Transactional
    @Override
    public List<String> uploadService(MultipartFile[] multipartFiles) throws IOException {
        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT);
        List<String> fileNames = new ArrayList<>();
        Path resourceDirectory = Paths.get(decodedPath + "/" + sdf.format(date) + "/");
        Files.createDirectories(resourceDirectory);
        Arrays.stream(multipartFiles).forEach(file -> {
            Path newFile = Paths.get(decodedPath + sdf.format(date) +  "/" + file.getName());
            DocumentRequest storedFile = DocumentRequest.builder()
                    .name(file.getOriginalFilename())
                    .fileType(file.getContentType())
                    .size(fileSize(file.getSize()))
                    .build();
            documentRepository.saveServiceDocument(storedFile, newFile.toString());
            fileNames.add(file.getOriginalFilename());
        });
        return fileNames;
    }

    @Override
    public void zippingFiles(List<Long> id, HttpServletResponse response) throws IOException {
        try(ZipOutputStream zipOutputStream = new ZipOutputStream(response.getOutputStream())){
            for (Long fileId : id) {
                Document document = findDocument(fileId);
                FileSystemResource resource = findInTheSystem(document.getLocation());
                zipOutputStream.putNextEntry(new ZipEntry(resource.getFilename()));
                StreamUtils.copy(resource.getInputStream(), zipOutputStream);
                zipOutputStream.closeEntry();
            }
        }catch (RuntimeException e){
            throw new RuntimeException("Could not read the file!");
        }
    }

    @Override
    public Resource download(long id) {
        FileSystemResource fileSystemResource = findInTheSystem(findDocument(id).getLocation());
        try {
            Path file = Paths.get(fileSystemResource.getPath());
            Resource resource = new UrlResource(file.toUri());
            if (resource.exists() || resource.isReadable()) {
                return resource;
            } else {
                throw new RuntimeException("Could not read the file!");
            }
        } catch (MalformedURLException e) {
            throw new RuntimeException("Error: " + e.getMessage());
        }
    }

    @Transactional
    @Override
    public List<String> upload(MultipartFile[] multipartFiles, ServiceDataDTO serviceDataDTO, FinanceDTO financeDTO, String email) throws IOException {
        long financeId = financeRepository.saveFinanceByServiceData(financeDTO);
        long serviceDataId = serviceDataRepository.saveServiceData(serviceDataDTO, financeId);
        serviceReservationRepository.setServiceDataFk(serviceDataId, serviceDataDTO.getFkCarId());
        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT);
        Path resourceDirectory = Paths.get((URLDecoder.decode(CarServiceApplication.class.getProtectionDomain().getCodeSource().getLocation().getPath(), DECODER) + "files/client/" + email + "/" + sdf.format(date)).substring(1));
        List<String> fileNames = new ArrayList<>();
        Files.createDirectories(resourceDirectory);

        Arrays.stream(multipartFiles).forEach(file -> {
            Path newFile = Paths.get(resourceDirectory + "/" + file.getName());
            DocumentRequest storedFile = DocumentRequest.builder()
                    .name(file.getOriginalFilename())
                    .fileType(file.getContentType())
                    .size(fileSize(file.getSize()))
                    .fkDocumentCarId(serviceDataDTO.getFkCarId())
                    .fkDocumentServiceData(serviceDataId)
                    .build();
            documentRepository.saveClientDocument(storedFile, newFile.toString());
            fileNames.add(file.getOriginalFilename());
        });
        return fileNames;
    }


    private String fileSize(long size) {
        String sizeToSave = "";
        df2.setRoundingMode(RoundingMode.UP);
        if (size / (Math.pow(1024, 3)) > 1) {
            sizeToSave = (Double.parseDouble(df2.format(size / (Math.pow(1024, 3)))) + " GB");
        } else if (size / (Math.pow(1024, 2)) > 1 && size / (Math.pow(1024, 3)) < 1) {
            sizeToSave = (Double.parseDouble(df2.format(size / (Math.pow(1024, 2)))) + " MB");
        } else if (size / 1024 > 1 && size / (Math.pow(1024, 2)) < 1 || size / 1024 < 1) {
            sizeToSave = (Double.parseDouble(df2.format(size / 1024)) + " KB");
        }
        return sizeToSave;
    }

    @Override
    public Document findDocument(long documentId) {
       return  documentRepository.findById(documentId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }

    private FileSystemResource findInTheSystem(String location) {
        return fileSystemRepository.findInFileSystem(location);
    }

    @Override
    public Set<FileAndCarResponse> getAllFilesByUser(long credentialId) {
        List<DocumentDTO> documents = documentRepository.findAllByUser(credentialId);
        return fileAndCarResponseSetter(documents);
    }

    @Override
    public Set<FileAndCarResponse> getAllFilesByCar(long carId, long credentialId) {
        List<DocumentDTO> documents = documentRepository.findAllByCar(carId, credentialId);
        return fileAndCarResponseSetter(documents);
    }

    private Set<FileAndCarResponse> fileAndCarResponseSetter(List<DocumentDTO> documents) {
        Set<FileAndCarResponse> response = new HashSet<>();
        for (DocumentDTO documentDTO : documents) {
            FileAndCarResponse fileAndCarResponse = mapStructObjectMapper.documentDTOtoFileAndCarResponse(documentDTO);
            response.add(fileAndCarResponse);
        }
        for (FileAndCarResponse resp : response) {
            List<ResponseFile> responseFiles = new ArrayList<>();
            List<Long> documentIds = new ArrayList<>();
            for (DocumentDTO documentDTO : documents) {
                if (documentDTO.getServiceDataId().equals(resp.getServiceDataId())) {
                    ResponseFile responseFile = mapStructObjectMapper.documentDTOtoResponseFile(documentDTO);
                    documentIds.add(documentDTO.getFileId());
                    responseFiles.add(responseFile);
                }
            }
            resp.setDocumentList(responseFiles);
            resp.setDocumentIds(documentIds);
        }
        return response;
    }

}

