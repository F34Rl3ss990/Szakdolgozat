package com.EGEA1R.CarService.service.classes;

import com.EGEA1R.CarService.service.interfaces.EmailService;
import com.EGEA1R.CarService.web.DTO.UnauthorizedUserReservationDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.thymeleaf.spring5.SpringTemplateEngine;
import org.thymeleaf.context.Context;

import javax.mail.MessagingException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.io.File;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;

@Service
public class EmailServiceImpl implements EmailService {


    private JavaMailSender emailSender;

    private SpringTemplateEngine thymeleafTemplateEngine;

    @Autowired
    public void setJavaMailSender(JavaMailSender emailSender){
        this.emailSender = emailSender;
    }

    @Autowired
    public void setThymeleafTemplateEngine(SpringTemplateEngine thymeleafTemplateEngine){
        this.thymeleafTemplateEngine = thymeleafTemplateEngine;
    }

    @Value("classpath:/unnamed.png")
    Resource resourceFile;

    @Override
    public void sendSimpleMessage(String to, String subject, String text) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("noreply@carservice.com");
        message.setTo(to);
        message.setSubject(subject);
        message.setText(text);
        emailSender.send(message);
    }

    public void sendMessageWithAttachment(String to, String subject, String text, String pathToAttachment) throws MessagingException, UnsupportedEncodingException {
            MimeMessage message = emailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
            helper.setFrom(new InternetAddress("carservice9900@gmail.com", "Car service"));
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(text);

            FileSystemResource file = new FileSystemResource(new File(pathToAttachment));
            helper.addAttachment("Invoice", file);

            emailSender.send(message);
    }

    private void sendHtmlMessage(String subject, String to, String htmlBody) throws MessagingException, UnsupportedEncodingException {
        MimeMessage message = emailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
        helper.setTo(to);
        helper.setSubject(subject);
        helper.setText(htmlBody, true);
        helper.addInline("unnamed", resourceFile);
        message.setFrom(new InternetAddress("carservice9900@gmail.com", "Car service"));
        emailSender.send(message);
    }




    private void sendMessageUsingThymeleafTemplate(
            String to, String subject, String html, Context thymeleafContext) throws MessagingException, UnsupportedEncodingException {
        String htmlBody = thymeleafTemplateEngine.process(html, thymeleafContext);
        sendHtmlMessage(subject, to, htmlBody);
    }

    @Override
    public void sendVerificationToken(String to, String token) throws MessagingException, UnsupportedEncodingException {
        String subject = "Verification email";
        String html = "VerificationEmailTemplate.html";
        Context thymeleafContext = new Context();
        tokenContext(thymeleafContext, token);
        sendMessageUsingThymeleafTemplate(to, subject, html, thymeleafContext);
    }

    @Override
    public void resendVerificationToken(String to, String token) throws MessagingException, UnsupportedEncodingException {
        String subject = "Verification mail resent";
        String html = "VerificationEmailResendTemplate.html";
        Context thymeleafContext = new Context();
        tokenContext(thymeleafContext, token);
        sendMessageUsingThymeleafTemplate(to, subject, html, thymeleafContext);
    }

    @Override
    public void sendResetPasswordToken(String to, String token) throws MessagingException, UnsupportedEncodingException {
        String subject = "Password reset token mail";
        String html = "PasswordResetTemplate.html";
        Context thymeleafContext = new Context();
        tokenContext(thymeleafContext, token);
        sendMessageUsingThymeleafTemplate(to, subject, html, thymeleafContext);
    }

    @Async
    @Override
    public void sendReservedServiceInformation(UnauthorizedUserReservationDTO unauthorizedUserReservationDTO) throws MessagingException, UnsupportedEncodingException {
        String subject ="Reserved service information";
        String html ="ReservedService.html";
        Context thymeleafContext = new Context();
        serviceContext(thymeleafContext, unauthorizedUserReservationDTO);
        sendMessageUsingThymeleafTemplate(unauthorizedUserReservationDTO.getEmail(), subject, html, thymeleafContext);
    }

    private void tokenContext(Context thymeleafContext, String token){
        thymeleafContext.setVariable("token", token);
    }

    private void serviceContext(Context context, UnauthorizedUserReservationDTO unauthorizedUserReservationDTO){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy.MM.dd");
        context.setVariable("name", unauthorizedUserReservationDTO.getName());
        context.setVariable("email", unauthorizedUserReservationDTO.getEmail());
        context.setVariable("phoneNumber", unauthorizedUserReservationDTO.getPhoneNumber());
        context.setVariable("brand", unauthorizedUserReservationDTO.getBrand());
        context.setVariable("type", unauthorizedUserReservationDTO.getType());
        context.setVariable("engineType", unauthorizedUserReservationDTO.getEngineType());
        context.setVariable("yearOfManufacture", unauthorizedUserReservationDTO.getYearOfManufacture());
        context.setVariable("engineNumber", unauthorizedUserReservationDTO.getEngineNumber());
        context.setVariable("chassisNumber", unauthorizedUserReservationDTO.getChassisNumber());
        context.setVariable("mileage", unauthorizedUserReservationDTO.getMileage());
        context.setVariable("licensePlate", unauthorizedUserReservationDTO.getLicensePlateNumber());
        context.setVariable("reservedDate", sdf.format(unauthorizedUserReservationDTO.getReservedDate()));
        context.setVariable("reservedServices", unauthorizedUserReservationDTO.getReservedServices());
        context.setVariable("comment", unauthorizedUserReservationDTO.getComment());
        context.setVariable("billingName", unauthorizedUserReservationDTO.getBillingName());
        context.setVariable("billingPhoneNumber", unauthorizedUserReservationDTO.getBillingPhoneNumber());
        context.setVariable("billingEmail", unauthorizedUserReservationDTO.getBillingEmail());
        context.setVariable("billingTax", unauthorizedUserReservationDTO.getBillingTaxNumber());
        context.setVariable("billingZipCode", unauthorizedUserReservationDTO.getBillingZipCode());
        context.setVariable("billingTown", sdf.format(unauthorizedUserReservationDTO.getBillingTown()));
        context.setVariable("billingStreet", unauthorizedUserReservationDTO.getBillingStreet());
        context.setVariable("billingOtherAddressType", unauthorizedUserReservationDTO.getBillingOtherAddressType());

    }
}
