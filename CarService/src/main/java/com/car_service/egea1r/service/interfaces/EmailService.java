package com.car_service.egea1r.service.interfaces;

import com.car_service.egea1r.web.data.DTO.UnauthorizedUserReservationDTO;

import javax.mail.MessagingException;
import java.io.UnsupportedEncodingException;

public interface EmailService {

    void sendResetPasswordToken(String recipientAddress, String token) throws MessagingException, UnsupportedEncodingException;

    void resendVerificationToken(String recipientAddress, String token) throws MessagingException, UnsupportedEncodingException;

    void sendVerificationToken(String recipientAddress, String token) throws MessagingException, UnsupportedEncodingException;

    void sendSimpleMessage(String to, String subject, String text);

    void sendReservedServiceInformation(UnauthorizedUserReservationDTO unauthorizedUserReservationDTO) throws MessagingException, UnsupportedEncodingException;
}
