package com.userservice.serviceTest;

import com.userservice.domain.dto.UserVerificationDto;
import com.userservice.domain.entity.Verification;
import com.userservice.domain.exception.BadRequestException;
import com.userservice.domain.exception.VerificationAcceptException;
import com.userservice.mapper.TestCreator;
import com.userservice.repository.VerificationRepository;
import com.userservice.service.VerificationCodeService;
import com.userservice.service.VerificationService;
import com.userservice.service.impl.VerificationCodeServiceImpl;
import com.userservice.service.impl.VerificationServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.sql.Timestamp;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@SpringBootTest(classes = VerificationCodeServiceTest.Config.class)
public class VerificationCodeServiceTest {
    @Configuration
    static class Config{
        @Bean
        public VerificationService verificationService(){
            return new VerificationServiceImpl(mock(VerificationRepository.class));
        }
        @Bean
        public VerificationCodeService verificationCodeService(){
            return new VerificationCodeServiceImpl(this.verificationService());
        }
    }
    @Autowired
    private VerificationCodeServiceImpl verificationCodeService;
    @Autowired
    private VerificationServiceImpl verificationService;
    @Mock
    private Verification verification;
    private Verification blockingVerification;
    private UserVerificationDto userVerificationDto;
    @BeforeEach
    void setUp(){
        blockingVerification = TestCreator.getVerificationWithTimeExpired();
        userVerificationDto = TestCreator.getUserVerificationDto();
    }

    @Test
    void testGenerateVerificationCode(){
        String phoneMobile = "123456678990";
        when(verificationService.findByReceiver(phoneMobile)).thenReturn(Optional.of(verification));
        String expectedCode = verificationCodeService.generateVerificationCode(phoneMobile,"phone");
        assertTrue(expectedCode.matches("\\d{6}"));
    }
    @Test
    void testCheckingCodeToExpiredAndValidIfClientIsBlocking(){
        assertThrows(VerificationAcceptException.class,
                ()->verificationCodeService.checkingCodeToExpiredAndValid(blockingVerification,userVerificationDto));
    }

    @Test
    void testCheckingCodeToExpiredAndValidIfCodeExpired(){
        Verification actualVerification = Verification.builder()
                .receiver("12345678901")
                .verificationCode("123456")
                .codeExpiration(Timestamp.valueOf(
                        LocalDateTime.now().minus(Duration.of(2, ChronoUnit.MINUTES))))
                .blockExpiration(null)
                .type("phone")
                .countRequest(0)
                .build();
        assertThrows(BadRequestException.class,
                ()->verificationCodeService.checkingCodeToExpiredAndValid(actualVerification,userVerificationDto));
    }

    @Test
    void testCheckingCodeToExpiredAndValidIfCodeExpiredButValidAndClientBlocking(){
        Verification actualVerification = Verification.builder()
                .receiver("123456789012")
                .verificationCode("123456")
                .codeExpiration(Timestamp.valueOf(
                        LocalDateTime.now().minus(Duration.of(2, ChronoUnit.MINUTES))))
                .blockExpiration(Timestamp.valueOf(
                        LocalDateTime.now().minus(Duration.of(10, ChronoUnit.MINUTES))))
                .type("phone")
                .countRequest(0)
                .build();
        assertThrows(BadRequestException.class,
                ()->verificationCodeService.checkingCodeToExpiredAndValid(actualVerification,userVerificationDto));
    }


    @Test
    void testIsDateExistsAndAfterFalse(){
        Verification actualVerification = Verification.builder()
                .receiver("123456789012")
                .verificationCode("123457")
                .codeExpiration(Timestamp.valueOf(
                        LocalDateTime.now().plus(Duration.of(2, ChronoUnit.MINUTES))))
                .blockExpiration(Timestamp.valueOf(
                        LocalDateTime.now().minus(Duration.of(10, ChronoUnit.MINUTES))))
                .type("phone")
                .countRequest(0)
                .build();
        assertFalse(verificationCodeService.isDateExistsAndAfter(actualVerification.getBlockExpiration()));
    }

    @Test
    void testIsDateExistsAndAfterTrue(){
        Verification actualVerification = Verification.builder()
                .receiver("123456789012")
                .verificationCode("123457")
                .codeExpiration(Timestamp.valueOf(
                        LocalDateTime.now().plus(Duration.of(2, ChronoUnit.MINUTES))))
                .blockExpiration(Timestamp.valueOf(
                        LocalDateTime.now().plus(Duration.of(10, ChronoUnit.MINUTES))))
                .type("phone")
                .countRequest(0)
                .build();
        assertTrue(verificationCodeService.isDateExistsAndAfter(actualVerification.getBlockExpiration()));
    }

}
