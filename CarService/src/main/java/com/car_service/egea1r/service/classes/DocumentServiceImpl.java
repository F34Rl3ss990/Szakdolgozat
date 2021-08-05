package com.car_service.egea1r.service.classes;

import com.car_service.egea1r.persistance.entity.Document;
import com.car_service.egea1r.persistance.repository.classes.FileSystemRepositoryImpl;
import com.car_service.egea1r.persistance.repository.interfaces.*;
import com.car_service.egea1r.service.interfaces.DocumentService;
import com.car_service.egea1r.web.data.DTO.DocumentDTO;
import com.car_service.egea1r.web.data.DTO.FinanceDTO;
import com.car_service.egea1r.web.data.DTO.ServiceDataDTO;
import com.car_service.egea1r.web.data.mapper.MapStructObjectMapper;
import com.car_service.egea1r.web.data.payload.request.DocumentRequest;
import com.car_service.egea1r.web.data.payload.response.FileAndCarResponse;
import com.car_service.egea1r.web.data.payload.response.ResponseFile;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.compress.utils.IOUtils;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileItemIterator;
import org.apache.commons.fileupload.FileItemStream;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
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

    private static final DecimalFormat df2 = new DecimalFormat("#.##");
    private static final String DATE_FORMAT = "yyyy.MM.dd";

    private final DocumentRepository documentRepository;
    private final FileSystemRepository fileSystemRepository;
    private final ServiceReservationRepository serviceReservationRepository;
    private final FinanceRepository financeRepository;
    private final ServiceDataRepository serviceDataRepository;
    private final MapStructObjectMapper mapStructObjectMapper;

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

    @Override
    @Transactional
    public String storeServiceBigFiles(HttpServletRequest request) {
        String RESOURCES_DIR = DocumentServiceImpl.class.getResource("/files/service/").getPath().substring(1);
        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT);
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
            return ("File upload error" + e);
        } catch (IOException e) {
            return ("Internal server IO error" + e);
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
    public void storeClientBigFiles(HttpServletRequest request) throws IOException, FileUploadException {
        String RESOURCES_DIR;
        new File(DocumentServiceImpl.class.getResource("/files").getPath()).mkdir();
        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT);
        ServiceDataDTO serviceDataDTO = new ServiceDataDTO();
        FinanceDTO financeDTO = new FinanceDTO();
        String email = "";
        long financeId;
        long serviceDataId;
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
                    new File(FileSystemRepositoryImpl.class.getResource(directory
                            + email + "/").getPath(), sdf.format(date)).mkdir();
                   RESOURCES_DIR = FileSystemRepositoryImpl.class.getResource(directory
                            + email + "/" + sdf.format(date) + "/").getPath().substring(1);
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
            throw new FileUploadException ("File upload error" + e);
        } catch (IOException e) {
            throw new IOException(e);
        }
    }


    @Override
    public FileSystemResource findDocument(long documentId) {
        Document document = documentRepository.findById(documentId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        return fileSystemRepository.findInFileSystem(document.getLocation());
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

    @Override
    public void zippingFiles(List<Long> id, HttpServletResponse response) throws IOException {
        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT);
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
    }

    @Override
    @Transactional
    public ResponseEntity<byte[]> getFile(long id, HttpServletResponse response) {
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
                return new ResponseEntity<>(msg.getBytes(), HttpStatus.NOT_FOUND);
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
                return new ResponseEntity<>(msg.getBytes(), HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            String msg = "ERROR: Could not read file.";
            response.setHeader(HttpHeaders.CONTENT_TYPE, String.valueOf(MediaType.TEXT_PLAIN));
            return new ResponseEntity<>(msg.getBytes(), HttpStatus.NOT_FOUND);
        }
        return null;
    }
}

