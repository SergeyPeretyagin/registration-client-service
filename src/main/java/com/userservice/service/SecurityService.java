package com.userservice.service;

import com.userservice.domain.dto.UserVerificationDto;

public interface SecurityService {
    String getPhoneByPassport(String passportNumber);

    Boolean sendCodeToEmail(String email);

    String sendCodeToPhone(String phone);

    String verify(UserVerificationDto userVerificationDto);

    boolean blockingVerification(String phoneNumber);
}
