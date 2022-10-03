package com.userservice.service.impl;

import com.userservice.domain.entity.Client;
import com.userservice.domain.entity.UserProfile;
import com.userservice.domain.exception.BadRequestException;
import com.userservice.domain.exception.WrongPasswordException;
import com.userservice.service.ClientService;
import com.userservice.service.LoginService;
import com.userservice.service.UserProfileService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
@RequiredArgsConstructor
@Log4j2
public class LoginServiceImpl implements LoginService {
    private final ClientService clientService;
    private final UserProfileService userProfileService;
    private final PasswordEncoder passwordEncoder;


    @Override
    @Transactional
    public String updatePassword(String passportNumber, String newPassword, String confirmNewPassword) {
        if (!newPassword.equals(confirmNewPassword)){
            throw new WrongPasswordException("Passwords do not match");
        }
        Client client = clientService.findByPassportDataPassportNumber(passportNumber)
                .orElseThrow(() -> {
                    log.error("UsernameNotFoundException. Incorrect passport number:{} ", passportNumber);
                    return new BadRequestException("Invalid data");
                }
        );
        UserProfile userProfile = userProfileService.findByClientId(client.getId())
                .orElseThrow(() -> {
                    log.error("BadRequestException. Client not found. clientId:{} ", client.getId());
                    return new BadRequestException("Invalid data");
                });
        if(!passwordEncoder.matches(newPassword, userProfile.getPassword())){
                userProfileService.updatePassword(passwordEncoder.encode(newPassword), client.getId());
                return "Operation completed successfully";

        }else {
            log.error("BadRequestException. Old and new password matches:{},{}",
                    newPassword,userProfile.getPassword());
            throw new BadRequestException("Old and new password matches");
        }
    }

}
