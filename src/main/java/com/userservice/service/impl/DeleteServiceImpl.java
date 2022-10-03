package com.userservice.service.impl;

import com.userservice.domain.entity.Client;
import com.userservice.domain.exception.InternalServerException;
import com.userservice.domain.exception.NotExistsUserException;
import com.userservice.repository.ClientRepository;
import com.userservice.repository.UserProfileRepository;
import com.userservice.service.ClientService;
import com.userservice.service.DeleteService;
import com.userservice.service.UserProfileService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.UUID;
@Service
@RequiredArgsConstructor
@Slf4j
public class DeleteServiceImpl implements DeleteService {

    private final ClientRepository clientRepository;
    private final UserProfileRepository userProfileRepository;

    @Override
    @Transactional
    public String deleteUserByUUID(UUID id) {
        return clientRepository.findById(id).map(client -> {
            clientRepository.deleteClientByPassportData(client.getPassportData());
            return "User account deleted successfully";
        }).orElseThrow(()->{
            log.error("InternalServerException. User profile not found. clientId: "+id);
            throw new NotExistsUserException("This user does not exist");
        });
    }
    @Override
    @Transactional
    public String deleteUserByPhoneNumber(String phoneNumber) {
        return clientRepository.findByMobilePhone(phoneNumber).map(client -> {
            clientRepository.deleteClientByPassportData(client.getPassportData());
            return "User account deleted successfully";
        }).orElseThrow(()->{
            log.error("InternalServerException. User profile not found. clientId: "+phoneNumber);
            throw new NotExistsUserException("This user does not exist");
        });
    }

    @Override
    @Transactional
    public String deleteUserFromUserProfileDB(UUID uuid) {
        userProfileRepository.deleteById(uuid);
        return "ok";
    }
}
