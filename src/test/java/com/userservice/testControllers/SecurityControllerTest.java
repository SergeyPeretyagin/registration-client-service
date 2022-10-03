package com.userservice.testControllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.userservice.controller.RegistrationController;
import com.userservice.controller.SecurityController;
import com.userservice.domain.dto.PassportNumberRequestDto;
import com.userservice.domain.dto.PhoneNumberDto;
import com.userservice.domain.dto.UserVerificationDto;
import com.userservice.domain.exception.BadRequestException;
import com.userservice.domain.exception.VerificationAcceptException;
import com.userservice.mapper.TestCreator;
import com.userservice.service.SecurityService;
import com.userservice.service.VerificationSenderService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
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

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(value = RegistrationController.class, useDefaultFilters = false,includeFilters = {
        @ComponentScan.Filter(
                type = FilterType.ASSIGNABLE_TYPE,
                value = SecurityController.class
        )})
@WebAppConfiguration
public class SecurityControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private WebApplicationContext webApplicationContext;
    @MockBean
    private  SecurityService securityService;
    @MockBean
    private  VerificationSenderService verificationSenderService;
    @Autowired
    private ObjectMapper objectMapper;
    private String mobilePhone;
    private String verificationCode;
    private UserVerificationDto userVerificationDto;
    private PhoneNumberDto mobilePhoneDto;

    @BeforeEach
    void setUp(){
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext)
                .build();
        mobilePhone = TestCreator.getMobilePhone();
        verificationCode = TestCreator.getverificationCode();
        userVerificationDto = TestCreator.getUserVerificationDto();
        mobilePhoneDto = TestCreator.getPhoneNumberDto();
    }

    @ParameterizedTest(name = "#{index} - Run test with passportNumber = {0}")
    @MethodSource("com.userservice.mapper.TestCreator#getStreamFromValidPassportNumberRequestDto")
    void testGetPhoneByPassportNumberSuccessWithValidPassport(PassportNumberRequestDto passportNumber) throws Exception {
        when(securityService.getPhoneByPassport(passportNumber.getPassportNumber()))
                .thenReturn(passportNumber.getPassportNumber());

        MockHttpServletRequestBuilder requestBuilder = post("/security/session")
                .contentType(MediaType.APPLICATION_JSON)
                .characterEncoding("UTF-8")
                .content(objectMapper.writeValueAsString(passportNumber));
        mockMvc.perform(requestBuilder)
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string(passportNumber.getPassportNumber()));

        verify(securityService,times(1)).getPhoneByPassport(passportNumber.getPassportNumber());
    }

    @ParameterizedTest(name = "#{index} - Run test with passportNumber = {0}")
    @MethodSource("com.userservice.mapper.TestCreator#getStreamFromNotValidPassportNumberRequestDto")
    void testGetPhoneByPassportNumberNotSuccessWithNotValidPassport(PassportNumberRequestDto passportNumber) throws Exception {

        MockHttpServletRequestBuilder requestBuilder = post("/security/session")
                .contentType(MediaType.APPLICATION_JSON)
                .characterEncoding("UTF-8")
                .content(objectMapper.writeValueAsString(passportNumber));
        mockMvc.perform(requestBuilder)
                .andDo(print())
                .andExpect(status().isBadRequest());

    }

    @Test
    void testGetPhoneByPassportNumberNotSuccessWithNotExistPassport(){
        PassportNumberRequestDto notExistPassportDto = new PassportNumberRequestDto("GBO123123123");
        when(securityService.getPhoneByPassport(notExistPassportDto.getPassportNumber()))
                .thenThrow(new BadRequestException("Incorrect number"));

        assertThatThrownBy(() -> mockMvc.perform(post("/security/session")
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding("UTF-8")
                        .content(objectMapper.writeValueAsString(notExistPassportDto)))
                .andExpect(status().isBadRequest()))
                .hasCause(new BadRequestException("Incorrect number"));

        verify(securityService,times(1)).getPhoneByPassport(notExistPassportDto.getPassportNumber());

    }

    @Test
    void testGetVerificationCodeSuccess() throws Exception {
        when(securityService.sendCodeToPhone(mobilePhone)).thenReturn(verificationCode);

        MockHttpServletRequestBuilder requestBuilder = get("/security/session")
                .contentType(MediaType.APPLICATION_JSON)
                .characterEncoding("UTF-8")
                .param("receiver", mobilePhone);
        mockMvc.perform(requestBuilder)
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string(verificationCode));

        verify(securityService,times(1)).sendCodeToPhone(mobilePhone);
    }

    @Test
    void testGetVerificationCodeNotSuccessWithNotValidReceiver() throws Exception {
        when(securityService.sendCodeToPhone(mobilePhone)).thenThrow(new BadRequestException("Invalid data"));

        assertThatThrownBy(() -> mockMvc.perform(get("/security/session")
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding("UTF-8")
                        .param("receiver", mobilePhone))
                .andExpect(status().isBadRequest()))
                .hasCause(new BadRequestException("Invalid data"));

        verify(securityService,times(1)).sendCodeToPhone(mobilePhone);
    }


    @ParameterizedTest(name = "#{index} - Run test with userVerificationDto = {0}")
    @MethodSource("com.userservice.mapper.TestCreator#getStreamFromValidUserVerificationDto")
    void testUserVerificationSuccessWithValidUserVerificationDto(UserVerificationDto userVerificationDto) throws Exception {
        when(securityService.verify(userVerificationDto)).thenReturn("Operation completed successfully");

        MockHttpServletRequestBuilder requestBuilder = post("/security/session/verification")
                .contentType(MediaType.APPLICATION_JSON)
                .characterEncoding("UTF-8")
                .content(objectMapper.writeValueAsString(userVerificationDto));
        mockMvc.perform(requestBuilder)
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string("Operation completed successfully"));
        verify(securityService,times(1)).verify(userVerificationDto);
    }

    @ParameterizedTest(name = "#{index} - Run test with userVerificationDto = {0}")
    @MethodSource("com.userservice.mapper.TestCreator#getStreamFromNotValidUserVerificationDto")
    void testUserVerificationNotSuccessWithNotValidUserVerificationDto(UserVerificationDto userVerificationDto) throws Exception {

        MockHttpServletRequestBuilder requestBuilder = post("/security/session/verification")
                .contentType(MediaType.APPLICATION_JSON)
                .characterEncoding("UTF-8")
                .content(objectMapper.writeValueAsString(userVerificationDto));
        mockMvc.perform(requestBuilder)
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    void testUserVerificationNotSuccessIfNoDataInDB(){
        when(securityService.verify(userVerificationDto)).thenThrow(new BadRequestException("Phone number is invalid"));
        assertThatThrownBy(() -> mockMvc.perform(post("/security/session/verification")
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding("UTF-8")
                        .content(objectMapper.writeValueAsString(userVerificationDto)))
                .andExpect(status().isUnauthorized()))
                .hasCause(new BadRequestException("Phone number is invalid"));
        verify(securityService,times(1)).verify(userVerificationDto);

    }

    @Test
    void testUserVerificationNotSuccessIfCodeVerificationIsExpired(){
        when(securityService.verify(userVerificationDto)).thenThrow(new BadRequestException("Code is expired"));
        assertThatThrownBy(() -> mockMvc.perform(post("/security/session/verification")
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding("UTF-8")
                        .content(objectMapper.writeValueAsString(userVerificationDto)))
                .andExpect(status().isNotAcceptable()))
                .hasCause(new BadRequestException("Code is expired"));
        verify(securityService,times(1)).verify(userVerificationDto);
    }

    @Test
    void testUserVerificationNotSuccessIfUserEnterInvalidData(){
        when(securityService.verify(userVerificationDto)).thenThrow(new BadRequestException("Verification code is invalid"));
        assertThatThrownBy(() -> mockMvc.perform(post("/security/session/verification")
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding("UTF-8")
                        .content(objectMapper.writeValueAsString(userVerificationDto)))
                .andExpect(status().isBadRequest()))
                .hasCause(new BadRequestException("Verification code is invalid"));
        verify(securityService,times(1)).verify(userVerificationDto);

    }
    @Test
    void testUserVerificationNotSuccessIfBlockExpiredExist(){
        String seconds ="12";
        when(securityService.verify(userVerificationDto)).thenThrow(new VerificationAcceptException(seconds));
        assertThatThrownBy(() -> mockMvc.perform(post("/security/session/verification")
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding("UTF-8")
                        .content(objectMapper.writeValueAsString(userVerificationDto)))
                .andExpect(status().isNotAcceptable()))
                .hasCause(new VerificationAcceptException(seconds));
        verify(securityService,times(1)).verify(userVerificationDto);
    }

}
