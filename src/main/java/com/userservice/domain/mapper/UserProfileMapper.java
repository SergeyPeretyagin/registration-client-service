package com.userservice.domain.mapper;

import com.userservice.domain.dto.AuthDto;
import com.userservice.domain.dto.NotificationDto;
import com.userservice.domain.entity.UserProfile;

public interface UserProfileMapper {

    NotificationDto userProfileToNotificationDto(UserProfile userProfile);

    AuthDto responseUserProfileToAuthDto(UserProfile userProfile);
}
