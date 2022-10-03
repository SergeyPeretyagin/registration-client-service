package com.userservice.service.impl;

import com.userservice.service.VerificationCodeService;
import com.userservice.service.VerificationSenderService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.concurrent.CompletableFuture;

@Service
@RequiredArgsConstructor
@Log4j2
public class VerificationSenderServiceImpl implements VerificationSenderService {
    private final JavaMailSender javaMailSender;
    private final VerificationCodeService verificationCodeService;

    @Value("${spring.mail.username}")
    private String from;


    @Override
    @Async
    public CompletableFuture<Boolean> sendCodeToEmail(String email) {

        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(email);
            message.setSubject("Verification Code");
            message.setText(verificationCodeService.generateVerificationCode(email, "email"));
            message.setFrom(from);
            log.info("VerificationSenderServiceImpl class Method sendCodeToEmail "+message.toString());
            javaMailSender.send(message);
            return CompletableFuture.completedFuture(true);
        } catch (Exception e) {
            log.error("Exception from VerificationSenderServiceImpl class Method sendCodeToEmail "+e);
            throw new RuntimeException(e);
        }
    }

    @Override
    public String sendCodeToPhone(String phone) {
        return verificationCodeService.generateVerificationCode(phone, "phone");
    }
}
