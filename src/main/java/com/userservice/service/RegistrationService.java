package com.userservice.service;

import com.userservice.domain.dto.NonRegisteredDto;
import com.userservice.domain.dto.ResponseClientDto;
import com.userservice.domain.dto.ResponseNonClientDto;
import com.userservice.domain.entity.Client;

import java.util.Optional;

public interface RegistrationService {
    Client registerNonClient(Client client);
    Optional<Client> checkRegistration(String mobilePhone);
    Client registerClient (Client client);
    String registrationClientInBank(Client client);
}
