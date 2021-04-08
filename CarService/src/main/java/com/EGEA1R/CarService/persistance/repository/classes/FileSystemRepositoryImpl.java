package com.EGEA1R.CarService.persistance.repository.classes;

import com.EGEA1R.CarService.persistance.repository.interfaces.FileSystemRepository;
import com.EGEA1R.CarService.web.DTO.payload.request.DocumentRequest;
import org.springframework.core.io.FileSystemResource;
import org.springframework.stereotype.Repository;

import javax.annotation.PostConstruct;
import javax.transaction.Transactional;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Date;

@Repository
public class FileSystemRepositoryImpl implements FileSystemRepository {

    String RESOURCES_DIR = "";

    @PostConstruct
    public void init() {
        new File(FileSystemRepositoryImpl.class.getResource("/").getPath(), "files").mkdir();
        new File(FileSystemRepositoryImpl.class.getResource("/files/").getPath(), "client").mkdir();
        new File(FileSystemRepositoryImpl.class.getResource("/files/").getPath(), "service").mkdir();
    }

    @Transactional
    @Override
    public String saveServiceDocument(DocumentRequest document) throws Exception {
        RESOURCES_DIR = FileSystemRepositoryImpl.class.getResource("/files/service/").getPath().substring(1);
        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy.MM.dd");
        Path newFile = Paths.get(RESOURCES_DIR + sdf.format(date) + "-" + document.getName());
        Files.createDirectories(newFile.getParent());

        Files.write(newFile, document.getData());

        return newFile.toAbsolutePath()
                .toString();
    }

    @Transactional
    @Override
    public String saveClientDocument(DocumentRequest document, String email) throws Exception {
        Date date = new Date();
        String directory = "/files/client";
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy.MM.dd");
        new File(FileSystemRepositoryImpl.class.getResource(directory).getPath(), email).mkdir();
        new File(FileSystemRepositoryImpl.class.getResource(directory + email + "/").getPath(), sdf.format(date)).mkdir();
        RESOURCES_DIR = FileSystemRepositoryImpl.class.getResource(directory + email + "/" + sdf.format(date) + "/").getPath().substring(1);
        Path newFile = Paths.get(RESOURCES_DIR + document.getName());
        Files.createDirectories(newFile.getParent());

        Files.write(newFile, document.getData());

        return newFile.toAbsolutePath()
                .toString();
    }

    @Override
    public FileSystemResource findInFileSystem(String location) {
            return new FileSystemResource(Paths.get(location));
    }
}
