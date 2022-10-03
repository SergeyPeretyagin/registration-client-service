package com.userservice.serviceTest;

import com.userservice.domain.entity.Verification;
import com.userservice.repository.VerificationRepository;
import com.userservice.service.impl.VerificationServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class VerificationServiceTest {
    @InjectMocks
    private VerificationServiceImpl verificationService;
    @Mock
    private VerificationRepository verificationRepository;
    @Mock
    private Verification verification;

    @Test
    void testSaveSuccess(){
        when(verificationRepository.save(verification)).thenReturn(verification);
        Verification expected = verificationService.save(verification);
        assertEquals(verification,expected);
        verify(verificationRepository,times(1)).save(verification);
    }
    @Test
    void testFindByReceiver(){
        String mobilePhone = "1234566789012";
        when(verificationRepository.findByReceiverIgnoreCase(mobilePhone)).thenReturn(Optional.of(verification));
        Verification expected = verificationService.findByReceiver(mobilePhone).get();
        assertEquals(verification,expected);
    }
}
