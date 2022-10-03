package com.userservice.domain.mapper.impl;

import com.userservice.domain.dto.*;
import com.userservice.domain.entity.Client;
import com.userservice.domain.entity.PassportData;
import com.userservice.domain.entity.UserProfile;
import com.userservice.domain.mapper.ClientMapper;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class ClientMapperImpl implements ClientMapper {
    @Override
    public ClientDto clientToClientDto(Client client) {
        return ClientDto.builder()
                .id(client.getId())
                .firstName(client.getFirstName())
                .clientStatus(client.getClientStatus())
                .mobilePhone(client.getMobilePhone())
                .passport(client.getPassportData().getPassportNumber())
                .password(client.getUserProfile().getPassword())
                .roles(client.getUserProfile().getRoles())
                .build();
    }

    @Override
    public Client registerNonClientDtoToClient(RequestNonClientDto requestNonClientDto) {
        PassportData passportData = PassportData.builder()
                .passportNumber(requestNonClientDto.getPassportNumber())
                .build();
        UserProfile userProfile = UserProfile.builder()
                .securityQuestion(requestNonClientDto.getSecurityQuestion())
                .securityAnswer(requestNonClientDto.getSecurityAnswer())
                .password(requestNonClientDto.getPassword())
                .email(requestNonClientDto.getEmail())
                .build();
        return Client.builder()
                .firstName(requestNonClientDto.getFirstName())
                .middleName(requestNonClientDto.getMiddleName())
                .lastName(requestNonClientDto.getLastName())
                .passportData(passportData)
                .userProfile(userProfile)
                .mobilePhone(requestNonClientDto.getMobilePhone())
                .countryOfResidence(requestNonClientDto.getCountryOfResidence())
                .build();
    }

    @Override
    public ResponseNonClientDto clientToResponseNonClientDto(Client client) {
        return ResponseNonClientDto.builder()
                .id(client.getId())
                .smsNotification(client.getUserProfile().getSmsNotification())
                .pushNotification(client.getUserProfile().getPushNotification())
                .mobilePhone(client.getMobilePhone())
                .passwordEncoded(client.getUserProfile().getPassword())
                .firstName(client.getFirstName())
                .lastName(client.getLastName())
                .middleName(client.getMiddleName())
                .passportNumber(client.getPassportData().getPassportNumber())
                .securityQuestion(client.getUserProfile().getSecurityQuestion())
                .securityAnswer(client.getUserProfile().getSecurityAnswer())
                .email(client.getUserProfile().getEmail())
                .clientStatus(client.getClientStatus())
                .countryOfResidence(client.getCountryOfResidence())
                .appRegistrationDate(client.getUserProfile().getAppRegistrationDate())
                .accessionDate(client.getAccessionDate())
                .build();
    }


    @Override
    public Client requestClientDtoToClient(RequestClientDto requestClientDto) {
        UserProfile userProfile = UserProfile.builder()
                .securityQuestion(requestClientDto.getSecurityQuestion())
                .securityAnswer(requestClientDto.getSecurityAnswer())
                .password(requestClientDto.getPassword())
                .email(requestClientDto.getEmail())
                .build();
        return Client.builder()
                .userProfile(userProfile)
                .mobilePhone(requestClientDto.getMobilePhone())
                .build();
    }

    @Override
    public ResponseClientDto clientToResponseClientDto(Client client) {
        return ResponseClientDto.builder()
                .id(String.valueOf(client.getId()))
                .smsNotification(client.getUserProfile().getSmsNotification())
                .pushNotification(client.getUserProfile().getPushNotification())
                .mobilePhone(client.getMobilePhone())
                .password(client.getUserProfile().getPassword())
                .securityQuestion(client.getUserProfile().getSecurityQuestion())
                .securityAnswer(client.getUserProfile().getSecurityAnswer())
                .email(client.getUserProfile().getEmail())
                .clientStatus(String.valueOf(client.getClientStatus()))
                .dateAppRegistration(client.getUserProfile().getAppRegistrationDate())
                .build();
    }
}
