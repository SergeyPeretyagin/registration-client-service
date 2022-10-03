package com.userservice.service.impl;


import com.userservice.component.CreditServiceClient;
import com.userservice.domain.dto.CardByClientIdResponseDto;
import com.userservice.service.ConnectService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ConnectServiceImpl implements ConnectService {
    private final CreditServiceClient creditServiceClient;
    @Override
    public List<CardByClientIdResponseDto> getListCardsByClientId(UUID clientId){
        return creditServiceClient.getListFromCreditService(clientId);
    }

}
