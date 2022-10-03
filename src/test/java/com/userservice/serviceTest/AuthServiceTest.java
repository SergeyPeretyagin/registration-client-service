package com.userservice.serviceTest;

import com.userservice.domain.entity.Client;
import com.userservice.domain.entity.UserProfile;
import com.userservice.domain.exception.BadRequestException;
import com.userservice.mapper.TestCreator;
import com.userservice.repository.UserProfileRepository;
import com.userservice.service.ClientService;
import com.userservice.service.impl.AuthServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AuthServiceTest {
    @InjectMocks
    private AuthServiceImpl authService;
    @Mock
    private ClientService clientService;
    @Mock
    private UserProfileRepository userProfileRepository;
    @Mock
    private Client client;
    @Mock
    private UserProfile userProfile;
    private String mobilePhone;
    private String passport;


    @BeforeEach
    void setUp(){
        mobilePhone = TestCreator.getMobilePhone();
        passport = TestCreator.getPassport();

    }


    @Test
    void testGetUserProfileByPhoneNumberSuccess(){
        when(clientService.findByPassportDataPassportNumberOrMobilePhone(mobilePhone)).thenReturn(Optional.of(client));
        when(userProfileRepository.findByClient_Id(client.getId())).thenReturn(Optional.of(userProfile));
        UserProfile expectedUserProfile = authService.getUserProfileByPhoneNumberOrPassport(mobilePhone);

        assertEquals(userProfile,expectedUserProfile);
        verify(clientService,times(1)).findByPassportDataPassportNumberOrMobilePhone(mobilePhone);
        verify(userProfileRepository,times(1)).findByClient_Id(client.getId());
    }

    @Test
    void testGetUserProfileByPhoneNumberNotSuccessIfUserProfileNotExist(){
        when(clientService.findByPassportDataPassportNumberOrMobilePhone(mobilePhone)).thenReturn(Optional.of(client));
        when(userProfileRepository.findByClient_Id(client.getId())).thenThrow(new BadRequestException("Invalid data"));
        assertThrows(BadRequestException.class,()->authService.getUserProfileByPhoneNumberOrPassport(mobilePhone));
        verify(clientService,times(1)).findByPassportDataPassportNumberOrMobilePhone(mobilePhone);
        verify(userProfileRepository,times(1)).findByClient_Id(client.getId());

    }

    @Test
    void testGetUserProfileByPhoneNumberNotSuccessIfClientNotExist(){
        when(clientService.findByPassportDataPassportNumberOrMobilePhone(mobilePhone)).thenThrow(new BadRequestException("Invalid data"));
        assertThrows(BadRequestException.class,()->authService.getUserProfileByPhoneNumberOrPassport(mobilePhone));
        verify(clientService,times(1)).findByPassportDataPassportNumberOrMobilePhone(mobilePhone);
    }

    @Test
    void testGetUserProfileByPassportSuccess(){
        when(clientService.findByPassportDataPassportNumberOrMobilePhone(passport)).thenReturn(Optional.of(client));
        when(userProfileRepository.findByClient_Id(client.getId())).thenReturn(Optional.of(userProfile));
        UserProfile expectedUserProfile = authService.getUserProfileByPhoneNumberOrPassport(passport);

        assertEquals(userProfile,expectedUserProfile);
        verify(clientService,times(1)).findByPassportDataPassportNumberOrMobilePhone(passport);
        verify(userProfileRepository,times(1)).findByClient_Id(client.getId());
    }

    @Test
    void testGetUserProfileByPassportNotSuccessIfUserProfileNotExist(){
        when(clientService.findByPassportDataPassportNumberOrMobilePhone(passport)).thenReturn(Optional.of(client));
        when(userProfileRepository.findByClient_Id(client.getId())).thenThrow(new BadRequestException("Invalid data"));
        assertThrows(BadRequestException.class,()->authService.getUserProfileByPhoneNumberOrPassport(passport));
        verify(clientService,times(1)).findByPassportDataPassportNumberOrMobilePhone(passport);
        verify(userProfileRepository,times(1)).findByClient_Id(client.getId());

    }

    @Test
    void testGetUserProfileByPassportNotSuccessIfClientNotExist(){
        when(clientService.findByPassportDataPassportNumberOrMobilePhone(passport)).thenThrow(new BadRequestException("Invalid data"));
        assertThrows(BadRequestException.class,()->authService.getUserProfileByPhoneNumberOrPassport(passport));
        verify(clientService,times(1)).findByPassportDataPassportNumberOrMobilePhone(passport);
    }

}
