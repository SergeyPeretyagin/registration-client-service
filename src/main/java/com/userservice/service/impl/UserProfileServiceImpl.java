package com.userservice.service.impl;

import com.userservice.domain.entity.UserProfile;
import com.userservice.repository.UserProfileRepository;
import com.userservice.service.UserProfileService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserProfileServiceImpl implements UserProfileService {
    private final UserProfileRepository userProfileRepository;

    @Override
    @Transactional(readOnly = true)
    public boolean existsByEmail(String email) {
        return userProfileRepository.existsByEmail(email);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<UserProfile> findByClientId(UUID id) {
        return userProfileRepository.findByClient_Id(id);
    }

    @Override
    @Transactional
    public UserProfile save(UserProfile userProfile) {
        return userProfileRepository.save(userProfile);
    }

    @Override
    @Transactional
    public int updateSmsNotification(Boolean notificationStatus, UUID clientId) {
        return userProfileRepository.updateSmsNotification(notificationStatus, clientId);
    }

    @Override
    @Transactional
    public int updatePushNotification(Boolean notificationStatus, UUID clientId) {
        return userProfileRepository.updatePushNotification(notificationStatus, clientId);
    }

    @Override
    @Transactional
    public int updateEmailSubscription(Boolean notificationStatus, UUID clientId) {
        return userProfileRepository.updateEmailSubscription(notificationStatus, clientId);
    }

    @Override
    @Transactional
    public int updatePassword(String password, UUID clientId) {
        return userProfileRepository.updatePassword(password, clientId);
    }

    @Override
    @Transactional
    public int updateEmail(String email, UUID clientId) {
        return userProfileRepository.updateEmail(email, clientId);
    }

    @Override
    @Transactional
    public int updateQA(String question, String answer, UUID clientId) {

        return userProfileRepository.updateQA(question, answer, clientId);
    }
}
