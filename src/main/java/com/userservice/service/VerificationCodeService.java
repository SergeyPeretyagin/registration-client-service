package com.userservice.service;

import com.userservice.domain.dto.UserVerificationDto;
import com.userservice.domain.entity.Verification;

import java.sql.Timestamp;

public interface VerificationCodeService {
    String generateVerificationCode(String receiver, String type);

    void checkingCodeToExpiredAndValid(Verification verification, UserVerificationDto userVerificationDto);

    boolean isDateExistsAndAfter(Timestamp blockExpiration);
    public boolean isDateExist(Timestamp blockExpiration);
    void deleteExpiredCode();
    boolean isValidCode(String verificationCode, String userVerificationCode);
    public boolean isCodeExpired(Timestamp timestamp);
}
