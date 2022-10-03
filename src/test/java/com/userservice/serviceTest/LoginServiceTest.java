package com.userservice.serviceTest;

import com.userservice.domain.dto.RequestNonClientDto;
import com.userservice.domain.dto.UpdatePasswordRequestDto;
import com.userservice.domain.entity.Client;
import com.userservice.domain.entity.UserProfile;
import com.userservice.domain.exception.BadRequestException;
import com.userservice.domain.exception.WrongPasswordException;
import com.userservice.mapper.TestCreator;
import com.userservice.service.ClientService;
import com.userservice.service.UserProfileService;
import com.userservice.service.impl.LoginServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;
import java.util.Optional;
import java.util.concurrent.ThreadLocalRandom;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class LoginServiceTest {

    @InjectMocks
    private LoginServiceImpl loginService;
    @Mock
    private ClientService clientService;
    @Mock
    private UserProfileService userProfileService;
    @Mock
    private PasswordEncoder passwordEncoder;

    private Client client;
    private UserProfile userProfile;


    @BeforeEach
    void setUp(){
        RequestNonClientDto requestNonClientDto = TestCreator.getRequestNonClientDto();
        client = TestCreator.getNewNotClient(requestNonClientDto);
        userProfile = TestCreator.getUserProfile(client, requestNonClientDto);
    }

    @ParameterizedTest(name = "#{index} - Run test with updatePasswordRequestDto = {0}")
    @MethodSource("com.userservice.mapper.CreatorOldAndNewPassword#validStreamValidNewAndConfirmPasswords")
    void testUpdatePasswordNotSuccessIfClientWithPassportNumberNotExist(UpdatePasswordRequestDto updatePasswordRequestDto){
        when(clientService.findByPassportDataPassportNumber(updatePasswordRequestDto.getPassportNumber())).thenThrow(new BadRequestException("Invalid data"));
        assertThrows(BadRequestException.class, ()->loginService.updatePassword(updatePasswordRequestDto.getPassportNumber(),
                updatePasswordRequestDto.getNewPassword(),updatePasswordRequestDto.getConfirmNewPassword()));
        verify(clientService,times(1)).findByPassportDataPassportNumber(updatePasswordRequestDto.getPassportNumber());
    }

    @ParameterizedTest(name = "#{index} - Run test with updatePasswordRequestDto = {0}")
    @MethodSource("com.userservice.mapper.CreatorOldAndNewPassword#streamNotEqualsNewAndConfirmPasswords")
    void testUpdatePasswordNotSuccessWithNotEqualsNewAndConfirmPassword(UpdatePasswordRequestDto updatePasswordRequestDto){
        assertThrows(WrongPasswordException.class, ()->loginService.updatePassword(updatePasswordRequestDto.getPassportNumber(),
                updatePasswordRequestDto.getNewPassword(),updatePasswordRequestDto.getConfirmNewPassword()));
    }

    @ParameterizedTest(name = "#{index} - Run test with updatePasswordRequestDto = {0}")
    @MethodSource("com.userservice.mapper.CreatorOldAndNewPassword#streamWithEqualsNewAndOldPasswords")
    void testUpdatePasswordNotSuccessWithEqualsOldPasswordInDB(UpdatePasswordRequestDto updatePasswordRequestDto, UserProfile userProfile){
        when(clientService.findByPassportDataPassportNumber(updatePasswordRequestDto.getPassportNumber())).thenReturn(Optional.of(client));
        when(userProfileService.findByClientId(client.getId())).thenReturn(Optional.of(userProfile));
        when(passwordEncoder.matches(updatePasswordRequestDto.getNewPassword(),userProfile.getPassword()))
                .thenThrow(new BadRequestException("Old and new password matches"));
        assertThrows(BadRequestException.class, ()->loginService.updatePassword(updatePasswordRequestDto.getPassportNumber(),
                updatePasswordRequestDto.getNewPassword(),updatePasswordRequestDto.getConfirmNewPassword()));
        verify(clientService,times(1)).findByPassportDataPassportNumber(updatePasswordRequestDto.getPassportNumber());
        verify(userProfileService,times(1)).findByClientId(client.getId());
        verify(passwordEncoder,times(1)).matches(updatePasswordRequestDto.getNewPassword(),userProfile.getPassword());
    }

    @ParameterizedTest(name = "#{index} - Run test with updatePasswordRequestDto = {0}")
    @MethodSource("com.userservice.mapper.CreatorOldAndNewPassword#validStreamValidNewAndConfirmPasswords")
    void testUpdatePasswordNotSuccessIfUserProfileWithPassportNumberNotExist(UpdatePasswordRequestDto updatePasswordRequestDto){
        when(clientService.findByPassportDataPassportNumber(updatePasswordRequestDto.getPassportNumber())).thenReturn(Optional.of(client));
        when(userProfileService.findByClientId(client.getId())).thenThrow(new BadRequestException("Invalid data"));
        assertThrows(BadRequestException.class, ()->loginService.updatePassword(updatePasswordRequestDto.getPassportNumber(),
                updatePasswordRequestDto.getNewPassword(),updatePasswordRequestDto.getConfirmNewPassword()));
        verify(clientService,times(1)).findByPassportDataPassportNumber(updatePasswordRequestDto.getPassportNumber());
        verify(userProfileService,times(1)).findByClientId(client.getId());
    }

    @ParameterizedTest(name = "#{index} - Run test with updatePasswordRequestDto = {0}")
    @MethodSource("com.userservice.mapper.CreatorOldAndNewPassword#validStreamValidNewAndConfirmPasswords")
    void testUpdatePasswordSuccess(UpdatePasswordRequestDto updatePasswordRequestDto) {
        when(clientService.findByPassportDataPassportNumber(updatePasswordRequestDto.getPassportNumber())).thenReturn(Optional.of(client));
        when(userProfileService.findByClientId(client.getId())).thenReturn(Optional.of(userProfile));
        String expectedMessage = "Operation completed successfully";
        assertEquals(expectedMessage, loginService.updatePassword(updatePasswordRequestDto.getPassportNumber(),
                updatePasswordRequestDto.getNewPassword(),updatePasswordRequestDto.getConfirmNewPassword()));
    }
}
