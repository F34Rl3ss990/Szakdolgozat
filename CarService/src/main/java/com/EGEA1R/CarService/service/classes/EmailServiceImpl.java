package com.EGEA1R.CarService.service.classes;

import com.EGEA1R.CarService.service.interfaces.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.thymeleaf.spring5.SpringTemplateEngine;
import org.thymeleaf.context.Context;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.File;

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
       try{
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("noreply@carservice.com");
        message.setTo(to);
        message.setSubject(subject);
        message.setText(text);
        emailSender.send(message);
    } catch (MailException exception) {
        exception.printStackTrace();
    }
    }

    public void sendMessageWithAttachment(String to, String subject, String text, String pathToAttachment) throws MessagingException {
        try {
            MimeMessage message = emailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
            helper.setFrom("noreply@carservice.com");
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(text);

            FileSystemResource file = new FileSystemResource(new File(pathToAttachment));
            helper.addAttachment("Invoice", file);

            emailSender.send(message);
        } catch (MessagingException e){
            e.printStackTrace();
        }
    }

    private void sendHtmlMessage(String subject, String to, String htmlBody) throws MessagingException {
        MimeMessage message = emailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
        helper.setFrom("noreply@carservice.com");
        helper.setTo(to);
        helper.setSubject(subject);
        helper.setText(htmlBody, true);
        helper.addInline("unnamed", resourceFile);
        emailSender.send(message);
    }


    private void sendMessageUsingThymeleafTemplate(
            String to, String token, String subject, String html)
            throws MessagingException {

        Context thymeleafContext = new Context();
        thymeleafContext.setVariable("token",  token);
        String htmlBody = thymeleafTemplateEngine.process(html, thymeleafContext);

        sendHtmlMessage(subject, to, htmlBody);
    }

    @Override
    public void sendVerificationToken(String to, String token) throws MessagingException {
        String subject = "Verification email";
        String html = "VerificationEmailTemplate.html";
        sendMessageUsingThymeleafTemplate(to, token, subject, html);
    }

    @Override
    public void resendVerificationToken(String to, String token) throws MessagingException {
        String subject = "Vericifation mail resent";
        String html = "PasswordResetTemplate.html";
        sendMessageUsingThymeleafTemplate(to, token, subject, html);
    }

    @Override
    public void sendResetPasswordToken(String to, String token) throws MessagingException {
        String subject = "Password reset token mail";
        String html = "VerificationEmailResendTemplate.html";
        sendMessageUsingThymeleafTemplate(to, token, subject, html);
    }
}
