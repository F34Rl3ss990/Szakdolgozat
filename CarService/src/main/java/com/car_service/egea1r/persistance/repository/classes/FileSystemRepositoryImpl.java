package com.car_service.egea1r.persistance.repository.classes;

import com.car_service.egea1r.persistance.repository.interfaces.FileSystemRepository;
import com.car_service.egea1r.web.data.payload.request.DocumentRequest;
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
    public void init(){
      //  new File(FileSystemRepositoryImpl.class.getResource("/").getPath(), "files").mkdir();
        File f = new File(System.getProperty("java.class.path"));
        File dir = f.getAbsoluteFile().getParentFile();
        String path = dir.toString();
        new File(path, "files").mkdir();
        new File(path +"/files", "client").mkdir();
        new File(path +"/files", "service").mkdir();
    //    new File(FileSystemRepositoryImpl.class.getResource("/files/").getPath(), "client").mkdir();
    //    new File(FileSystemRepositoryImpl.class.getResource("/files/").getPath(), "service").mkdir();
    }

    @Transactional
    @Override
    public String saveServiceDocument(DocumentRequest document) throws Exception {
      //  RESOURCES_DIR = FileSystemRepositoryImpl.class.getResource("/files/service/").getPath().substring(1);
        File f = new File(System.getProperty("java.class.path"));
        File dir = f.getAbsoluteFile().getParentFile();
        String path = dir.toString();
        RESOURCES_DIR = path + "files/service/";
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
        File f = new File(System.getProperty("java.class.path"));
        File dir = f.getAbsoluteFile().getParentFile();
        String path = dir.toString();
        Date date = new Date();
        String directory = "/files/client";
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy.MM.dd");
        new File (path + "/files/client", email).mkdir();
        new File (path + "/files/client" +  email + "/", sdf.format(date)).mkdir();
      //  new File(FileSystemRepositoryImpl.class.getResource(directory).getPath(), email).mkdir();
      //  new File(FileSystemRepositoryImpl.class.getResource(directory + email + "/").getPath(), sdf.format(date)).mkdir();
      //  RESOURCES_DIR = FileSystemRepositoryImpl.class.getResource(directory + email + "/" + sdf.format(date) + "/").getPath().substring(1);
        RESOURCES_DIR = path + directory + email + "/" + sdf.format(date);
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
