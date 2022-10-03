package com.userservice.testControllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.userservice.controller.AuthController;
import com.userservice.controller.RegistrationController;
import com.userservice.domain.dto.AuthDto;
import com.userservice.domain.dto.AuthenticationRequestDto;
import com.userservice.domain.entity.UserProfile;
import com.userservice.domain.exception.BadRequestException;
import com.userservice.domain.mapper.UserProfileMapper;
import com.userservice.service.AuthService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
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

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(value = RegistrationController.class, useDefaultFilters = false,includeFilters = {
        @ComponentScan.Filter(
                type = FilterType.ASSIGNABLE_TYPE,
                value = AuthController.class
        )})
@WebAppConfiguration
public class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserProfileMapper userProfileMapper;
    @MockBean
    private AuthService authService;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private WebApplicationContext webApplicationContext;
    private UserProfile mockUserProfile;
    private AuthDto actualAuthDto;
    private AuthenticationRequestDto authenticationRequestDto;

    private UUID userId;

    @BeforeEach
    void setUp(){
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext)
                .build();
        mockUserProfile = mock(UserProfile.class);
        actualAuthDto = new AuthDto(UUID.fromString("4ba07598-1c12-4ea8-a669-0cee9d83b7ba"), "aassdd1122!!!");
        authenticationRequestDto = new AuthenticationRequestDto("441234567890","aassdd1122!!!");
        userId = UUID.randomUUID();
    }

    @Test
    public void testGetIdByPhoneNumberSuccess() throws Exception {
        when(authService.getUserProfileByPhoneNumberOrPassport(authenticationRequestDto.getLogin())).thenReturn(mockUserProfile);
        when(userProfileMapper.responseUserProfileToAuthDto(mockUserProfile)).thenReturn(actualAuthDto);

        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.post("/login")
                .contentType(MediaType.APPLICATION_JSON)
                .characterEncoding("UTF-8")
                .content(objectMapper.writeValueAsString(authenticationRequestDto));
        mockMvc.perform(requestBuilder)
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(String.valueOf(actualAuthDto.getId())))
                .andExpect(jsonPath("$.password").value(actualAuthDto.getPassword()));
        verify(authService,times(1)).getUserProfileByPhoneNumberOrPassport(authenticationRequestDto.getLogin());
        verify(userProfileMapper,times(1)).responseUserProfileToAuthDto(mockUserProfile);
    }

    @Test
    public void testGetIdByPhoneNumberNotSuccessWithNotExistUser() throws Exception {
        when(authService.getUserProfileByPhoneNumberOrPassport(authenticationRequestDto.getLogin())).thenThrow(new BadRequestException("Invalid data"));

        assertThrows(BadRequestException.class, ()->when(authService.getUserProfileByPhoneNumberOrPassport(authenticationRequestDto.getLogin()))
                .thenThrow(new BadRequestException("Invalid data")));
        verify(authService,times(1)).getUserProfileByPhoneNumberOrPassport(authenticationRequestDto.getLogin());
    }

    @Test
    public void testGetAuthDtoByPassportSuccess() throws Exception {
        when(authService.getUserProfileByPhoneNumberOrPassport(authenticationRequestDto.getLogin())).thenReturn(mockUserProfile);
        when(userProfileMapper.responseUserProfileToAuthDto(mockUserProfile)).thenReturn(actualAuthDto);

        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.post("/login")
                .contentType(MediaType.APPLICATION_JSON)
                .characterEncoding("UTF-8")
                .content(objectMapper.writeValueAsString(authenticationRequestDto));
        mockMvc.perform(requestBuilder)
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(String.valueOf(actualAuthDto.getId())))
                .andExpect(jsonPath("$.password").value(actualAuthDto.getPassword()));

        verify(authService,times(1)).getUserProfileByPhoneNumberOrPassport(authenticationRequestDto.getLogin());
        verify(userProfileMapper,times(1)).responseUserProfileToAuthDto(mockUserProfile);
    }

    @Test
    public void testGetIdByPassportNotSuccessWithNotExistUser() throws Exception {
        when(authService.getUserProfileByPhoneNumberOrPassport(authenticationRequestDto.getLogin())).thenThrow(new BadRequestException("Invalid data"));

        assertThrows(BadRequestException.class, ()->when(authService.getUserProfileByPhoneNumberOrPassport(authenticationRequestDto.getLogin()))
                .thenThrow(new BadRequestException("Invalid data")));
        verify(authService,times(1)).getUserProfileByPhoneNumberOrPassport(authenticationRequestDto.getLogin());
    }

}
