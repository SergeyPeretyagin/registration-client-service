package com.userservice.domain.mapper.impl;

import com.userservice.domain.dto.AuthDto;
import com.userservice.domain.dto.NotificationDto;
import com.userservice.domain.entity.UserProfile;
import com.userservice.domain.mapper.UserProfileMapper;
import org.springframework.stereotype.Component;

@Component
public class UserProfileMapperImpl implements UserProfileMapper {
    @Override
    public NotificationDto userProfileToNotificationDto(UserProfile userProfile) {
        return NotificationDto.builder()
                .email(userProfile.getEmail())
                .smsNotification(userProfile.getSmsNotification())
                .pushNotification(userProfile.getPushNotification())
                .emailSubscription(userProfile.getEmailSubscription())
                .build();
    }

    @Override
    public AuthDto responseUserProfileToAuthDto(UserProfile userProfile) {
        return AuthDto.builder()
                .id(userProfile.getClient().getId())
                .password(userProfile.getPassword())
                .build();
    }
}
