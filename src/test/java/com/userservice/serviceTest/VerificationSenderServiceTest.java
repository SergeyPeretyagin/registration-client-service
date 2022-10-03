package com.userservice.serviceTest;

import com.userservice.service.VerificationCodeService;
import com.userservice.service.impl.VerificationSenderServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mail.javamail.JavaMailSender;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class VerificationSenderServiceTest {

    @InjectMocks
    private VerificationSenderServiceImpl verificationSenderService;
    @Mock
    private JavaMailSender javaMailSender;
    @Mock
    private VerificationCodeService verificationCodeService;

    @Test
    void testSendCodeToEmail() throws ExecutionException, InterruptedException {
        String email = "email@gmail.com";
        when(verificationCodeService.generateVerificationCode(email, "email")).thenReturn("123456");
        CompletableFuture<Boolean> expected = verificationSenderService.sendCodeToEmail(email);
        assertTrue(expected.get());
    }
    @Test
    void testSendCodeToPhone(){
        String phoneNumber = "123456789012";
        when(verificationCodeService.generateVerificationCode(phoneNumber, "phone")).thenReturn("123456");
        String expected = verificationSenderService.sendCodeToPhone(phoneNumber);
        assertEquals("123456",expected);
    }

}
