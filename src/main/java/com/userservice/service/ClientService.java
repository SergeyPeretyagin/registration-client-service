package com.userservice.service;

import com.userservice.domain.entity.Client;
import com.userservice.domain.entity.ClientStatus;
import com.userservice.domain.entity.UserProfile;

import java.util.Optional;
import java.util.UUID;

public interface ClientService {
    Optional<Client> findById(UUID id);

    Client save(Client client);

    Optional<Client> findByMobilePhone(String mobilePhone);

    Optional<Client> findByPassportDataPassportNumber(String passportNumber);

    Optional<Client> findByPassportDataPassportNumberOrMobilePhone(String variable);

    Optional<Client> findByPassportDataPassportNumberOrMobilePhone(String passportNumber, String mobilePhone);

    void delete(Client client);
    Optional<Client> findByIdOrMobilePhone(UUID uuid, String mobilePhone);

   boolean existsClientByUUID(UUID uuid);

}
