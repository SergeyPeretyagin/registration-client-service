package com.userservice.service.impl;

import com.userservice.domain.dto.UserVerificationDto;
import com.userservice.domain.entity.Verification;
import com.userservice.domain.exception.BadRequestException;
import com.userservice.domain.exception.CountAttemptsException;
import com.userservice.domain.exception.VerificationAcceptException;
import com.userservice.service.*;
import com.userservice.utilies.ValidationUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import java.sql.Timestamp;
import java.time.Duration;
import java.time.Instant;
import java.time.temporal.ChronoUnit;


@Service
@RequiredArgsConstructor
@Log4j2
public class SecurityServiceImpl implements SecurityService {
    private final ClientService clientService;
    private final VerificationSenderService verificationSenderService;
    private final VerificationService verificationService;

    private final VerificationCodeService verificationCodeService;

    private final static int COUNT_ATTEMPTS_BLOCK = 3;

    @Override
    public String getPhoneByPassport(String passportNumber) {
        return clientService.findByPassportDataPassportNumber(passportNumber)
                .orElseThrow(() -> {
                    log.error("BadRequestException from SecurityServiceImpl class, method getPhoneByPassport."+
                            " Incorrect number: "+passportNumber);
                    return new BadRequestException("Incorrect number");
                }).getMobilePhone();
    }

    @Override
    public Boolean sendCodeToEmail(String email) {
        return verificationSenderService.sendCodeToEmail(email).isDone();
    }

    @Override
    public String sendCodeToPhone(String phone) {
        if(!ValidationUtil.checkMobileNumber(phone))
            throw new BadRequestException("Invalid data");
        verificationService.findByReceiver(phone)
                .ifPresent(verification -> {
                    if (verificationCodeService.isDateExistsAndAfter(verification.getBlockExpiration()))
                        throwVerificationAcceptException(verification.getBlockExpiration(),true);
                    if (!verification.getVerificationCode().isEmpty() &&
                            verificationCodeService.isCodeExpired(verification.getCodeExpiration()))
                        throwVerificationAcceptException(verification.getCodeExpiration(),false);
                    if (verificationCodeService.isDateExist(verification.getBlockExpiration()))
                        verificationService.deleteVerification(verification.getReceiver());
                });
        return verificationSenderService.sendCodeToPhone(phone);
    }

    @Override
    public String verify(UserVerificationDto userVerificationDto) {
        return verificationService.findByReceiver(userVerificationDto.getReceiver())
                .map(verification -> {
                    verificationCodeService.checkingCodeToExpiredAndValid(verification,userVerificationDto);
                    if (verification.getVerificationCode().equals(userVerificationDto.getVerificationCode())) {
                        return "Operation completed successfully";
                    } else {
                        int countRequest = verification.getCountRequest();
                        if (countRequest+1==COUNT_ATTEMPTS_BLOCK){
                            verification.setBlockExpiration(Timestamp.from(Instant.now().plus(10, ChronoUnit.MINUTES)));
                            verification.setCountRequest(0);
                            verificationService.save(verification);
                            throwVerificationAcceptException(verification.getBlockExpiration(),true);
                        }
                        verification.setCountRequest(countRequest + 1);
                        verificationService.save(verification);
                        log.error("UsernameNotFoundException from SecurityServiceImpl class"+
                                ", method  verify. Verification code is invalid: {}" , userVerificationDto.getVerificationCode());

                        throw new CountAttemptsException(
                                """
                                        {
                                        "message": "Verification code is invalid",
                                        "countAttempts": "countRequest"
                                        }
                                        """.replace("countRequest", String.valueOf(2-countRequest))
                        );
                    }
                }).orElseThrow(()->{
                    log.error("UsernameNotFoundException from SecurityServiceImpl class"+
                            ", method  verify. Phone number is invalid: {}" , userVerificationDto.getReceiver());
                    return new BadRequestException("Invalid data");
                });
    }

    @Override
    public boolean blockingVerification(String phoneNumber) {
        Verification verification = verificationService.findByReceiver(phoneNumber)
                .orElseThrow(() -> {
                    log.error("UsernameNotFoundException from SecurityServiceImpl class"+
                            ", method  verify. Verification code is invalid: {}" , phoneNumber);
                    return new BadRequestException("Invalid data");
                });
        verification.setBlockExpiration(Timestamp.from(Instant.now().plus(10, ChronoUnit.MINUTES)));
        verificationService.save(verification);
        return true;
    }

    private void throwVerificationAcceptException(Timestamp timestamp,boolean isBlocked){
        throw new VerificationAcceptException(String.valueOf(Duration.between(Instant.now(),
                timestamp.toInstant()).getSeconds())+isBlocked);
    }
}
