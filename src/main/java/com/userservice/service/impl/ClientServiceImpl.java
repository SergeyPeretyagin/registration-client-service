package com.userservice.service.impl;

import com.userservice.domain.entity.Client;
import com.userservice.domain.entity.ClientStatus;
import com.userservice.domain.entity.UserProfile;
import com.userservice.repository.ClientRepository;
import com.userservice.service.ClientService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class ClientServiceImpl implements ClientService {
    private final ClientRepository clientRepository;

    @Override
    @Transactional(readOnly = true)
    public Optional<Client> findById(UUID id) {
        return clientRepository.findById(id);
    }

    @Override
    @Transactional
    public Client save(Client client) {
        return clientRepository.save(client);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Client> findByMobilePhone(String mobilePhone) {
        return clientRepository.findByMobilePhone(mobilePhone);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Client> findByPassportDataPassportNumber(String passportNumber) {
        return clientRepository.findByPassportData_PassportNumber(passportNumber);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Client> findByPassportDataPassportNumberOrMobilePhone(String variable) {
        return clientRepository.findByPassportData_PassportNumberOrMobilePhone(variable, variable);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Client> findByPassportDataPassportNumberOrMobilePhone(String passportNumber, String mobilePhone) {
        return clientRepository.findByPassportData_PassportNumberOrMobilePhone(passportNumber, mobilePhone);
    }

    @Override
    @Transactional
    public void delete(Client client) {
        clientRepository.delete(client);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Client> findByIdOrMobilePhone(UUID uuid, String mobilePhone) {
        return clientRepository.findByIdOrMobilePhone(uuid,mobilePhone);
    }
    @Override
    @Transactional(readOnly = true)
    public boolean existsClientByUUID(UUID uuid){
        log.info("existsClientByUUID {} ",uuid);
        return clientRepository.existsClientById(uuid);
    }
}
