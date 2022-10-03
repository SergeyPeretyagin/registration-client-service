package com.userservice.service.impl;

import com.userservice.domain.entity.Client;
import com.userservice.domain.entity.ClientStatus;
import com.userservice.domain.entity.EnumRole;
import com.userservice.domain.entity.UserProfile;
import com.userservice.domain.exception.BadRequestException;
import com.userservice.service.ConnectService;
import com.userservice.service.RegistrationService;
import com.userservice.service.ClientService;
import com.userservice.service.RoleService;
import com.userservice.utilies.ValidationUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.sql.Date;
import java.time.LocalDate;
import java.util.Optional;
import java.util.Set;


@Service
@RequiredArgsConstructor
@Log4j2
public class RegistrationServiceImpl implements RegistrationService {
    private final ClientService clientService;
    private final PasswordEncoder passwordEncoder;
    private final RoleService roleService;
    private final ConnectService connectService;

    @Override
    public Client registerNonClient(Client client) {
        clientService.findByPassportDataPassportNumberOrMobilePhone(
                client.getPassportData().getPassportNumber(), client.getMobilePhone())
                .ifPresent(this::throwBadRequestException);
                setupClient(client);
                clientService.save(client);
                return client;
    }

    @Override
    public Optional<Client> checkRegistration(String mobilePhone) {
        if(!ValidationUtil.checkMobileNumber(mobilePhone))
            throw new BadRequestException("Invalid data");

        Optional<Client> findClient = clientService.findByMobilePhone(mobilePhone);
        findClient.filter(Client.isActive().or(Client.isNotActive()))
                .ifPresent(this::throwBadRequestException);
        return findClient.filter(Client.isNotRegistered());
    }

    @Override
    public Client registerClient(Client client) {
        return clientService.findByMobilePhone(client.getMobilePhone())
                .map(existClient -> {
                    existClient.setUserProfile(client.getUserProfile());
                    setupClient(existClient);
                    return  clientService.save(existClient);
                }).orElseThrow(()->{
                    log.error("BadRequestException. User is not exists: {} ",client.getMobilePhone());
                    throw new BadRequestException("Invalid data");
                });
    }

    @Override
    public String registrationClientInBank(Client client) {
       clientService.findByMobilePhone(client.getMobilePhone())
                .ifPresentOrElse(this::throwBadRequestException,()->{
                    client.setClientStatus(ClientStatus.NOT_REGISTERED);
                    clientService.save(client);
                });
        return "Operation complete successfully";
    }

    private void setupClient(Client client) {
        UserProfile userProfile = client.getUserProfile();
        userProfile.setPassword(passwordEncoder.encode(userProfile.getPassword()));
        userProfile.setRoles(Set.of(roleService.findByEnumRole(EnumRole.ROLE_USER)));
        userProfile.setPushNotification(true);
        userProfile.setSmsNotification(true);
        userProfile.setEmailSubscription(userProfile.getEmail() != null);
        userProfile.setAppRegistrationDate(Date.valueOf(LocalDate.now()));
        userProfile.setClient(client);
        boolean isCardsExist = true;
        if(client.getId()!=null){
            isCardsExist  = connectService.getListCardsByClientId(client.getId()).isEmpty();
        }
        log.info("Client dont have cards {}, {}",isCardsExist, client.getId());
        if (isCardsExist){
            client.setClientStatus(ClientStatus.NOT_ACTIVE);
        }else {
            client.setClientStatus(ClientStatus.ACTIVE);
        }
        client.setAccessionDate(Date.valueOf(LocalDate.now()));
        client.setUserProfile(userProfile);
    }

    private void throwBadRequestException(Client client){
        log.error("BadRequestException.{} User is already exists: {},{} ",
                client.getMobilePhone(), client.getPassportData(), client.getMobilePhone());
        throw new BadRequestException("Invalid data");
    }


}
