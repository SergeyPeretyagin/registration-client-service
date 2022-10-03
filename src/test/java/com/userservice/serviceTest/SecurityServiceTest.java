package com.userservice.serviceTest;

import com.userservice.domain.dto.UserVerificationDto;
import com.userservice.domain.entity.Client;
import com.userservice.domain.entity.Verification;
import com.userservice.domain.exception.BadRequestException;
import com.userservice.domain.exception.CountAttemptsException;
import com.userservice.domain.exception.VerificationAcceptException;
import com.userservice.mapper.TestCreator;
import com.userservice.service.ClientService;
import com.userservice.service.VerificationCodeService;
import com.userservice.service.VerificationSenderService;
import com.userservice.service.VerificationService;
import com.userservice.service.impl.SecurityServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Optional;
import java.util.concurrent.CompletableFuture;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class SecurityServiceTest {

    @InjectMocks
    private SecurityServiceImpl securityService;
    @Mock
    private ClientService clientService;
    @Mock
    private VerificationSenderService verificationSenderService;
    @Mock
    private VerificationService verificationService;
    @Mock
    private VerificationCodeService verificationCodeService;
    @Mock
    private Client client;
    private String passportNumber;
    private String email;
    private String verificationCode;
    private String mobilePhone;
    private UserVerificationDto userVerificationDto;
    private Verification verificationSuccess;
    private Verification verificationExpired;
    private Verification verificationWithNotValidCode;

    @BeforeEach
    void setUp(){
        passportNumber = TestCreator.getPassport();
        email = TestCreator.getEmail();
        verificationCode = TestCreator.getverificationCode();
        mobilePhone = TestCreator.getMobilePhone();
        userVerificationDto = TestCreator.getUserVerificationDto();
        verificationSuccess = TestCreator.getVerification();
        verificationExpired = TestCreator.getVerificationWithTimeExpired();
        verificationWithNotValidCode = TestCreator.getVerificationWithNotValidVerificationCode();

    }
    @Test
    void testGetPhoneByPassportSuccess(){
        when(clientService.findByPassportDataPassportNumber(passportNumber)).thenReturn(Optional.of(client));
        String expected = securityService.getPhoneByPassport(passportNumber);
        assertEquals(client.getMobilePhone(),expected);
        verify(clientService,times(1)).findByPassportDataPassportNumber(passportNumber);
    }
    @Test
    void testGetPhoneByPassportNotSuccessIfClientNotExist(){
        when(clientService.findByPassportDataPassportNumber(passportNumber)).thenThrow(new BadRequestException("Incorrect number"));
        assertThrows(BadRequestException.class, ()->securityService.getPhoneByPassport(passportNumber));
        verify(clientService,times(1)).findByPassportDataPassportNumber(passportNumber);
    }

    @Test
    void testSendCodeToEmailSuccess(){
        when(verificationSenderService.sendCodeToEmail(email)).thenReturn(CompletableFuture.completedFuture(true));
        boolean expected = securityService.sendCodeToEmail(email);
        assertTrue(expected);
        verify(verificationSenderService,times(1)).sendCodeToEmail(email);
    }

    @Test
    void testSendCodeToPhoneSuccess(){
        when(verificationSenderService.sendCodeToPhone(mobilePhone)).thenReturn(verificationCode);
        String expected = securityService.sendCodeToPhone(mobilePhone);
        assertEquals(verificationCode,expected);
        verify(verificationSenderService,times(1)).sendCodeToPhone(mobilePhone);
    }
    @Test
    void testSendCodeToPhoneNotSuccessIfPhoneNumberNotValid(){
        when(verificationSenderService.sendCodeToPhone(mobilePhone)).thenThrow(new BadRequestException("Invalid data"));
        assertThrows(BadRequestException.class,()->securityService.sendCodeToPhone(mobilePhone));
        verify(verificationSenderService,times(1)).sendCodeToPhone(mobilePhone);
    }



    @Test
    void testUserVerificationSuccess(){
        when(verificationService.findByReceiver(userVerificationDto.getReceiver())).thenReturn(Optional.of(verificationSuccess));
        doNothing().when(verificationCodeService).checkingCodeToExpiredAndValid(verificationSuccess,userVerificationDto);
        String expected = securityService.verify(userVerificationDto);
        assertEquals("Operation completed successfully", expected);
        verify(verificationService, times(1)).findByReceiver(userVerificationDto.getReceiver());
        verify(verificationCodeService, times(1)).checkingCodeToExpiredAndValid(verificationSuccess,userVerificationDto);
    }

    @Test
    void testUserVerificationNotSuccessIfVerificationNotFound(){
        when(verificationService.findByReceiver(userVerificationDto.getReceiver())).thenThrow(new BadRequestException("Phone number is invalid"));
        assertThrows(BadRequestException.class, ()->securityService.verify(userVerificationDto));
        verify(verificationService, times(1)).findByReceiver(userVerificationDto.getReceiver());
    }

    @Test
    void testUserVerificationNotSuccessIfIfCodeVerificationIsExpired(){
        when(verificationService.findByReceiver(userVerificationDto.getReceiver())).thenReturn(Optional.of(verificationExpired));
        doThrow(new BadRequestException("Code is expired")).when(verificationCodeService).checkingCodeToExpiredAndValid(verificationExpired,userVerificationDto);
        assertThrows(BadRequestException.class, ()->securityService.verify(userVerificationDto));
        verify(verificationService, times(1)).findByReceiver(userVerificationDto.getReceiver());
        verify(verificationCodeService, times(1)).checkingCodeToExpiredAndValid(verificationExpired,userVerificationDto);
    }
    @Test
    void testUserVerificationNotSuccessWithNotValidVerificationCode(){
        when(verificationService.findByReceiver(userVerificationDto.getReceiver())).thenReturn(Optional.of(verificationWithNotValidCode));
        doNothing().when(verificationCodeService).checkingCodeToExpiredAndValid(verificationWithNotValidCode,userVerificationDto);
        assertThrows(CountAttemptsException.class, ()->securityService.verify(userVerificationDto));
        verify(verificationService, times(1)).findByReceiver(userVerificationDto.getReceiver());
        verify(verificationCodeService, times(1)).checkingCodeToExpiredAndValid(verificationWithNotValidCode,userVerificationDto);
    }
    @Test
    void testUserVerificationNotSuccessIfBlockExpirationAndUserBlocking(){
        when(verificationService.findByReceiver(userVerificationDto.getReceiver())).thenReturn(Optional.of(verificationSuccess));
        doThrow(new VerificationAcceptException("100")).when(verificationCodeService).checkingCodeToExpiredAndValid(verificationSuccess,userVerificationDto);
        assertThrows(VerificationAcceptException.class, ()->securityService.verify(userVerificationDto));
        verify(verificationService, times(1)).findByReceiver(userVerificationDto.getReceiver());
        verify(verificationCodeService, times(1)).checkingCodeToExpiredAndValid(verificationSuccess,userVerificationDto);
    }
    @Test
    void testBlockingVerificationSuccess(){
        when(verificationService.findByReceiver(mobilePhone)).thenReturn(Optional.of(verificationSuccess));
        boolean expected = securityService.blockingVerification(mobilePhone);
        assertTrue(expected);
        verify(verificationService,times(1)).findByReceiver(mobilePhone);
    }
    @Test
    void testBlockingVerificationNotSuccessIfVerificationNotExist(){
        when(verificationService.findByReceiver(mobilePhone)).thenThrow(new UsernameNotFoundException("Verification code is invalid"));
        assertThrows(UsernameNotFoundException.class, ()->securityService.blockingVerification(mobilePhone));
        verify(verificationService,times(1)).findByReceiver(mobilePhone);
    }


}
