package com.EGEA1R.CarService.service.interfaces;

import javax.mail.MessagingException;
import java.util.Map;

public interface EmailService {
    void sendMessageWithAttachment(String to, String subject, String text, String pathToAttachment) throws MessagingException;

    void sendResetPasswordToken(String recipientAddress, String token);

    void resendVerificationToken(String recipientAddress, String token);

    void sendVerificationToken(String recipientAddress, String token);

    void sendMessageUsingThymeleafTemplate(String to, String subject, Map<String, Object> templateModel) throws MessagingException;
}
