package com.userservice.service;

import com.userservice.domain.entity.UserProfile;

import java.util.Optional;
import java.util.UUID;

public interface UserProfileService {
    Optional<UserProfile> findByClientId(UUID id);

    UserProfile save(UserProfile userProfile);

    boolean existsByEmail(String email);

    int updateSmsNotification(Boolean notificationStatus, UUID clientId);

    int updatePushNotification(Boolean notificationStatus, UUID clientId);

    int updateEmailSubscription(Boolean notificationStatus, UUID clientId);

    int updatePassword(String password, UUID clientId);

    int updateEmail(String email, UUID clientId);

    int updateQA(String question, String answer, UUID clientId);


}
