package com.userservice.testControllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.userservice.controller.LoginController;
import com.userservice.domain.dto.UpdatePasswordRequestDto;
import com.userservice.domain.exception.BadRequestException;
import com.userservice.domain.exception.WrongPasswordException;
import com.userservice.service.impl.LoginServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.http.MediaType;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.assertj.core.api.Assertions.*;
import static org.hamcrest.Matchers.containsString;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.times;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;



@WebMvcTest(value = LoginController.class, useDefaultFilters = false,includeFilters = {
        @ComponentScan.Filter(
                type = FilterType.ASSIGNABLE_TYPE,
                value = LoginController.class
        )})
@WebAppConfiguration
public class LoginControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private LoginServiceImpl loginService;

    @Autowired
    private WebApplicationContext webApplicationContext;


    @BeforeEach
    void setUp(){
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext)
                .build();
    }


    @ParameterizedTest(name = "#{index} - Run test with updatePasswordRequestDto = {0}")
    @MethodSource("com.userservice.mapper.CreatorOldAndNewPassword#validStreamValidNewAndConfirmPasswords")
    void testUpdatePasswordSuccessWithValidAllParameters(UpdatePasswordRequestDto updatePasswordRequestDto)
            throws Exception {
        when(loginService.updatePassword(updatePasswordRequestDto.getPassportNumber(),
                updatePasswordRequestDto.getNewPassword(), updatePasswordRequestDto.getConfirmNewPassword()))
                .thenReturn("Operation completed successfully");


        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.patch("/login/password")
                .contentType(MediaType.APPLICATION_JSON)
                .characterEncoding("UTF-8")
                .content(objectMapper.writeValueAsString(updatePasswordRequestDto));
        mockMvc.perform(requestBuilder)
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("Operation completed successfully")));

        verify(loginService,times(1)).updatePassword(updatePasswordRequestDto.getPassportNumber(),
                updatePasswordRequestDto.getNewPassword(), updatePasswordRequestDto.getConfirmNewPassword());
    }

    @ParameterizedTest(name = "#{index} - Run test with updatePasswordRequestDto = {0}")
    @MethodSource("com.userservice.mapper.CreatorOldAndNewPassword#streamWithNotValidPassportNumberAndValidNewAndConfirmPasswords")
    void testUpdatePasswordCatchExceptionWithNotValidPassportNumberAndValidNewAndConfirmPasswords(UpdatePasswordRequestDto updatePasswordRequestDto) throws Exception {
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.patch("/login/password")
                .contentType(MediaType.APPLICATION_JSON)
                .characterEncoding("UTF-8")
                .content(objectMapper.writeValueAsString(updatePasswordRequestDto));
        mockMvc.perform(requestBuilder)
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @ParameterizedTest(name = "#{index} - Run test with updatePasswordRequestDto = {0}")
    @MethodSource("com.userservice.mapper.CreatorOldAndNewPassword#validStreamValidNewAndConfirmPasswords")
    void testUpdatePasswordCatchExceptionWithValidPassportNumberAndNotEqualsNewAndConfirmPasswords(UpdatePasswordRequestDto updatePasswordRequestDto){

        when(loginService.updatePassword(updatePasswordRequestDto.getPassportNumber(),
                updatePasswordRequestDto.getNewPassword(), updatePasswordRequestDto.getConfirmNewPassword()))
                .thenThrow(new WrongPasswordException("Passwords do not match"));

        assertThatThrownBy(() -> mockMvc.perform(patch("/login/password")
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding("UTF-8")
                        .content(objectMapper.writeValueAsString(updatePasswordRequestDto)))
                .andExpect(status().isBadRequest()))
                .hasCause(new WrongPasswordException("Passwords do not match"));

        verify(loginService,times(1)).updatePassword(updatePasswordRequestDto.getPassportNumber(),
                updatePasswordRequestDto.getNewPassword(), updatePasswordRequestDto.getConfirmNewPassword());
    }

    @ParameterizedTest(name = "#{index} - Run test with updatePasswordRequestDto = {0}")
    @MethodSource("com.userservice.mapper.CreatorOldAndNewPassword#streamNotValidNewAndConfirmPasswords")
    void testUpdatePasswordCatchExceptionWithValidPhoneNumberAndNotValidNewAndConfirmPasswords(UpdatePasswordRequestDto updatePasswordRequestDto) throws Exception {
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.patch("/login/password")
                .contentType(MediaType.APPLICATION_JSON)
                .characterEncoding("UTF-8")
                .content(objectMapper.writeValueAsString(updatePasswordRequestDto));
        mockMvc.perform(requestBuilder)
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

}
