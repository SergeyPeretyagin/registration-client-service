package com.userservice.testControllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.userservice.controller.RegistrationController;
import com.userservice.controller.SettingsController;
import com.userservice.domain.dto.*;
import com.userservice.domain.entity.Client;
import com.userservice.domain.entity.UserProfile;
import com.userservice.domain.exception.BadRequestException;
import com.userservice.domain.exception.EmailExistsException;
import com.userservice.domain.exception.InternalServerException;
import com.userservice.domain.exception.WrongPasswordException;
import com.userservice.domain.mapper.Mapper;
import com.userservice.mapper.TestCreator;
import com.userservice.service.ClientService;
import com.userservice.service.SettingService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.http.MediaType;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(value = RegistrationController.class, useDefaultFilters = false,includeFilters = {
        @ComponentScan.Filter(
                type = FilterType.ASSIGNABLE_TYPE,
                value = SettingsController.class
        )})
@WebAppConfiguration
public class SettingsControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private SettingService settingService;
    @Autowired
    private WebApplicationContext webApplicationContext;
    @Autowired
    private ObjectMapper objectMapper;
    @MockBean
    private Mapper mapper;
    @Mock
    private Client client;

    private UUID clientId;
    private UserProfile mockUserProfile;

    private NotificationDto notificationDTO;
    private NotificationRequestDto notificationRequestDto;
    private UpdatePasswordDto updatePasswordDto;
    private ClientInformationDto clientInformationDto;


    @BeforeEach
    void setUp(){
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext)
                .build();
        clientId = UUID.randomUUID();
        mockUserProfile = mock(UserProfile.class);
        notificationDTO = TestCreator.getNotificationDto();
        notificationRequestDto = new NotificationRequestDto(true);
        updatePasswordDto = TestCreator.getUpdatePasswordDto();
        clientInformationDto = TestCreator.getClientInformationDto();
    }

    @Test
    public void testGetNotificationSettingsSuccess() throws Exception {
        when(settingService.getNotificationSettings(clientId)).thenReturn(mockUserProfile);
        when(mapper.userProfileToNotificationDto(mockUserProfile)).thenReturn(notificationDTO);

        MockHttpServletRequestBuilder requestBuilder = get("/auth/user/settings/notifications/all")
                .contentType(MediaType.APPLICATION_JSON)
                .characterEncoding("UTF-8")
                .param("clientId", String.valueOf(clientId));
        mockMvc.perform(requestBuilder)
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().bytes(objectMapper.writeValueAsBytes(notificationDTO)));

        verify(settingService,times(1)).getNotificationSettings(clientId);
        verify(mapper,times(1)).userProfileToNotificationDto(mockUserProfile);
    }

    @Test
    public void testGetNotificationSettingsNotSuccessIfClientNotExist() {
        when(settingService.getNotificationSettings(clientId)).thenThrow(new InternalServerException("User profile not found"));

        assertThatThrownBy(() -> mockMvc.perform(get("/auth/user/settings/notifications/all")
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding("UTF-8")
                        .param("clientId", String.valueOf(clientId)))
                .andExpect(status().is5xxServerError()))
                .hasCause(new InternalServerException("User profile not found"));

        verify(settingService,times(1)).getNotificationSettings(clientId);

    }

    @Test
    public void testUpdateSmsNotificationSuccess() throws Exception {
        when(settingService.changeSmsNotification(clientId, notificationRequestDto.getNotificationStatus()))
                .thenReturn("SMS-notification has been successfully updated");

        MockHttpServletRequestBuilder requestBuilder = patch("/auth/user/settings/notifications/sms")
                .contentType(MediaType.APPLICATION_JSON)
                .characterEncoding("UTF-8")
                .param("clientId", String.valueOf(clientId))
                .content(objectMapper.writeValueAsString(notificationRequestDto));
        mockMvc.perform(requestBuilder)
                .andDo(print())
                .andExpect(status().isOk());

        verify(settingService,times(1)).changeSmsNotification(clientId, notificationRequestDto.getNotificationStatus());

    }

    @Test
    public void testUpdateSmsNotificationNotSuccessIfClientNotExist() {

        when(settingService.changeSmsNotification(clientId, notificationRequestDto.getNotificationStatus()))
                .thenThrow(new InternalServerException("User profile not found"));

        assertThatThrownBy(() -> mockMvc.perform(patch("/auth/user/settings/notifications/sms")
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding("UTF-8")
                        .param("clientId", String.valueOf(clientId))
                        .content(objectMapper.writeValueAsString(notificationRequestDto)))
                .andExpect(status().is5xxServerError()))
                .hasCause(new InternalServerException("User profile not found"));

        verify(settingService, times(1)).changeSmsNotification(clientId, notificationRequestDto.getNotificationStatus());

    }

    @Test
    public void testUpdatePushNotificationSuccess() throws Exception {
        when(settingService.changePushNotification(clientId, notificationRequestDto.getNotificationStatus()))
                .thenReturn("SMS-notification has been successfully updated");

        MockHttpServletRequestBuilder requestBuilder = patch("/auth/user/settings/notifications/push")
                .contentType(MediaType.APPLICATION_JSON)
                .characterEncoding("UTF-8")
                .param("clientId", String.valueOf(clientId))
                .content(objectMapper.writeValueAsString(notificationRequestDto));
        mockMvc.perform(requestBuilder)
                .andDo(print())
                .andExpect(status().isOk());

        verify(settingService,times(1)).changePushNotification(clientId, notificationRequestDto.getNotificationStatus());

    }

    @Test
    public void testUpdateEmailSubscriptionSuccess() throws Exception {
        when(settingService.changeEmailSubscription(clientId, notificationRequestDto.getNotificationStatus()))
                .thenReturn("SMS-notification has been successfully updated");

        MockHttpServletRequestBuilder requestBuilder = patch("/auth/user/settings/notifications/email")
                .contentType(MediaType.APPLICATION_JSON)
                .characterEncoding("UTF-8")
                .param("clientId", String.valueOf(clientId))
                .content(objectMapper.writeValueAsString(notificationRequestDto));
        mockMvc.perform(requestBuilder)
                .andDo(print())
                .andExpect(status().isOk());

        verify(settingService,times(1)).changeEmailSubscription(clientId, notificationRequestDto.getNotificationStatus());

    }

    @Test
    public void testUpdateEmailSubscriptionNotSuccessIfClientNotExist() {
        when(settingService.changeEmailSubscription(clientId, notificationRequestDto.getNotificationStatus()))
                .thenThrow(new UsernameNotFoundException("UNAUTHORIZED"));

        assertThatThrownBy(() -> mockMvc.perform(patch("/auth/user/settings/notifications/email")
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding("UTF-8")
                        .param("clientId", String.valueOf(clientId))
                        .content(objectMapper.writeValueAsString(notificationRequestDto)))
                .andExpect(status().is5xxServerError()))
                .hasCause(new UsernameNotFoundException("UNAUTHORIZED"));

        verify(settingService,times(1)).changeEmailSubscription(clientId, notificationRequestDto.getNotificationStatus());

    }

    @ParameterizedTest(name = "#{index} - Run test with validUpdatePasswordDto = {0}")
    @MethodSource("com.userservice.mapper.TestCreator#getStreamFromValidUpdatePasswordDto")
    public void testUpdatePasswordSuccess(UpdatePasswordDto validUpdatePasswordDto) throws Exception {
        when(settingService.changePassword(clientId, validUpdatePasswordDto.getOldPassword(), validUpdatePasswordDto.getNewPassword(),
                validUpdatePasswordDto.getConfirmNewPassword()))
                .thenReturn("Operation completed successfully");

        MockHttpServletRequestBuilder requestBuilder = patch("/auth/user/settings/password")
                .contentType(MediaType.APPLICATION_JSON)
                .characterEncoding("UTF-8")
                .param("clientId", String.valueOf(clientId))
                .content(objectMapper.writeValueAsString(validUpdatePasswordDto));
        mockMvc.perform(requestBuilder)
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string("Operation completed successfully"));

        verify(settingService,times(1)).changePassword(clientId, validUpdatePasswordDto.getOldPassword(), validUpdatePasswordDto.getNewPassword(),
                validUpdatePasswordDto.getConfirmNewPassword());

    }

    @ParameterizedTest(name = "#{index} - Run test with validUpdatePasswordDto = {0}")
    @MethodSource("com.userservice.mapper.TestCreator#getStreamFromNotValidUpdatePasswordDto")
    public void testUpdatePasswordNotSuccessWithNotValidPassword(UpdatePasswordDto validUpdatePasswordDto) throws Exception {

        MockHttpServletRequestBuilder requestBuilder = patch("/auth/user/settings/password")
                .contentType(MediaType.APPLICATION_JSON)
                .characterEncoding("UTF-8")
                .param("clientId", String.valueOf(clientId))
                .content(objectMapper.writeValueAsString(validUpdatePasswordDto));
        mockMvc.perform(requestBuilder)
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    public void testUpdatePasswordNotSuccessWithNotEqualsNewAndConfirmPassword() {
        when(settingService.changePassword(clientId, updatePasswordDto.getOldPassword(), updatePasswordDto.getNewPassword(),
                updatePasswordDto.getConfirmNewPassword()))
                .thenThrow(new WrongPasswordException("Passwords do not match"));

        assertThatThrownBy(() -> mockMvc.perform(patch("/auth/user/settings/password")
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding("UTF-8")
                        .param("clientId", String.valueOf(clientId))
                        .content(objectMapper.writeValueAsString(updatePasswordDto)))
                .andExpect(status().isBadGateway()))
                .hasCause(new WrongPasswordException("Passwords do not match"));

        verify(settingService, times(1)).changePassword(clientId, updatePasswordDto.getOldPassword(), updatePasswordDto.getNewPassword(),
                updatePasswordDto.getConfirmNewPassword());
    }

    @Test
    public void testUpdatePasswordNotSuccessIfClientNotExist() {
        when(settingService.changePassword(clientId, updatePasswordDto.getOldPassword(), updatePasswordDto.getNewPassword(),
                updatePasswordDto.getConfirmNewPassword()))
                .thenThrow(new BadRequestException("Invalid data"));

        assertThatThrownBy(() -> mockMvc.perform(patch("/auth/user/settings/password")
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding("UTF-8")
                        .param("clientId", String.valueOf(clientId))
                        .content(objectMapper.writeValueAsString(updatePasswordDto)))
                .andExpect(status().isUnauthorized()))
                .hasCause(new BadRequestException("Invalid data"));

        verify(settingService, times(1)).changePassword(clientId, updatePasswordDto.getOldPassword(), updatePasswordDto.getNewPassword(),
                updatePasswordDto.getConfirmNewPassword());
    }

    @Test
    public void testUpdatePasswordNotSuccessIfOldPasswordIsWrong() {
        when(settingService.changePassword(clientId, updatePasswordDto.getOldPassword(), updatePasswordDto.getNewPassword(),
                updatePasswordDto.getConfirmNewPassword()))
                .thenThrow(new BadRequestException("Wrong old password entered"));

        assertThatThrownBy(() -> mockMvc.perform(patch("/auth/user/settings/password")
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding("UTF-8")
                        .param("clientId", String.valueOf(clientId))
                        .content(objectMapper.writeValueAsString(updatePasswordDto)))
                .andExpect(status().isBadRequest()))
                .hasCause(new BadRequestException("Wrong old password entered"));

        verify(settingService, times(1)).changePassword(clientId, updatePasswordDto.getOldPassword(), updatePasswordDto.getNewPassword(),
                updatePasswordDto.getConfirmNewPassword());
    }
    @ParameterizedTest(name = "#{index} - Run test with updateEmailDto = {0}")
    @MethodSource("com.userservice.mapper.TestCreator#getStreamFromUpdateEmailDto")
    void testUpdateEmailSuccess(UpdateEmailDto updateEmailDto) throws Exception {
        when(settingService.changeEmail(clientId,updateEmailDto.getNewEmail())).thenReturn("Email has been successfully updated");
        MockHttpServletRequestBuilder requestBuilder = patch("/auth/user/settings/email")
                .contentType(MediaType.APPLICATION_JSON)
                .characterEncoding("UTF-8")
                .param("clientId", String.valueOf(clientId))
                .content(objectMapper.writeValueAsString(updateEmailDto));
        mockMvc.perform(requestBuilder)
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string("Email has been successfully updated"));
        verify(settingService,times(1)).changeEmail(clientId,updateEmailDto.getNewEmail());
    }

    @ParameterizedTest(name = "#{index} - Run test with updateEmailDto = {0}")
    @MethodSource("com.userservice.mapper.TestCreator#getStreamFromNotValidUpdateEmailDto")
    void testUpdateEmailNotSuccessWithNotValidEmail(UpdateEmailDto updateEmailDto) throws Exception {
        MockHttpServletRequestBuilder requestBuilder = patch("/auth/user/settings/email")
                .contentType(MediaType.APPLICATION_JSON)
                .characterEncoding("UTF-8")
                .param("clientId", String.valueOf(clientId))
                .content(objectMapper.writeValueAsString(updateEmailDto));
        mockMvc.perform(requestBuilder)
                .andDo(print())
                .andExpect(status().isBadRequest());
    }


    @ParameterizedTest(name = "#{index} - Run test with updateEmailDto = {0}")
    @MethodSource("com.userservice.mapper.TestCreator#getStreamFromUpdateEmailDto")
    void testUpdateEmailNotSuccessIfThisEmailIsExist(UpdateEmailDto updateEmailDto) throws Exception {
        when(settingService.changeEmail(clientId, updateEmailDto.getNewEmail())).thenThrow(new EmailExistsException("Email already exists"));
        assertThatThrownBy(() -> mockMvc.perform(patch("/auth/user/settings/email")
                .contentType(MediaType.APPLICATION_JSON)
                .characterEncoding("UTF-8")
                .param("clientId", String.valueOf(clientId))
                .content(objectMapper.writeValueAsString(updateEmailDto)))
                .andExpect(status().isBadRequest()))
                .hasCause(new EmailExistsException("Email already exists"));
        verify(settingService,times(1)).changeEmail(clientId,updateEmailDto.getNewEmail());
    }

    @ParameterizedTest(name = "#{index} - Run test with updateQADto = {0}")
    @MethodSource("com.userservice.mapper.TestCreator#getStreamFromValidUpdateQADto")
    void testUpdateSecurityQASuccess(UpdateQADto updateQADto) throws Exception {
        when(settingService.changeQA(clientId, updateQADto.getSecurityQuestion(), updateQADto.getSecurityAnswer()))
                .thenReturn("Operation completed successfully");

        MockHttpServletRequestBuilder requestBuilder = patch("/auth/user/settings/controls")
                .contentType(MediaType.APPLICATION_JSON)
                .characterEncoding("UTF-8")
                .param("clientId", String.valueOf(clientId))
                .content(objectMapper.writeValueAsString(updateQADto));
        mockMvc.perform(requestBuilder)
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string("Operation completed successfully"));

        verify(settingService, times(1)).changeQA(clientId, updateQADto.getSecurityQuestion(), updateQADto.getSecurityAnswer());
    }

    @ParameterizedTest(name = "#{index} - Run test with updateQADto = {0}")
    @MethodSource("com.userservice.mapper.TestCreator#getStreamFromNotValidUpdateQADto")
    void testUpdateSecurityQANotSuccessWithNotValidUpdateQA(UpdateQADto updateQADto) throws Exception {

        MockHttpServletRequestBuilder requestBuilder = patch("/auth/user/settings/controls")
                .contentType(MediaType.APPLICATION_JSON)
                .characterEncoding("UTF-8")
                .param("clientId", String.valueOf(clientId))
                .content(objectMapper.writeValueAsString(updateQADto));
        mockMvc.perform(requestBuilder)
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    void testGetClientInformationSuccess() throws Exception {
        when(settingService.getClientInformation(clientId)).thenReturn(client);
        when(mapper.clientToInformationDto(client)).thenReturn(clientInformationDto);

        MockHttpServletRequestBuilder requestBuilder = get("/auth/user/settings/information/client")
                .contentType(MediaType.APPLICATION_JSON)
                .characterEncoding("UTF-8")
                .param("clientId", String.valueOf(clientId));
        mockMvc.perform(requestBuilder)
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.countryOfResidence").value(clientInformationDto.getCountryOfResidence()))
                .andExpect(jsonPath("$.email").value(String.valueOf(clientInformationDto.getEmail())))
                .andExpect(jsonPath("$.firstName").value(String.valueOf(clientInformationDto.getFirstName())))
                .andExpect(jsonPath("$.lastName").value(String.valueOf(clientInformationDto.getLastName())))
                .andExpect(jsonPath("$.middleName").value(String.valueOf(clientInformationDto.getMiddleName())))
                .andExpect(jsonPath("$.mobilePhone").value(String.valueOf(clientInformationDto.getMobilePhone())))
                .andExpect(jsonPath("$.passportNumber").value(String.valueOf(clientInformationDto.getPassportNumber())));

        verify(settingService,times(1)).getClientInformation(clientId);
        verify(mapper,times(1)).clientToInformationDto(client);

    }
    @Test
    void testGetClientInformationNotSuccessIfClientNotExist() throws Exception {
        when(settingService.getClientInformation(clientId)).thenThrow(new BadRequestException("Invalid data"));

        assertThatThrownBy(() -> mockMvc.perform(get("/auth/user/settings/information/client")
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding("UTF-8")
                        .param("clientId", String.valueOf(clientId)))
                .andExpect(status().isBadRequest()))
                .hasCause(new BadRequestException("Invalid data"));

        verify(settingService,times(1)).getClientInformation(clientId);

    }

}
