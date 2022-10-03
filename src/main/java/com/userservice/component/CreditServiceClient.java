package com.userservice.component;

import com.userservice.domain.dto.CardByClientIdResponseDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import java.util.List;
import java.util.UUID;

@Component
@RequiredArgsConstructor
@Slf4j
public class CreditServiceClient {

    private final RestTemplate restTemplate;
    public List<CardByClientIdResponseDto> getListFromCreditService(UUID clientId){
        log.info("rest template request {} ",clientId);
        return restTemplate.getForObject("http://credit-service/auth/credit-cards?clientId="+clientId,List.class);
    }
}
