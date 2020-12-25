package com.EGEA1R.CarService.service;

import com.EGEA1R.CarService.entity.Document;
import com.EGEA1R.CarService.repository.DocumentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.stream.Stream;

@Service
public class DocumentServiceImpl implements DocumentService {

    private DocumentRepository documentRepository;

    @Autowired
    public void setDocumentRepository(DocumentRepository documentRepository){
        this.documentRepository = documentRepository;
    }

    public Document store(MultipartFile fileToStore) throws IOException {
        String fileName = StringUtils.cleanPath(fileToStore.getOriginalFilename());
        Document storedFile =  Document.builder()
                .name(fileName)
                .type(fileToStore.getContentType())
                .data(fileToStore.getBytes())
                .build();
        return documentRepository.save(storedFile);
    }


    @Override
    public Document getFile(Long id) {
        return documentRepository.findById(id).get();
    }

    public Stream<Document> getAllFiles() {
        return documentRepository.findAll().stream();
    }
}
