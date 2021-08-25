package com.car_service.egea1r.service.classes;

import com.car_service.egea1r.CarServiceApplication;
import com.car_service.egea1r.persistance.entity.Document;
import com.car_service.egea1r.persistance.repository.interfaces.*;
import com.car_service.egea1r.web.data.DTO.DocumentDTO;
import com.car_service.egea1r.web.data.DTO.FinanceDTO;
import com.car_service.egea1r.web.data.DTO.ServiceDataDTO;
import com.car_service.egea1r.web.data.mapper.MapStructObjectMapper;
import com.car_service.egea1r.web.data.payload.request.DocumentRequest;
import com.car_service.egea1r.web.data.payload.response.FileAndCarResponse;
import com.car_service.egea1r.web.data.payload.response.ResponseFile;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.lang.ArrayUtils;
import org.apache.tomcat.util.http.fileupload.FileUpload;
import org.junit.Before;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.mock.web.*;
import org.springframework.web.server.ResponseStatusException;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.*;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class DocumentServiceImplTest {

    @Mock
    DocumentRepository documentRepository;
    @Mock
    FileSystemRepository fileSystemRepository;
    @Mock
    ServiceReservationRepository serviceReservationRepository;
    @Mock
    FinanceRepository financeRepository;
    @Mock
    ServiceDataRepository serviceDataRepository;
    @Mock
    MapStructObjectMapper mapStructObjectMapper;

    @InjectMocks
    DocumentServiceImpl documentService;

    private static final String DATE_FORMAT = "yyyy.MM.dd";
    private static final DecimalFormat df2 = new DecimalFormat("#.##");

    @BeforeEach
    void setUp() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        Method postConstruct = DocumentServiceImpl.class.getDeclaredMethod("init", null);
        postConstruct.setAccessible(true);
        postConstruct.invoke(documentService);
    }

