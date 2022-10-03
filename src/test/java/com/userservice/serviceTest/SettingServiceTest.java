package com.userservice.serviceTest;

import com.userservice.domain.dto.UpdateEmailDto;
import com.userservice.domain.dto.UpdatePasswordDto;
import com.userservice.domain.entity.Client;
import com.userservice.domain.entity.UserProfile;
import com.userservice.domain.exception.BadRequestException;
import com.userservice.domain.exception.EmailExistsException;
import com.userservice.domain.exception.InternalServerException;
import com.userservice.domain.exception.WrongPasswordException;
import com.userservice.service.ClientService;
import com.userservice.service.UserProfileService;
import com.userservice.service.impl.SettingServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class SettingServiceTest {
    @InjectMocks
    private SettingServiceImpl settingService;
    @Mock
    private UserProfileService userProfileService;
    @Mock
    private PasswordEncoder passwordEncoder;
    @Mock
    private UserProfile userProfile;
    @Mock
    private ClientService clientService;
    private UUID userId;
    @BeforeEach
    void setUp(){
        userId = UUID.randomUUID();
    }
    @Test
    void testGetNotificationSettingsSuccess(){
        when(userProfileService.findByClientId(userId)).thenReturn(Optional.of(userProfile));
        UserProfile expected = settingService.getNotificationSettings(userId);
        assertEquals(userProfile,expected);
        verify(userProfileService,times(1)).findByClientId(userId);

    }

    @Test
    void testGetNotificationSettingsNotSuccessIfUserProfileNotFound(){
        when(userProfileService.findByClientId(userId)).thenThrow(new InternalServerException("User profile not found"));
        assertThrows(InternalServerException.class,()->settingService.getNotificationSettings(userId));
        verify(userProfileService,times(1)).findByClientId(userId);

    }
    @Test
    void testChangeSmsNotificationSuccess(){
        when(userProfileService.updateSmsNotification(true, userId)).thenReturn(1);
        String expected = settingService.changeSmsNotification(userId,true);
        assertEquals("SMS-notification has been successfully updated",expected);
        verify(userProfileService,times(1)).updateSmsNotification(true, userId);
    }
    @Test
    void testChangePushNotificationSuccess(){
        when(userProfileService.updatePushNotification(true, userId)).thenReturn(1);
        String expected = settingService.changePushNotification(userId,true);
        assertEquals("PUSH-notification has been successfully updated",expected);
        verify(userProfileService,times(1)).updatePushNotification(true,userId);
    }
    @Test
    void testChangeEmailSubscriptionSuccess(){
        when(userProfileService.findByClientId(userId)).thenReturn(Optional.of(userProfile));
        String expected = settingService.changeEmailSubscription(userId,true);
        assertEquals("Operation completed successfully",expected);
        verify(userProfileService,times(1)).findByClientId(userId);
    }
    @Test
    void testChangeEmailSubscriptionNotSuccessIfUserProfileNotExist(){
        when(userProfileService.findByClientId(userId)).thenThrow(new UsernameNotFoundException("UNAUTHORIZED"));
        assertThrows(UsernameNotFoundException.class,()->settingService.changeEmailSubscription(userId,true));
        verify(userProfileService,times(1)).findByClientId(userId);
    }

    @ParameterizedTest(name = "#{index} - Run test with validUpdatePasswordDto = {0}")
    @MethodSource("com.userservice.mapper.TestCreator#getStreamFromValidUpdatePasswordDto")
    void testChangePasswordSuccess(UpdatePasswordDto updatePasswordDto){
        when(userProfileService.findByClientId(userId)).thenReturn(Optional.of(userProfile));
        when(passwordEncoder.matches(updatePasswordDto.getOldPassword(), userProfile.getPassword())).thenReturn(true);
        String expected = settingService.changePassword(userId,updatePasswordDto.getOldPassword(),
                updatePasswordDto.getNewPassword(),updatePasswordDto.getConfirmNewPassword());
        assertEquals("Operation completed successfully", expected);
        verify(userProfileService,times(1)).findByClientId(userId);
        verify(passwordEncoder,times(1)).matches(updatePasswordDto.getOldPassword(), userProfile.getPassword());
    }

    @ParameterizedTest(name = "#{index} - Run test with validUpdatePasswordDto = {0}")
    @MethodSource("com.userservice.mapper.TestCreator#getStreamFromUpdatePasswordDtoWithNotEqualsNewAndConfirmPassword")
    void testChangePasswordNotSuccessWIthNotEqualsNewAndConfirmPassword(UpdatePasswordDto updatePasswordDto){
        assertThrows(WrongPasswordException.class,()-> settingService.changePassword(userId,updatePasswordDto.getOldPassword(),
                updatePasswordDto.getNewPassword(),updatePasswordDto.getConfirmNewPassword()));

    }

    @ParameterizedTest(name = "#{index} - Run test with validUpdatePasswordDto = {0}")
    @MethodSource("com.userservice.mapper.TestCreator#getStreamFromValidUpdatePasswordDto")
    void testChangePasswordNotSuccessWithNotEqualsOldPasswordAndPasswordInDB(UpdatePasswordDto updatePasswordDto){
        when(userProfileService.findByClientId(userId)).thenReturn(Optional.of(userProfile));
        when(passwordEncoder.matches(updatePasswordDto.getOldPassword(), userProfile.getPassword())).thenReturn(false);
        assertThrows(BadRequestException.class,()->settingService.changePassword(userId,updatePasswordDto.getOldPassword(),
                        updatePasswordDto.getNewPassword(),updatePasswordDto.getConfirmNewPassword()));
        verify(userProfileService,times(1)).findByClientId(userId);
        verify(passwordEncoder,times(1)).matches(updatePasswordDto.getOldPassword(), userProfile.getPassword());
    }
    @ParameterizedTest(name = "#{index} - Run test with validUpdatePasswordDto = {0}")
    @MethodSource("com.userservice.mapper.TestCreator#getStreamFromValidUpdatePasswordDto")
    void testChangePasswordNotSuccessIfUserProfileNotExist(UpdatePasswordDto updatePasswordDto){
        when(userProfileService.findByClientId(userId)).thenThrow(new BadRequestException("Invalid data"));
        assertThrows(BadRequestException.class,()->settingService.changePassword(userId,updatePasswordDto.getOldPassword(),
                updatePasswordDto.getNewPassword(),updatePasswordDto.getConfirmNewPassword()));
        verify(userProfileService,times(1)).findByClientId(userId);
    }

    @ParameterizedTest(name = "#{index} - Run test with updateEmailDto = {0}")
    @MethodSource("com.userservice.mapper.TestCreator#getStreamFromUpdateEmailDto")
    void testChangeEmailSuccess(UpdateEmailDto updateEmailDto){
        when(userProfileService.existsByEmail(updateEmailDto.getNewEmail())).thenReturn(false);
        when(userProfileService.updateEmail(updateEmailDto.getNewEmail(), userId)).thenReturn(1);
        String expected = settingService.changeEmail(userId,updateEmailDto.getNewEmail());
        assertEquals("Email has been successfully updated",expected);
        verify(userProfileService,times(1)).existsByEmail(updateEmailDto.getNewEmail());
    }


    @ParameterizedTest(name = "#{index} - Run test with updateEmailDto = {0}")
    @MethodSource("com.userservice.mapper.TestCreator#getStreamFromUpdateEmailDto")
    void testChangeEmailNotSuccessIfNewEmailIsExist(UpdateEmailDto updateEmailDto){
        when(userProfileService.existsByEmail(updateEmailDto.getNewEmail())).thenReturn(true);
        assertThrows(EmailExistsException.class,()->settingService.changeEmail(userId,updateEmailDto.getNewEmail()));
        verify(userProfileService,times(1)).existsByEmail(updateEmailDto.getNewEmail());
    }

    @Test
    void testChangeQASuccess(){
        String answer = "answer";
        String question = "question";
        when(userProfileService.updateQA(question,answer,userId)).thenReturn(1);
        String expected = settingService.changeQA(userId,question,answer);
        assertEquals("Operation completed successfully",expected);
        verify(userProfileService,times(1)).updateQA(question,answer,userId);
    }

    @Test
    void testGetClientInformationSuccess(){
        Client client = mock(Client.class);
        when(clientService.findById(userId)).thenReturn(Optional.of(client));
        Client expected = settingService.getClientInformation(userId);
        assertEquals(client,expected);
        verify(clientService,times(1)).findById(userId);
    }

    @Test
    void testGetClientInformationSuccessIfClientNotExist(){
        when(clientService.findById(userId)).thenThrow(new BadRequestException("Invalid data"));
        assertThrows(BadRequestException.class,()->settingService.getClientInformation(userId));
        verify(clientService,times(1)).findById(userId);
    }

}
