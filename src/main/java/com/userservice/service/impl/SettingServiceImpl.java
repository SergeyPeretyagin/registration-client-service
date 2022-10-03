package com.userservice.service.impl;

import com.userservice.domain.entity.Client;
import com.userservice.domain.entity.UserProfile;
import com.userservice.domain.exception.BadRequestException;
import com.userservice.domain.exception.EmailExistsException;
import com.userservice.domain.exception.InternalServerException;
import com.userservice.domain.exception.WrongPasswordException;
import com.userservice.service.ClientService;
import com.userservice.service.SettingService;
import com.userservice.service.UserProfileService;
import com.userservice.service.VerificationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Log4j2
public class SettingServiceImpl implements SettingService {
    private final UserProfileService userProfileService;
    private final PasswordEncoder passwordEncoder;
    private final ClientService clientService;

    @Override
    public UserProfile getNotificationSettings(UUID clientId) {
        UserProfile userProfile = userProfileService.findByClientId(clientId).orElseThrow(
                () -> {
                    log.error("InternalServerException. User profile not found. clientId: {} ", clientId);
                    return new InternalServerException("User profile not found");
                });
        return userProfile;
    }

    @Override
    public String changeSmsNotification(UUID clientId, Boolean notificationStatus) {
        userProfileService.updateSmsNotification(notificationStatus, clientId);
        return "SMS-notification has been successfully updated";
    }

    @Override
    public String changePushNotification(UUID clientId, Boolean notificationStatus) {
        userProfileService.updatePushNotification(notificationStatus, clientId);
        return "PUSH-notification has been successfully updated";
    }

    @Override
    public String changeEmailSubscription(UUID clientId, Boolean notificationStatus) {
        UserProfile userProfile = userProfileService.findByClientId(clientId).orElseThrow(() -> {
            log.error("UsernameNotFoundException. Client not found. clientId: {} ", clientId);
            return new UsernameNotFoundException("UNAUTHORIZED");
        });
        if (userProfile.getEmail() != null) {
            userProfileService.updateEmailSubscription(notificationStatus, clientId);
            return "Operation completed successfully";
        }
        return "Operation completed successfully";
    }

    @Override
    public String changePassword(UUID clientId, String oldPassword, String newPassword, String confirmNewPassword) {
        if (!newPassword.equals(confirmNewPassword)){
            throw new WrongPasswordException("Passwords do not match");
        }
        UserProfile userProfile = userProfileService.findByClientId(clientId)
                .orElseThrow(() -> {
                    log.error("UsernameNotFoundException. Client not found. clientId:{}, {} ", clientId+
                            " password: {}"+ oldPassword+ " newPassword: {} ", newPassword);
                    return new BadRequestException("Invalid data");
                });
        if (passwordEncoder.matches(oldPassword, userProfile.getPassword())) {
            userProfileService.updatePassword(passwordEncoder.encode(newPassword), clientId);
            return "Operation completed successfully";
        } else {
            log.error("BadRequestException. Incorrect password. Not matches: {}, {}",
                    oldPassword, userProfile.getPassword());
            throw new BadRequestException("Wrong old password entered");
        }
    }

    @Override
    public String changeEmail(UUID clientId,String newEmail) {
        if(!userProfileService.existsByEmail(newEmail)){
            userProfileService.updateEmail(newEmail, clientId);
            return "Email has been successfully updated";
        } else {
            log.error("Email already exists");
            throw new EmailExistsException("Email already exists");
        }
    }

    @Override
    public String changeQA(UUID clientId, String question, String answer) {
        userProfileService.updateQA(question, answer, clientId);
        return "Operation completed successfully";
    }

    @Override
    public Client getClientInformation(UUID clientId) {
        return clientService.findById(clientId).orElseThrow(()->{
            log.error("BadRequestException from SettingService class, method getClientInformation."+
                    " Incorrect clientId: {} ",clientId);
            return new BadRequestException("Invalid data");
        });
    }
}
