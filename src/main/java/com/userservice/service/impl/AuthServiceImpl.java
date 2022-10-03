package com.userservice.service.impl;

import com.userservice.domain.entity.UserProfile;
import com.userservice.domain.exception.BadRequestException;
import com.userservice.repository.UserProfileRepository;
import com.userservice.service.AuthService;
import com.userservice.service.ClientService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Log4j2
public class AuthServiceImpl implements AuthService {
    private final ClientService clientService;

    private final UserProfileRepository userProfileRepository;


    @Override
    @Transactional(readOnly = true)
    public UserProfile getUserProfileByPhoneNumberOrPassport(String login) {
          return clientService.findByPassportDataPassportNumberOrMobilePhone(login)
                .map(client -> userProfileRepository.findByClient_Id(client.getId()))
                  .orElseThrow(()->{
                    log.error("getUserIdAndPassword method : "+login+ " Invalid data");
                    return new BadRequestException("Login not found");
                }).orElseThrow(()->{
                      log.error("getUserIdAndPassword method : "+login+ " Invalid data");
                      return new BadRequestException("Login not found");
                  });
    }


}
