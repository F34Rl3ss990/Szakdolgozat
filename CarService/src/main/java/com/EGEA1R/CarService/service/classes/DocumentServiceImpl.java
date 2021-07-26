package com.EGEA1R.CarService.service.classes;

import com.EGEA1R.CarService.persistance.entity.CarMileage;
import com.EGEA1R.CarService.persistance.entity.Document;
import com.EGEA1R.CarService.persistance.repository.classes.FileSystemRepositoryImpl;
import com.EGEA1R.CarService.persistance.repository.interfaces.*;
import com.EGEA1R.CarService.service.interfaces.DocumentService;
import com.EGEA1R.CarService.web.DTO.DocumentDTO;
import com.EGEA1R.CarService.web.DTO.FinanceDTO;
import com.EGEA1R.CarService.web.DTO.ServiceDataDTO;
import com.EGEA1R.CarService.web.DTO.payload.request.DocumentRequest;
import com.EGEA1R.CarService.web.DTO.payload.response.FileAndCarResponse;
import com.EGEA1R.CarService.web.DTO.payload.response.ResponseFile;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.compress.utils.IOUtils;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileItemIterator;
import org.apache.commons.fileupload.FileItemStream;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.transaction.Transactional;
import java.io.*;
import java.math.RoundingMode;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

@Service
public class DocumentServiceImpl implements DocumentService {

    private static DecimalFormat df2 = new DecimalFormat("#.##");

    private DocumentRepository documentRepository;

    private FileSystemRepository fileSystemRepository;

    private ServiceReservationRepository serviceReservationRepository;

    private FinanceRepository financeRepository;

    private ServiceDataRepository serviceDataRepository;

    @Autowired
    public void setServiceReservationRepository(ServiceReservationRepository serviceReservationRepository) {
        this.serviceReservationRepository = serviceReservationRepository;
    }

    @Autowired
    public void setFinanceRepository(FinanceRepository financeRepository) {
        this.financeRepository = financeRepository;
    }

    @Autowired
    public void setServiceDataRepository(ServiceDataRepository serviceDataRepository) {
        this.serviceDataRepository = serviceDataRepository;
    }

    @Autowired
    public void setDocumentRepository(DocumentRepository documentRepository) {
        this.documentRepository = documentRepository;
    }

    @Autowired
    public void setFileSystemRepository(FileSystemRepository fileSystemRepository) {
        this.fileSystemRepository = fileSystemRepository;
    }

