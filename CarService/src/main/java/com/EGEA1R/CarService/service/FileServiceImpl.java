package com.EGEA1R.CarService.service;

import com.EGEA1R.CarService.repository.FileRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class FileServiceImpl implements FileService{

    private FileRepository fileRepository;

    @Autowired
    public void setFileRepository(FileRepository fileRepository){
        this. fileRepository = fileRepository;
    }
}
