package com.userservice.serviceTest;

import com.userservice.domain.entity.UserProfile;
import com.userservice.repository.UserProfileRepository;
import com.userservice.service.impl.UserProfileServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserProfileServiceTest {
    @InjectMocks
    private UserProfileServiceImpl userProfileService;
    @Mock
    private UserProfileRepository userProfileRepository;
    @Mock
    private UserProfile userProfile;
    private UUID userId;
    @BeforeEach
    void setUp(){
        userId = UUID.randomUUID();
    }

    @Test
    void testExistsByEmail(){
        when(userProfileRepository.existsByEmail(anyString())).thenReturn(true);
        boolean expected = userProfileService.existsByEmail(anyString());
        assertEquals(true, expected);
        verify(userProfileRepository,times(1)).existsByEmail(anyString());
    }

    @Test
    void testFindByClientId(){
        when(userProfileRepository.findByClient_Id(userId)).thenReturn(Optional.of(userProfile));
        UserProfile expected = userProfileService.findByClientId(userId).get();
        assertEquals(userProfile,expected);
        verify(userProfileRepository,times(1)).findByClient_Id(userId);
    }
    @Test
    void testSave(){
        when(userProfileRepository.save(userProfile)).thenReturn(userProfile);
        UserProfile expected = userProfileService.save(userProfile);
        assertEquals(userProfile,expected);
        verify(userProfileRepository,times(1)).save(userProfile);
    }
    @Test
    void testUpdateSmsNotification(){
        when(userProfileRepository.updateSmsNotification(true, userId)).thenReturn(1);
        int expected = userProfileService.updateSmsNotification(true,userId);
        assertEquals(1,expected);
        verify(userProfileRepository,times(1)).updateSmsNotification(true,userId);
    }
    @Test
    void testUpdatePushNotification(){
        when(userProfileRepository.updatePushNotification(true,userId)).thenReturn(1);
        int expected = userProfileService.updatePushNotification(true,userId);
        assertEquals(1,expected);
        verify(userProfileRepository,times(1)).updatePushNotification(true,userId);
    }
    @Test
    void testUpdateEmailSubscription(){
        when(userProfileRepository.updateEmailSubscription(true,userId)).thenReturn(1);
        int expected = userProfileService.updateEmailSubscription(true,userId);
        assertEquals(1,expected);
        verify(userProfileRepository,times(1)).updateEmailSubscription(true,userId);
    }
    @Test
    void testUpdatePassword(){
        String password ="123ASDasd";
        when(userProfileRepository.updatePassword(password,userId)).thenReturn(1);
        int expected = userProfileService.updatePassword(password,userId);
        assertEquals(1,expected);
        verify(userProfileRepository,times(1)).updatePassword(password,userId);
    }
    @Test
    void testUpdateEmail(){
        String email = "123@gamil.com";
        when(userProfileRepository.updateEmail(email,userId)).thenReturn(1);
        int expected = userProfileService.updateEmail(email,userId);
        assertEquals(1,expected);
        verify(userProfileRepository,times(1)).updateEmail(email,userId);
    }
    @Test
    void testUpdateQA(){
        String question = "question";
        String answer = "answer";
        when(userProfileRepository.updateQA(question, answer, userId)).thenReturn(1);
        int expected = userProfileService.updateQA(question, answer, userId);
        assertEquals(1,expected);
        verify(userProfileRepository,times(1)).updateQA(question, answer, userId);
    }

}