    @Override
    @Transactional
    public String storeServiceBigFiles(HttpServletRequest request) {
        File f = new File(System.getProperty("java.class.path"));
        File dir = f.getAbsoluteFile().getParentFile();
        String path = dir.toString();
        String RESOURCES_DIR = DocumentServiceImpl.class.getResource("/files/service/").getPath().substring(1);
       // String RESOURCES_DIR = path + "/files/service/";
        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy.MM.dd");

        try {
            DiskFileItemFactory factory = new DiskFileItemFactory();
            ServletFileUpload upload = new ServletFileUpload(factory);
            FileItemIterator iter = upload.getItemIterator(request);
            while (iter.hasNext()) {
                FileItemStream item = iter.next();
                InputStream stream = item.openStream();
                String filename = item.getName();
                String contentType = item.getContentType();
                Path newFile = Paths.get(RESOURCES_DIR + sdf.format(date) + "-" + filename);
                if (!item.isFormField()) {
                    OutputStream out = new FileOutputStream(String.valueOf(newFile));
                    IOUtils.copy(stream, out);
                    stream.close();
                    out.close();
                    File file = new File(RESOURCES_DIR + sdf.format(date) + "-" + filename);
                    DocumentRequest storedFile = DocumentRequest.builder()
                            .name(filename)
                            .fileType(contentType)
                            .size(fileSize(file.length()))
                            .build();
                    documentRepository.saveServiceDocument(storedFile, newFile.toString());
                }
            }
        } catch (FileUploadException e) {
            return ("File upload error" + e.toString());
        } catch (IOException e) {
            return ("Internal server IO error" + e.toString());
        }
        return "Successfully uploaded";
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
    @Transactional
    public String storeClientBigFiles(HttpServletRequest request) {
        String RESOURCES_DIR;
        new File(DocumentServiceImpl.class.getResource("/files").getPath()).mkdir();
        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy.MM.dd");
        ServiceDataDTO serviceDataDTO = new ServiceDataDTO();
        FinanceDTO financeDTO = new FinanceDTO();
        String email = "";
        Long financeId;
        Long serviceDataId;
        String decoder = "UTF-8";
        try {
            DiskFileItemFactory factory = new DiskFileItemFactory();
            ServletFileUpload upload = new ServletFileUpload(factory);
            List<FileItem> items = upload.parseRequest(request);
            for (FileItem item : items) {
                ObjectMapper objectMapper = new ObjectMapper();
                if (item.isFormField()) {
                    if (item.getFieldName().equals("ServiceDataDTO")) {
                        serviceDataDTO = objectMapper.readValue(item.getString(decoder),
                                ServiceDataDTO.class);
                    } else if (item.getFieldName().equals("FinanceDTO")) {
                        financeDTO = objectMapper.readValue(item.getString(decoder),
                                FinanceDTO.class);
                    } else {
                        email = item.getString(decoder);
                    }
                }
            }
            financeId = financeRepository.saveFinanceByServiceData(financeDTO);
            serviceDataId = serviceDataRepository.saveServiceData(serviceDataDTO, financeId);
            serviceReservationRepository.setServiceDataFk(serviceDataId, serviceDataDTO.getFkCarId());

            for (FileItem item : items) {
                if (!item.isFormField()) {
                    InputStream stream = item.getInputStream();
                    String filename = item.getName();
                    String contentType = item.getContentType();
                    String directory = "/files/client/";
                    new File(DocumentServiceImpl.class.getResource("/files").getPath()).mkdir();
                    new File(DocumentServiceImpl.class.getResource("/files/client").getPath()).mkdir();
                    new File(DocumentServiceImpl.class.getResource(directory)
                            .getPath(), email).mkdir();
                  //  new File(path + directory, email).mkdir();
                    new File(FileSystemRepositoryImpl.class.getResource(directory
                            + email + "/").getPath(), sdf.format(date)).mkdir();
                  //  new File(path + directory + email + "/", sdf.format(date)).mkdir();
                   RESOURCES_DIR = FileSystemRepositoryImpl.class.getResource(directory
                            + email + "/" + sdf.format(date) + "/").getPath().substring(1);
                //    RESOURCES_DIR = path + directory + email + "/" + sdf.format(date) + "/";
                    Path newFile = Paths.get(RESOURCES_DIR + filename);
                    OutputStream out = new FileOutputStream(String.valueOf(newFile));
                    IOUtils.copy(stream, out);
                    stream.close();
                    out.close();
                    File file = new File(RESOURCES_DIR + filename);
                    DocumentRequest storedFile = DocumentRequest.builder()
                            .name(filename)
                            .fileType(contentType)
                            .size(fileSize(file.length()))
                            .fkDocumentCarId(serviceDataDTO.getFkCarId())
                            .fkDocumentServiceData(serviceDataId)
                            .build();
                    documentRepository.saveClientDocument(storedFile, newFile.toString());
                }
            }
        } catch (FileUploadException e) {
            return ("File upload error" + e.toString());
        } catch (IOException e) {
            return ("Internal server IO error" + e.toString());
        }
        return "Successfully uploaded";
    }


    @Override
    public FileSystemResource findDocument(Long documentId) {
        Document document = documentRepository.findById(documentId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        return fileSystemRepository.findInFileSystem(document.getLocation());
    }

    @Override
    public List<FileAndCarResponse> getAllFilesByUser(Long credentialId) {
        List<DocumentDTO> documents = documentRepository.findAllByUser(credentialId);
        return fileAndCarResponseSetter(documents);
    }

    @Override
    public List<FileAndCarResponse> getAllFilesByCar(Long carId, Long credentialId) {
        List<DocumentDTO> documents = documentRepository.findAllByCar(carId, credentialId);
        return fileAndCarResponseSetter(documents);
    }

    private List<FileAndCarResponse> fileAndCarResponseSetter(List<DocumentDTO> documents) {
        Set<FileAndCarResponse> setCars = new HashSet<>();
        List<FileAndCarResponse> response = new ArrayList<>();
        for (DocumentDTO documentDTO : documents) {
            FileAndCarResponse fileAndCarResponse = new FileAndCarResponse();
            fileAndCarResponse.setCarId(documentDTO.getCarId());
            fileAndCarResponse.setBrand(documentDTO.getBrand());
            fileAndCarResponse.setType(documentDTO.getCarType());
            fileAndCarResponse.setLicensePlateNumber(documentDTO.getLicensePlateNumber());
            fileAndCarResponse.setServiceDataId(documentDTO.getServiceDataId());
            fileAndCarResponse.setDate(documentDTO.getUploadTime());
            fileAndCarResponse.setMileage(documentDTO.getMileage());
            setCars.add(fileAndCarResponse);
        }
        for (FileAndCarResponse fileAndCarResponse : setCars) {
            response.add(fileAndCarResponse);
        }
        for (FileAndCarResponse resp : response) {
            List<ResponseFile> responseFiles = new ArrayList<>();
            List<Long> documentIds = new ArrayList<>();
            for (DocumentDTO documentDTO : documents) {
                if (documentDTO.getServiceDataId().equals(resp.getServiceDataId())) {
                    ResponseFile responseFile = new ResponseFile();
                    responseFile.setId(documentDTO.getFileId());
                    responseFile.setType(documentDTO.getDocumentType());
                    responseFile.setName(documentDTO.getName());
                    responseFile.setSize(documentDTO.getSize());
                    documentIds.add(documentDTO.getFileId());
                    responseFiles.add(responseFile);
                }
            }
            resp.setDocumentList(responseFiles);
            resp.setDocumentIds(documentIds);
        }
        return response;
    }

    @Override
    public byte[] zippingFiles(List<Long> id, HttpServletResponse response) throws IOException {
        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy.MM.dd");
        response.setStatus(HttpServletResponse.SC_OK);
        response.addHeader(HttpHeaders.ACCESS_CONTROL_EXPOSE_HEADERS, "Content-Disposition");
        response.addHeader(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + sdf.format(date) + ".zip" + "\"");
        response.addHeader(HttpHeaders.CONTENT_TYPE, "application/octet-stream");

        ZipOutputStream zipOutputStream = new ZipOutputStream(response.getOutputStream());
        for (Long fileId : id) {
            FileSystemResource resource = findDocument(fileId);
            zipOutputStream.putNextEntry(new ZipEntry(resource.getFilename()));
            FileInputStream is = new FileInputStream(resource.getPath());
            IOUtils.copy(is, zipOutputStream);
            is.close();
            zipOutputStream.closeEntry();
        }
        zipOutputStream.close();
        return null;
    }

    @Override
    @Transactional
    public ResponseEntity<byte[]> getFile(Long id, HttpServletResponse response) {
        FileSystemResource fileSystemResource = findDocument(id);
        try {
            InputStream is = new FileInputStream(fileSystemResource.getPath());
            Long fileSize = fileSystemResource.contentLength();
            response.addHeader(HttpHeaders.ACCESS_CONTROL_EXPOSE_HEADERS, "Content-Disposition");
            response.addHeader(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" +
                    fileSystemResource.getFilename() + "\"");
            response.addHeader(HttpHeaders.CONTENT_LENGTH, String.valueOf(fileSize));
            response.addHeader(HttpHeaders.CONTENT_TYPE, "application/blob");
            OutputStream outputStream = null;
            try {
                outputStream = response.getOutputStream();
            } catch (IOException e) {
                String msg = "ERROR: Could not generate output stream.";
                response.setHeader(HttpHeaders.CONTENT_TYPE, String.valueOf((MediaType.TEXT_PLAIN)));
                return new ResponseEntity<byte[]>(msg.getBytes(), HttpStatus.NOT_FOUND);
            }
            byte[] buffer = new byte[1024];
            int read = 0;
            try {
                while ((read = is.read(buffer)) != -1) {
                    outputStream.write(buffer, 0, read);
                }
                outputStream.flush();
                outputStream.close();
                is.close();
            } catch (Exception e) {
                String msg = "ERROR: Could not read file.";
                response.setHeader(HttpHeaders.CONTENT_TYPE, String.valueOf(MediaType.TEXT_PLAIN));
                return new ResponseEntity<byte[]>(msg.getBytes(), HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            String msg = "ERROR: Could not read file.";
            response.setHeader(HttpHeaders.CONTENT_TYPE, String.valueOf(MediaType.TEXT_PLAIN));
            return new ResponseEntity<byte[]>(msg.getBytes(), HttpStatus.NOT_FOUND);
        }
        return null;
    }
}

