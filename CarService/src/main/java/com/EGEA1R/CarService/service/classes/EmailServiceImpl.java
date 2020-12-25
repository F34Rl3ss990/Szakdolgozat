package com.EGEA1R.CarService.service.classes;

import com.EGEA1R.CarService.service.interfaces.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.File;
import java.util.UUID;

@Component
public class EmailServiceImpl implements EmailService {


    private JavaMailSender emailSender;

    @Autowired
    public void setJavaMailSender(JavaMailSender emailSender){
        this.emailSender = emailSender;
    }

    private void sendSimpleMessage(String to, String subject, String text) {
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

    @Override
    public void sendResetPasswordToken(String recipientAddress, String token){
        String subject = "Password reset token mail";
        String confirmationUrl =  "\r\n" + "http://localhost:8080/apu/auth/signup/resendRegistrationToken?token=" + token;
        String message = "A jelszó aktivációjához szükséges linket itt találod: ";
        String text = message + confirmationUrl;
        sendSimpleMessage(recipientAddress, subject, text);
    }

    @Override
    public void resendVerificationToken(String recipientAddress, String token){
        String subject = "Vericifation mail resent";
        String confirmationUrl =  "\r\n" + "http://localhost:8080/apu/auth/signup/resendRegistrationToken?token=" + token;
        String message = "A fiók aktivációjához szükséges linket itt találod: ";
        String text = message + confirmationUrl;
        sendSimpleMessage(recipientAddress, subject, text);
    }

    @Override
    public void sendVerificationToken(String recipientAddress, String token){
        String subject = "Verification email sent";
        String confirmationUrl
                = "http://localhost:8080/apu/auth/signup/regitrationConfirm?token=" + token;
        String message = ("Sikeresen regisztráltál a carservice.hu weboldalra\n"+
                "A fiók aktiválásához szükséges linket itt találod:");
        String text = message + "\r\n" + "http://localhost:8080" + confirmationUrl;
        sendSimpleMessage(recipientAddress, subject, text);
    }

    @Override
    public void sendMessageWithAttachment(String to, String subject, String text, String pathToAttachment) throws MessagingException {
        try {
            MimeMessage message = emailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);
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
}