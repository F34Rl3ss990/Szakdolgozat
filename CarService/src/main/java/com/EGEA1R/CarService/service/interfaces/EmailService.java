package com.EGEA1R.CarService.service.interfaces;

import com.EGEA1R.CarService.web.DTO.UnauthorizedUserReservationDTO;

import javax.mail.MessagingException;
import java.io.UnsupportedEncodingException;

public interface EmailService {
   // void sendMessageWithAttachment(String to, String subject, String text, String pathToAttachment) throws MessagingException;

    void sendResetPasswordToken(String recipientAddress, String token) throws MessagingException, UnsupportedEncodingException;

    void resendVerificationToken(String recipientAddress, String token) throws MessagingException, UnsupportedEncodingException;

    void sendVerificationToken(String recipientAddress, String token) throws MessagingException, UnsupportedEncodingException;

    void sendSimpleMessage(String to, String subject, String text);

    void sendReservedServiceInformation(UnauthorizedUserReservationDTO unauthorizedUserReservationDTO)  throws MessagingException;
}
