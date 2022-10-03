package com.userservice.componentTest;

import com.userservice.component.CreditServiceClient;
import com.userservice.domain.dto.CardByClientIdResponseDto;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
class RestTemplateClientTest {

    @InjectMocks
    private CreditServiceClient creditServiceClient;

    @Mock
    private RestTemplate restTemplate;


    @Test
    void testSuccessGetListCards()  {
        UUID id = UUID.randomUUID();
        List<CardByClientIdResponseDto> actualCards = new ArrayList<>();
        when(restTemplate.getForObject("http://credit-service/auth/credit-cards?clientId=" + id, List.class)).thenReturn(actualCards);
        List<CardByClientIdResponseDto> expectedCards = creditServiceClient.getListFromCreditService(id);
        verify(restTemplate,times(1)).getForObject("http://credit-service/auth/credit-cards?clientId=" + id, List.class);
        Assertions.assertEquals(actualCards, expectedCards);
    }

}
