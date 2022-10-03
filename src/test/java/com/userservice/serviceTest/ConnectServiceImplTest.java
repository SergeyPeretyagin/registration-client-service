package com.userservice.serviceTest;

import com.userservice.component.CreditServiceClient;
import com.userservice.domain.dto.CardByClientIdResponseDto;
import com.userservice.service.impl.ConnectServiceImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ConnectServiceImplTest {
    @Mock
    private CreditServiceClient creditServiceClient;
    @InjectMocks
    private ConnectServiceImpl connectService;

    @BeforeEach
    void setUp(){
        connectService = new ConnectServiceImpl(creditServiceClient);
    }

    @Test
    void testSuccessGetListFromCreditService() {
        UUID id = UUID.randomUUID();
        List<CardByClientIdResponseDto> actualCards = new ArrayList<>();
        when(creditServiceClient.getListFromCreditService(id)).thenReturn(actualCards);
        List<CardByClientIdResponseDto> expectedCards = connectService.getListCardsByClientId(id);
        verify(creditServiceClient,times(1)).getListFromCreditService(id);
        Assertions.assertEquals(actualCards, expectedCards);
    }
}
