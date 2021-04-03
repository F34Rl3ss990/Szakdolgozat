package com.EGEA1R.CarService.service.classes;

import com.EGEA1R.CarService.persistance.entity.Document;
import com.EGEA1R.CarService.persistance.repository.interfaces.DocumentRepository;
import com.EGEA1R.CarService.persistance.repository.interfaces.FileSystemRepository;
import com.EGEA1R.CarService.service.interfaces.DocumentService;
import com.EGEA1R.CarService.web.DTO.DocumentDTO;
import com.EGEA1R.CarService.web.DTO.payload.request.DocumentRequest;
import com.EGEA1R.CarService.web.DTO.payload.response.FileAndCarResponse;
import com.EGEA1R.CarService.web.DTO.payload.response.ResponseFile;
import com.EGEA1R.CarService.web.exception.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.HttpStatus;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.transaction.Transactional;
import java.io.IOException;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class DocumentServiceImpl implements DocumentService {

    private static DecimalFormat df2 = new DecimalFormat("#.##");

    private DocumentRepository documentRepository;

    private FileSystemRepository fileSystemRepository;

    @Autowired
    public void setDocumentRepository(DocumentRepository documentRepository) {
        this.documentRepository = documentRepository;
    }

    @Autowired
    public void setFileSystemRepository(FileSystemRepository fileSystemRepository){
        this.fileSystemRepository = fileSystemRepository;
    }

    @Transactional
    @Override
    public void storeServiceFiles(List<MultipartFile> fileToStore) throws Exception {
        for(MultipartFile file : fileToStore) {
            String fileName = StringUtils.cleanPath(file.getOriginalFilename());
            long size = file.getBytes().length;
            double sizeToSave = 0;
            String suffix = "";
            df2.setRoundingMode(RoundingMode.UP);
            if(size/(Math.pow(1024,3))>1 && size/(Math.pow(1024,3))<1){
                suffix = "GB";
                sizeToSave = Double.parseDouble(df2.format(size/(Math.pow(1024,3))));
            } else if(size/(Math.pow(1024,2))>1 && size/(Math.pow(1024,3))<1){
                suffix = "MB";
                sizeToSave = Double.parseDouble(df2.format(size/(Math.pow(1024,2))));
            } else if(size/1024>1 && size/(Math.pow(1024,2))<1 || size/1024<1){
                suffix = "KB";
                sizeToSave = Double.parseDouble(df2.format(size/1024));
            }
            DocumentRequest storedFile = DocumentRequest.builder()
                    .name(fileName)
                    .fileType(file.getContentType())
                    .data(file.getBytes())
                    .size(sizeToSave + " " + suffix)
                    .build();
            String location = fileSystemRepository.saveServiceDocument(storedFile);
            documentRepository.saveServiceDocument(storedFile, location);
        }
    }

    @Transactional
    @Override
    public void storeClientFiles(List<MultipartFile> fileToStore, Long fkCarId, String email, Long fkServiceDataId) throws Exception {
        for(MultipartFile file : fileToStore) {
            String fileName = StringUtils.cleanPath(file.getOriginalFilename());
            long size = file.getBytes().length;
            double sizeToSave = 0;
            String suffix = "";
            df2.setRoundingMode(RoundingMode.UP);
            if(size/(Math.pow(1024,3))>1 && size/(Math.pow(1024,3))<1){
                suffix = "GB";
                sizeToSave = Double.parseDouble(df2.format(size/(Math.pow(1024,3))));
            } else if(size/(Math.pow(1024,2))>1 && size/(Math.pow(1024,3))<1){
                suffix = "MB";
                sizeToSave = Double.parseDouble(df2.format(size/(Math.pow(1024,2))));
            } else if(size/1024>1 && size/(Math.pow(1024,2))<1 || size/1024<1){
                suffix = "KB";
                sizeToSave = Double.parseDouble(df2.format(size/1024));
            }
            DocumentRequest storedFile = DocumentRequest.builder()
                    .name(fileName)
                    .fileType(file.getContentType())
                    .data(file.getBytes())
                    .size(sizeToSave + " " + suffix)
                    .fkDocumentCarId(fkCarId)
                    .fkDocumentServiceData(fkServiceDataId)
                    .build();
            String location = fileSystemRepository.saveClientDocument(storedFile, email);
            documentRepository.saveClientDocument(storedFile, location);
        }
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
    public List<FileAndCarResponse> getAllFilesByCar(Long carId) {
        List<DocumentDTO> documents = documentRepository.findAllByCar(carId);
        return fileAndCarResponseSetter(documents);
    }

    private List<FileAndCarResponse> fileAndCarResponseSetter(List<DocumentDTO> documents){
        Set<FileAndCarResponse> setCars = new HashSet<>();
        List<FileAndCarResponse> response = new ArrayList<>();
        for(DocumentDTO documentDTO : documents){
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
        for(FileAndCarResponse fileAndCarResponse : setCars){
            response.add(fileAndCarResponse);
        }
        for(FileAndCarResponse resp : response){
            List<ResponseFile> responseFiles = new ArrayList<>();
            List<Long> documentIds = new ArrayList<>();
            for(DocumentDTO documentDTO : documents){
                if(documentDTO.getServiceDataId().equals(resp.getServiceDataId())) {
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
}

