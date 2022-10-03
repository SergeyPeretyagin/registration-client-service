package com.userservice.service;

import com.userservice.domain.dto.NotificationDto;
import com.userservice.domain.entity.Client;
import com.userservice.domain.entity.UserProfile;

import java.util.UUID;

public interface SettingService {
    UserProfile getNotificationSettings(UUID clientId);

    String changeSmsNotification(UUID clientId, Boolean notificationStatus);

    String changePushNotification(UUID clientId, Boolean notificationStatus);

    String changeEmailSubscription(UUID clientId, Boolean notificationStatus);

    String changePassword(UUID clientId, String oldPassword, String newPassword, String confirmNewPassword);

    String changeEmail(UUID clientId,String newEmail);

    String changeQA(UUID clientId, String question, String answer);

    Client getClientInformation(UUID clientId);
}
