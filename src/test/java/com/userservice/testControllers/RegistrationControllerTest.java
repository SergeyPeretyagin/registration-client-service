package com.userservice.testControllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.userservice.controller.RegistrationController;
import com.userservice.domain.dto.RequestClientDto;
import com.userservice.domain.dto.RequestClientNonRegisteredDto;
import com.userservice.domain.dto.RequestNonClientDto;
import com.userservice.domain.entity.Client;
import com.userservice.domain.entity.ClientStatus;
import com.userservice.domain.exception.BadRequestException;
import com.userservice.domain.exception.InternalServerException;
import com.userservice.domain.mapper.ClientMapper;
import com.userservice.domain.mapper.MapperToClient;
import com.userservice.mapper.TestCreator;
import com.userservice.service.RegistrationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.http.MediaType;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import java.util.Optional;
import java.util.UUID;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@WebMvcTest(value = RegistrationController.class, useDefaultFilters = false,includeFilters = {
        @ComponentScan.Filter(
                type = FilterType.ASSIGNABLE_TYPE,
                value = RegistrationController.class
        )})
@WebAppConfiguration
public class RegistrationControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private ClientMapper clientMapper;
    @MockBean
    private RegistrationService registrationService;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private WebApplicationContext webApplicationContext;

    @MockBean
    private DiscoveryClient discoveryClient;
    @MockBean
    private MapperToClient mapperToClient;


    @BeforeEach
    void setUp(){
         mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext)
                .build();
    }

    @ParameterizedTest(name = "#{index} - Run test with requestNonClientDto = {0}")
    @MethodSource("com.userservice.mapper.TestCreator#validStreamRequestNonClientDto")
    void signUpSuccessWithValidRequestNonClientDto(RequestNonClientDto requestNonClientDto) throws Exception {
        Client newClient = TestCreator.getNewNotClient(requestNonClientDto);
        when(registrationService.registerNonClient(newClient)).thenReturn(newClient);
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.post("/registration/user-profile/new")
                .contentType(MediaType.APPLICATION_JSON)
                .characterEncoding("UTF-8")
                .content(objectMapper.writeValueAsString(requestNonClientDto));
        mockMvc.perform(requestBuilder)
                .andDo(print())
                .andExpect(status().isOk());
    }

    @ParameterizedTest(name = "#{index} - Run test with requestNonClientDto = {0}")
    @MethodSource("com.userservice.mapper.TestCreator#notValidStreamRequestNonClientDto")
    void signUpSuccessWithValidNotRequestNonClientDto(RequestNonClientDto requestNonClientDto) throws Exception {
        Client newClient = TestCreator.getNewNotClient(requestNonClientDto);
        when(registrationService.registerNonClient(newClient)).thenReturn(newClient);
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.post("/registration/user-profile/new")
                .contentType(MediaType.APPLICATION_JSON)
                .characterEncoding("UTF-8")
                .content(objectMapper.writeValueAsString(requestNonClientDto));
        mockMvc.perform(requestBuilder)
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    void signUpNotSuccessWithExistNonClient() {
        RequestNonClientDto requestNonClientDto = TestCreator.getRequestNonClientDto();
        Client existClient = TestCreator.getNewNotClient(requestNonClientDto);
        when(registrationService.registerNonClient(existClient)).thenThrow(new BadRequestException("User is already exists"));
        assertThatThrownBy(() -> mockMvc.perform(patch("/registration/user-profile/new")
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding("UTF-8")
                        .content(objectMapper.writeValueAsString(requestNonClientDto)))
                .andExpect(status().isBadRequest()));
    }

    @Test
    void signUpNotSuccessWithDontSaveNonClient() {
        RequestNonClientDto requestNonClientDto = TestCreator.getRequestNonClientDto();
        Client dontSaveClient = TestCreator.getNewNotClient(requestNonClientDto);
        when(registrationService.registerNonClient(dontSaveClient)).thenThrow(new InternalServerException(""));

        assertThatThrownBy(() -> mockMvc.perform(patch("/registration/user-profile/new")
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding("UTF-8")
                        .content(objectMapper.writeValueAsString(requestNonClientDto)))
                .andExpect(status().isInternalServerError()));
    }

    @ParameterizedTest(name = "#{index} - Run test with requestClientDto = {0}")
    @MethodSource("com.userservice.mapper.TestCreator#validStreamRequestClientDto")
    void signUpSuccessWithValidRequestClientDto(RequestClientDto requestClientDto) throws Exception {
        Client newClient = TestCreator.getNewClient(requestClientDto);
        when(registrationService.registerNonClient(newClient)).thenReturn(newClient);
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.patch("/registration/user-profile")
                .contentType(MediaType.APPLICATION_JSON)
                .characterEncoding("UTF-8")
                .content(objectMapper.writeValueAsString(requestClientDto));
        mockMvc.perform(requestBuilder)
                .andDo(print())
                .andExpect(status().isOk());
    }

    @ParameterizedTest(name = "#{index} - Run test with requestClientDto = {0}")
    @MethodSource("com.userservice.mapper.TestCreator#notValidStreamRequestClientDto")
    void signUpSuccessWithValidNotRequestClientDto(RequestClientDto requestClientDto) throws Exception {
        Client newClient = TestCreator.getNewClient(requestClientDto);
        when(registrationService.registerNonClient(newClient)).thenReturn(newClient);
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.patch("/registration/user-profile")
                .contentType(MediaType.APPLICATION_JSON)
                .characterEncoding("UTF-8")
                .content(objectMapper.writeValueAsString(requestClientDto));
        mockMvc.perform(requestBuilder)
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    void signUpNotSuccessWithDontSaveClient() {
        RequestClientDto requestClientDto = TestCreator.getRequestClientDto();
        Client dontSaveClient = TestCreator.getNewClient(requestClientDto);
        when(registrationService.registerClient(dontSaveClient)).thenThrow(new InternalServerException(""));
        assertThatThrownBy(() -> mockMvc.perform(patch("/registration/user-profile")
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding("UTF-8")
                        .content(objectMapper.writeValueAsString(requestClientDto)))
                .andExpect(status().isInternalServerError()));
    }

    @Test
    void testCheckRegistrationIfUserNotClient() throws Exception {
        UUID clientId = UUID.randomUUID();
        String mobilePhone = "123456789012";
        Optional<Client> client = TestCreator.getOptionalClientNotRegistered(mobilePhone,clientId);
        when(registrationService.checkRegistration(mobilePhone)).thenReturn(client);
        mockMvc.perform(get("/registration")
                        .param("mobilePhone", mobilePhone)
                        .characterEncoding("UTF-8"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.mobilePhone").value(mobilePhone))
                .andExpect(jsonPath("$.clientStatus").value(String.valueOf(client.get().getClientStatus())))
                .andExpect(jsonPath("$.id").value(String.valueOf(clientId)))
                .andExpect(jsonPath("$.message").value("Operation completed successfully"));

    }
    @Test
    void testCheckRegistrationIfUserNotFound() throws Exception {
        UUID clientId = UUID.randomUUID();
        String mobilePhone = "123456789012";
        Optional<Client> client = TestCreator.getOptionalClientNotFound(mobilePhone,clientId);
        when(registrationService.checkRegistration(mobilePhone)).thenReturn(client);
        mockMvc.perform(get("/registration")
                        .param("mobilePhone", mobilePhone)
                        .characterEncoding("UTF-8"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.mobilePhone").value(mobilePhone))
                .andExpect(jsonPath("$.clientStatus").value(String.valueOf(ClientStatus.NOT_CLIENT)))
                .andExpect(jsonPath("$.message").value("Operation completed successfully"));

    }

    @ParameterizedTest(name = "#{index} - Run test with clientStatus = {0}")
    @MethodSource("com.userservice.mapper.TestCreator#streamWithClientStatusActiveAndNotActive")
    void testCheckRegistrationIfUserActiveOrNotActive(Optional<Client> optionalClient){
        String mobilePhone = "123456789012";
        when(registrationService.checkRegistration(mobilePhone)).thenThrow(new BadRequestException("User is already exists"));
        assertThatThrownBy(() -> mockMvc.perform(get("/registration")
                        .param("mobilePhone", mobilePhone)
                        .characterEncoding("UTF-8"))
                .andExpect(status().isBadRequest()))
                .hasCause(new BadRequestException("User is already exists"));
        verify(registrationService,times(1)).checkRegistration(mobilePhone);
    }

    @ParameterizedTest(name = "#{index} - Run test with phoneNumber = {0}")
    @ValueSource(strings = {"1","12","123","asd","123123","dsada13123","12345677"})
    void testCheckRegistrationIfPhoneNumberIsNotValid(String mobilePhone){
        assertThatThrownBy(() -> mockMvc.perform(get("/registration")
                        .param("mobilePhone", mobilePhone)
                        .characterEncoding("UTF-8"))
                .andExpect(status().isBadRequest()));

    }

    @Test
    void becomesClientOfBankSuccess() throws Exception {
        RequestClientNonRegisteredDto nonRegisteredDto = TestCreator.getRequestClientNonRegistered();
        Client client = TestCreator.getClient(nonRegisteredDto);
        when(mapperToClient.reguestClientNonRegisteredDtoToClient(nonRegisteredDto)).thenReturn(client);
        when(registrationService.registrationClientInBank(client)).thenReturn("Operation completed successfully");
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.post("/registration")
                .contentType(MediaType.APPLICATION_JSON)
                .characterEncoding("UTF-8")
                .content(objectMapper.writeValueAsString(nonRegisteredDto));
        mockMvc.perform(requestBuilder)
                .andDo(print())
                .andExpect(status().isOk());
        verify(mapperToClient,times(1)).reguestClientNonRegisteredDtoToClient(nonRegisteredDto);
        verify(registrationService,times(1)).registrationClientInBank(client);
    }

    @ParameterizedTest(name = "#{index} - Run test with nonRegisteredDto = {0}")
    @MethodSource("com.userservice.mapper.TestCreator#getRequestClientNonRegisteredWithNotValidData")
    void becomesClientOfBankNotSuccessWithNotValidData() throws Exception {
        RequestClientNonRegisteredDto nonRegisteredDto = TestCreator.getRequestClientNonRegistered();
        Client client = TestCreator.getClient(nonRegisteredDto);
        when(mapperToClient.reguestClientNonRegisteredDtoToClient(nonRegisteredDto)).thenReturn(client);
        when(registrationService.registrationClientInBank(client)).thenReturn("Operation completed successfully");
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.post("/registration")
                .contentType(MediaType.APPLICATION_JSON)
                .characterEncoding("UTF-8")
                .content(objectMapper.writeValueAsString(nonRegisteredDto));
        mockMvc.perform(requestBuilder)
                .andDo(print())
                .andExpect(status().isOk());
        verify(mapperToClient,times(1)).reguestClientNonRegisteredDtoToClient(nonRegisteredDto);
        verify(registrationService,times(1)).registrationClientInBank(client);
    }

    @Test
    void becomesClientOfBankNotSuccessIfClientExists() throws Exception {
        RequestClientNonRegisteredDto nonRegisteredDto = TestCreator.getRequestClientNonRegistered();
        Client client = TestCreator.getClient(nonRegisteredDto);
        when(mapperToClient.reguestClientNonRegisteredDtoToClient(nonRegisteredDto)).thenReturn(client);
        when(registrationService.registrationClientInBank(client)).thenThrow(new BadRequestException("Invalid data"));

        assertThatThrownBy(() -> mockMvc.perform(post("/registration")
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding("UTF-8")
                        .content(objectMapper.writeValueAsString(nonRegisteredDto)))
                .andExpect(status().isBadRequest()))
                .hasCause(new BadRequestException("Invalid data"));

        verify(mapperToClient,times(1)).reguestClientNonRegisteredDtoToClient(nonRegisteredDto);
        verify(registrationService,times(1)).registrationClientInBank(client);
    }

}


