package com.userservice.testControllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.userservice.controller.DeleteController;
import com.userservice.controller.RegistrationController;
import com.userservice.domain.exception.NotExistsUserException;
import com.userservice.service.DeleteService;
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
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(value = RegistrationController.class, useDefaultFilters = false,includeFilters = {
        @ComponentScan.Filter(
                type = FilterType.ASSIGNABLE_TYPE,
                value = DeleteController.class
        )})
@WebAppConfiguration
public class DeleteControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private WebApplicationContext webApplicationContext;
    @MockBean
    private DeleteService deleteService;
    @Autowired
    private ObjectMapper objectMapper;
    @BeforeEach
    void setUp(){
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext)
                .build();
    }

    @Test
    void testDeleteUserFromDBSuccessWithValid() throws Exception {
        UUID userId = UUID.randomUUID();
        when(deleteService.deleteUserByUUID(userId)).thenReturn("User account deleted successfully");
        MockHttpServletRequestBuilder requestBuilder = delete("/user-service/user")
                .contentType(MediaType.APPLICATION_JSON)
                .characterEncoding("UTF-8")
                .param("clientId", String.valueOf(userId));
        mockMvc.perform(requestBuilder)
                .andDo(print())
                .andExpect(status().isNoContent())
                .andExpect(content().string("User account deleted successfully"));
        verify(deleteService,times(1)).deleteUserByUUID(userId);
    }

    @Test
    void testDeleteUserFromDBNotSuccessWithNotValidUUID() throws Exception {
        UUID userId = null;
        MockHttpServletRequestBuilder requestBuilder = delete("/user-service/user")
                .contentType(MediaType.APPLICATION_JSON)
                .characterEncoding("UTF-8")
                .param("clientId", String.valueOf(userId));
        mockMvc.perform(requestBuilder)
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    void testDeleteUserFromDBNotSuccessWithNotExistUUID() throws Exception {
        UUID userId = UUID.randomUUID();
        when(deleteService.deleteUserByUUID(userId)).thenThrow(new NotExistsUserException("This user does not exist"));

        assertThatThrownBy(() -> mockMvc.perform(delete("/user-service/user")
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding("UTF-8")
                        .param("clientId", String.valueOf(userId)))
                .andExpect(status().isNotFound()))
                .hasCause(new NotExistsUserException("This user does not exist"));

        verify(deleteService,times(1)).deleteUserByUUID(userId);
    }

}