//    @AfterEach
//    void setDown() throws UnsupportedEncodingException {
//        String filePath = CarServiceApplication.class.getProtectionDomain().getCodeSource().getLocation().getPath();
//        String decodedPath = URLDecoder.decode(filePath, "UTF-8");
//        File newFile = new File(decodedPath);
//        newFile.delete();
//    }

    private byte[] createFileContent(byte[] data, String boundary, String contentType, String fileName) {
        String start = "--" + boundary + "\r\n Content-Disposition: form-data; name=\"file\"; filename=\"" + fileName + "\"\r\n"
                + "Content-type: " + contentType + "\r\n\r\n";
        ;

        String end = "\r\n--" + boundary + "--";
        return ArrayUtils.addAll(start.getBytes(), ArrayUtils.addAll(data, end.getBytes()));
    }

    @Test
    void uploadService_whenValid_thenCorrect() throws IOException {
        String fileName = "kép.jpg";
        String fileType = "image/jpeg";
        String fileName2 = "Miskolci Egyetem.pdf";
        String fileType2 = "application/pdf";
        Path path = Paths.get("d:\\" + fileName);
        Path path2 = Paths.get("d:\\" + fileName2);
        byte[] data = Files.readAllBytes(path);
        byte[] data2 = Files.readAllBytes(path2);
        MockMultipartFile file = new MockMultipartFile(fileName, fileName,
                fileType, data);
        MockMultipartFile file2 = new MockMultipartFile(fileName2, fileName2,
                fileType2, data2);
        MockMultipartFile[] multipartFiles = new MockMultipartFile[]{file, file2};

        String size = (Double.parseDouble(df2.format(data.length / 1024))) + " KB";
        String size2 = (Double.parseDouble(df2.format(data2.length / 1024))) + " KB";
        String filePath = CarServiceApplication.class.getProtectionDomain().getCodeSource().getLocation().getPath();
        String decodedPath = URLDecoder.decode(filePath, "UTF-8");
        decodedPath = (decodedPath + "files/service/").substring(1);
        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT);
        Path newFile = Paths.get(decodedPath + sdf.format(date) + "/" + fileName);
        Path newFile2 = Paths.get(decodedPath + sdf.format(date) + "/" + fileName2);
        DocumentRequest documentRequest = DocumentRequest.builder()
                .name(fileName)
                .fileType(fileType)
                .size(size)
                .build();
        DocumentRequest documentRequest2 = DocumentRequest.builder()
                .name(fileName2)
                .fileType(fileType2)
                .size(size2)
                .build();

        List<String> actualMessage = documentService.uploadService(multipartFiles);
        verify(documentRepository, times(1)).saveServiceDocument(documentRequest, newFile.toString());
        verify(documentRepository, times(1)).saveServiceDocument(documentRequest2, newFile2.toString());
        assertTrue(actualMessage.contains(file.getOriginalFilename()));
        assertTrue(actualMessage.contains(file2.getOriginalFilename()));
        File oldFile = new File(newFile.toString());
        oldFile.delete();
    }

    @Test
    void storeClientBigFiles_whenValid_thenCorrect() throws IOException, FileUploadException {
        String fileName = "kép.jpg";
        String fileType = "image/jpeg";
        String fileName2 = "Miskolci Egyetem.pdf";
        String fileType2 = "application/pdf";
        Path path = Paths.get("d:\\" + fileName);
        Path path2 = Paths.get("d:\\" + fileName2);
        byte[] data = Files.readAllBytes(path);
        byte[] data2 = Files.readAllBytes(path2);
        long financeId = 5L;
        long serviceDataId = 6L;
        String email = "asd@asd.hu";
        MockMultipartFile file = new MockMultipartFile(fileName, fileName,
                fileType, data);
        MockMultipartFile file2 = new MockMultipartFile(fileName2, fileName2,
                fileType2, data2);
        MockMultipartFile[] multipartFiles = new MockMultipartFile[]{file, file2};

        String size = (Double.parseDouble(df2.format(data.length / 1024))) + " KB";
        String size2 = (Double.parseDouble(df2.format(data2.length / 1024))) + " KB";
        String filePath = CarServiceApplication.class.getProtectionDomain().getCodeSource().getLocation().getPath();
        String decodedPath = URLDecoder.decode(filePath, "UTF-8");
        decodedPath = (decodedPath + "files/client/").substring(1);
        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT);
        Path newFile = Paths.get(decodedPath + email + "/" + sdf.format(date) + "/" + fileName);
        Path newFile2 = Paths.get(decodedPath + email + "/" + sdf.format(date) + "/" + fileName2);

        ServiceDataDTO serviceDataDTO = ServiceDataDTO.builder()
                .mileage("200")
                .brand("audi")
                .type("a4")
                .fkCarId(4L)
                .build();
        FinanceDTO financeDTO = FinanceDTO.builder()
                .accountName("Abc Bca")
                .amount("20000")
                .isPaid(false)
                .build();
        DocumentRequest documentRequest = DocumentRequest.builder()
                .name(fileName)
                .fileType(fileType)
                .size(size)
                .fkDocumentCarId(serviceDataDTO.getFkCarId())
                .fkDocumentServiceData(serviceDataId)
                .build();
        DocumentRequest documentRequest2 = DocumentRequest.builder()
                .name(fileName2)
                .fileType(fileType2)
                .size(size2)
                .fkDocumentCarId(serviceDataDTO.getFkCarId())
                .fkDocumentServiceData(serviceDataId)
                .build();


        given(financeRepository.saveFinanceByServiceData(financeDTO)).willReturn(financeId);
        given(serviceDataRepository.saveServiceData(serviceDataDTO, financeId)).willReturn(serviceDataId);

        List<String> fileNames = documentService.upload(multipartFiles, serviceDataDTO, financeDTO, email);

        verify(documentRepository, times(1)).saveClientDocument(documentRequest, newFile.toString());
        verify(documentRepository, times(1)).saveClientDocument(documentRequest2, newFile2.toString());
        verify(financeRepository, times(1)).saveFinanceByServiceData(financeDTO);
        verify(serviceDataRepository, times(1)).saveServiceData(serviceDataDTO, financeId);
        verify(serviceReservationRepository, times(1)).setServiceDataFk(serviceDataId, serviceDataDTO.getFkCarId());
        assertEquals(2, fileNames.size());
        assertTrue(fileNames.contains(file.getOriginalFilename()));
        assertTrue(fileNames.contains(file2.getOriginalFilename()));

        File oldFile = new File(newFile.toString());
        oldFile.delete();
    }

    @Test
    void zippingFiles_whenValid_thenCorrect() throws IOException {
        List<Long> ids = new ArrayList<>();
        ids.add(1L);
        ids.add(2L);
        String fileName = "kép.jpg";
        String fileName2 = "Miskolci Egyetem.pdf";
        MockHttpServletResponse mockHttpServletResponse = new MockHttpServletResponse();
        Document document = Document.builder()
                .location("d:\\" + fileName)
                .build();

        Document document1 = Document.builder()
                .location("d:\\" + fileName2)
                .build();

        FileSystemResource fileSystemResource = new FileSystemResource(Paths.get(document.getLocation()));
        FileSystemResource fileSystemResource1 = new FileSystemResource(Paths.get(document1.getLocation()));
        given(documentRepository.findById(ids.get(0))).willReturn(Optional.of(document));
        given(documentRepository.findById(ids.get(1))).willReturn(Optional.of(document1));
        given(fileSystemRepository.findInFileSystem(document.getLocation())).willReturn(fileSystemResource);
        given(fileSystemRepository.findInFileSystem(document1.getLocation())).willReturn(fileSystemResource1);

        documentService.zippingFiles(ids, mockHttpServletResponse);

        verify(documentRepository, times(1)).findById(ids.get(0));
        verify(documentRepository, times(1)).findById(ids.get(1));
        verify(fileSystemRepository, times(1)).findInFileSystem(document.getLocation());
        verify(fileSystemRepository, times(1)).findInFileSystem(document1.getLocation());
    }

    @Test
    void zippingFiles_whenNotFound_thenException() throws IOException {
        List<Long> ids = new ArrayList<>();
        ids.add(1L);
        String fileName = "kép3.jpg";
        MockHttpServletResponse mockHttpServletResponse = new MockHttpServletResponse();
        Document document = Document.builder()
                .location("d:\\" + fileName)
                .build();


        FileSystemResource fileSystemResource = new FileSystemResource(Paths.get(document.getLocation()));
        given(documentRepository.findById(ids.get(0))).willReturn(Optional.of(document));
        given(fileSystemRepository.findInFileSystem(document.getLocation())).willReturn(fileSystemResource);

        Exception exception = assertThrows(FileNotFoundException.class, () -> documentService.zippingFiles(ids, mockHttpServletResponse));
        String expectedMessage = "";
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
        verify(documentRepository, times(1)).findById(ids.get(0));
        verify(fileSystemRepository, times(1)).findInFileSystem(document.getLocation());
    }

    @Test
    void download_whenValid_thenCorrect() {
        long id = 4L;
        String location = "d:\\kép2.png";
        Document document = Document.builder()
                .location(location)
                .build();
        FileSystemResource fileSystemResource = new FileSystemResource(Paths.get(location));
        given(documentRepository.findById(id)).willReturn(Optional.of(document));
        given(fileSystemRepository.findInFileSystem(document.getLocation())).willReturn(fileSystemResource);

        Resource resource = documentService.download(id);

        assertEquals(resource.getFilename(), "kép2.png");
        verify(documentRepository, times(1)).findById(id);
        verify(fileSystemRepository, times(1)).findInFileSystem(document.getLocation());
    }

    @Test
    void download_whenNotFound_thenException() {
        long id = 4L;
        String location = "d:\\kép2.jpg";
        Document document = Document.builder()
                .location(location)
                .build();
        FileSystemResource fileSystemResource = new FileSystemResource(Paths.get(location));
        given(documentRepository.findById(id)).willReturn(Optional.of(document));
        given(fileSystemRepository.findInFileSystem(document.getLocation())).willReturn(fileSystemResource);

        Exception exception = assertThrows(RuntimeException.class, () -> documentService.download(id));

        String expectedMessage = "Could not read the file!";
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
        verify(documentRepository, times(1)).findById(id);
        verify(fileSystemRepository, times(1)).findInFileSystem(document.getLocation());
    }

    @Test
    void findDocument_whenFound_thenCorrect() {
        long documentId = 4L;
        String location = "drive/files/fileName";

        Document document = Document.builder()
                .fileId(3L)
                .name("randomName")
                .location(location)
                .build();

        given(documentRepository.findById(documentId)).willReturn(Optional.of(document));

        Document returnValue = documentService.findDocument(documentId);

        assertEquals(returnValue, document);
        verify(documentRepository, times(1)).findById(documentId);
    }

    @Test
    void findDocument_whenNotFound_thenInvalid() {
        long documentId = 4L;
        String location = "drive/files/fileName";

        Document document = Document.builder()
                .fileId(3L)
                .name("randomName")
                .location(location)
                .build();

        Exception exception = assertThrows(ResponseStatusException.class, () -> documentService.findDocument(documentId));
        String expectedMessage = "NOT_FOUND";
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
        verify(documentRepository, times(1)).findById(documentId);
    }

    @Test
    void getAllFilesByUser_whenValid_thenCorrect() {
        long credentialId = 4L;
        List<DocumentDTO> documentDTOS = new ArrayList<>();
        DocumentDTO documentDTO = DocumentDTO.builder()
                .brand("Audi")
                .carId(3L)
                .carType("A4")
                .serviceDataId(3L)
                .build();
        DocumentDTO documentDTO1 = DocumentDTO.builder()
                .brand("Audi")
                .carId(5L)
                .carType("A5")
                .serviceDataId(5L)
                .build();
        FileAndCarResponse fileAndCarResponse = FileAndCarResponse.builder()
                .brand("Audi")
                .carId(3L)
                .type("A4")
                .serviceDataId(3L)
                .build();
        FileAndCarResponse fileAndCarResponse2 = FileAndCarResponse.builder()
                .brand("Audi")
                .carId(5L)
                .type("A5")
                .serviceDataId(5L)
                .build();
        ResponseFile responseFile = ResponseFile.builder()
                .id(3L)
                .type("A4")
                .build();
        ResponseFile responseFile2 = ResponseFile.builder()
                .id(5L)
                .type("A5")
                .build();
        documentDTOS.add(documentDTO);
        documentDTOS.add(documentDTO1);

        given(documentRepository.findAllByUser(credentialId)).willReturn(documentDTOS);
        given(mapStructObjectMapper.documentDTOtoFileAndCarResponse(documentDTO)).willReturn(fileAndCarResponse);
        given(mapStructObjectMapper.documentDTOtoFileAndCarResponse(documentDTO1)).willReturn(fileAndCarResponse2);
        given(mapStructObjectMapper.documentDTOtoResponseFile(documentDTO)).willReturn(responseFile);
        given(mapStructObjectMapper.documentDTOtoResponseFile(documentDTO1)).willReturn(responseFile2);

        Set<FileAndCarResponse> returnSet = documentService.getAllFilesByUser(credentialId);

        assertEquals(2, returnSet.size());
        assertTrue(returnSet.contains(fileAndCarResponse));
        assertTrue(returnSet.contains(fileAndCarResponse2));
        verify(documentRepository, times(1)).findAllByUser(credentialId);
        verify(mapStructObjectMapper, times(1)).documentDTOtoFileAndCarResponse(documentDTO);
        verify(mapStructObjectMapper, times(1)).documentDTOtoFileAndCarResponse(documentDTO1);
        verify(mapStructObjectMapper, times(1)).documentDTOtoResponseFile(documentDTO);
        verify(mapStructObjectMapper, times(1)).documentDTOtoResponseFile(documentDTO1);
    }

    @Test
    void getAllFilesByCar_whenValid_thenCorrect() {
        long credentialId = 4L;
        long carId = 5L;
        List<DocumentDTO> documentDTOS = new ArrayList<>();
        DocumentDTO documentDTO = DocumentDTO.builder()
                .brand("Audi")
                .carId(3L)
                .carType("A4")
                .serviceDataId(3L)
                .build();
        DocumentDTO documentDTO1 = DocumentDTO.builder()
                .brand("Audi")
                .carId(5L)
                .carType("A5")
                .serviceDataId(5L)
                .build();
        FileAndCarResponse fileAndCarResponse = FileAndCarResponse.builder()
                .brand("Audi")
                .carId(3L)
                .type("A4")
                .serviceDataId(3L)
                .build();
        FileAndCarResponse fileAndCarResponse2 = FileAndCarResponse.builder()
                .brand("Audi")
                .carId(5L)
                .type("A5")
                .serviceDataId(5L)
                .build();
        ResponseFile responseFile = ResponseFile.builder()
                .id(3L)
                .type("A4")
                .build();
        ResponseFile responseFile2 = ResponseFile.builder()
                .id(5L)
                .type("A5")
                .build();
        documentDTOS.add(documentDTO);
        documentDTOS.add(documentDTO1);

        given(documentRepository.findAllByCar(carId, credentialId)).willReturn(documentDTOS);
        given(mapStructObjectMapper.documentDTOtoFileAndCarResponse(documentDTO)).willReturn(fileAndCarResponse);
        given(mapStructObjectMapper.documentDTOtoFileAndCarResponse(documentDTO1)).willReturn(fileAndCarResponse2);
        given(mapStructObjectMapper.documentDTOtoResponseFile(documentDTO)).willReturn(responseFile);
        given(mapStructObjectMapper.documentDTOtoResponseFile(documentDTO1)).willReturn(responseFile2);

        Set<FileAndCarResponse> returnSet = documentService.getAllFilesByCar(carId, credentialId);

        assertEquals(2, returnSet.size());
        assertTrue(returnSet.contains(fileAndCarResponse));
        assertTrue(returnSet.contains(fileAndCarResponse2));
        verify(documentRepository, times(1)).findAllByCar(carId, credentialId);
        verify(mapStructObjectMapper, times(1)).documentDTOtoFileAndCarResponse(documentDTO);
        verify(mapStructObjectMapper, times(1)).documentDTOtoFileAndCarResponse(documentDTO1);
        verify(mapStructObjectMapper, times(1)).documentDTOtoResponseFile(documentDTO);
        verify(mapStructObjectMapper, times(1)).documentDTOtoResponseFile(documentDTO1);
    }
}
