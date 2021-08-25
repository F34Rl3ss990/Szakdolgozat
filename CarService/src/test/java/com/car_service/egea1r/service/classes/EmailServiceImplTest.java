package com.car_service.egea1r.service.classes;

import com.car_service.egea1r.CarServiceApplication;
import com.car_service.egea1r.web.data.DTO.UnauthorizedUserReservationDTO;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring5.SpringTemplateEngine;

import javax.mail.MessagingException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URLDecoder;
import java.nio.file.Path;
import java.nio.file.Paths;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class EmailServiceImplTest {

    @Mock
    JavaMailSender mailSender;

    @Mock
    SpringTemplateEngine springTemplateEngine;

    @InjectMocks
    EmailServiceImpl emailService;

//    @Test
//    void sendSimpleMessage() {
//        String subject = "subject";
//        String to = "to@to.to";
//        String text = "text";
//        SimpleMailMessage message = new SimpleMailMessage();
//        message.setFrom("from@from.from");
//        message.setTo(to);
//        message.setSubject(subject);
//        message.setText(text);
//
//        emailService.sendSimpleMessage(to, subject, text);
//        verify(mailSender, times(1)).send(message);
//    }
//
//    @Test
//    void sendMessageWithAttachment() throws MessagingException {
//        String to = "to@to.to";
//        String subject = "subject";
//        String text = "text";
//        String pathToAttachment = "d:\\kép2.png";
//        MimeMessage message = mailSender.createMimeMessage();
//        given(mailSender.createMimeMessage()).willReturn(message);
//        MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
//        helper.setSubject(subject);
//        helper.setTo(to);
//        helper.setText(text);
//        FileSystemResource fileSystemResource = new FileSystemResource(new File(pathToAttachment));
//        helper.addAttachment("Invoice", fileSystemResource);
//
//
//        mailSender.send(message);
//
//        verify(mailSender, times(1)).send(message);
//        verify(mailSender, times(1)).createMimeMessage();
//    }
//
//    @Test
//    void sendVerificationToken() throws MessagingException, MalformedURLException, UnsupportedEncodingException {
//        String subject = "Verification email";
//        String html = "VerificationEmailTemplate.html";
//        String to = "to@to.to";
//        String token = "randomUUID";
//        Context context = new Context();
//        context.setVariable("token", token);
//        UnauthorizedUserReservationDTO unauthorizedUserReservationDTO = UnauthorizedUserReservationDTO.builder()
//                .email("email@email.email")
//                .build();
//        String htmlBody = "returnValue";
//        given(springTemplateEngine.process(html, context)).willReturn(htmlBody);
//        String filePath = CarServiceApplication.class.getProtectionDomain().getCodeSource().getLocation().getPath();
//        String decodedPath = URLDecoder.decode(filePath, "UTF-8");
//        decodedPath = (decodedPath + "carServiceLogo.png").substring(1);
//        Resource resource = new UrlResource(Paths.get(decodedPath).toUri());
//        MimeMessage message = mailSender.createMimeMessage();
//        given(mailSender.createMimeMessage()).willReturn(message);
//        MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
//        helper.setSubject(subject);
//        helper.setTo(to);
//        helper.setText(htmlBody, true);
//        helper.addInline("carServiceLogo", resource);
//        message.setFrom(new InternetAddress("carservice9900@gmail.com", "Car service"));
//
//        emailService.sendVerificationToken(to, token);
//
//        verify(mailSender, times(1)).send(message);
//        verify(springTemplateEngine, times(1)).process(html, context);
//        verify(mailSender, times(1)).createMimeMessage();
//    }

    @Test
    void resendVerificationToken() {
    }

    @Test
    void sendResetPasswordToken() {
    }

    @Test
    void sendReservedServiceInformation() {
    }
}
