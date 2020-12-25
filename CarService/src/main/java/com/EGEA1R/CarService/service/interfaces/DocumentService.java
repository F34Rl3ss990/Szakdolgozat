package com.EGEA1R.CarService.service.interfaces;

import com.EGEA1R.CarService.entity.Document;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.stream.Stream;

@Service
public interface DocumentService {

    Document store(MultipartFile fileToStore) throws IOException;

    Document getFile(Long id);

    Stream<Document> getAllFiles();

}
