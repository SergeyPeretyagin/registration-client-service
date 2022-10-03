package com.userservice.repository;

import com.userservice.domain.entity.UserProfile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.Optional;
import java.util.UUID;

public interface UserProfileRepository extends JpaRepository<UserProfile, UUID> {
    Optional<UserProfile> findByClient_Id(UUID id);

    boolean existsByEmail(String email);

    @Modifying
    @Query("update UserProfile u set u.smsNotification = ?1 where u.client.id = ?2")
    int updateSmsNotification(Boolean notificationStatus, UUID clientId);

    @Modifying
    @Query("update UserProfile u set u.pushNotification = ?1 where u.client.id = ?2")
    int updatePushNotification(Boolean notificationStatus, UUID clientId);

    @Modifying
    @Query("update UserProfile u set u.emailSubscription = ?1 where u.client.id = ?2")
    int updateEmailSubscription(Boolean notificationStatus, UUID clientId);

    @Modifying
    @Query("update UserProfile u set u.password = ?1 where u.client.id = ?2")
    int updatePassword(String password, UUID clientId);

    @Modifying
    @Query("update UserProfile u set u.email = ?1 where u.client.id = ?2")
    int updateEmail(String email, UUID clientId);

    @Modifying
    @Query("update UserProfile u set u.securityQuestion = :question, u.securityAnswer = :answer where u.client.id = :clientId")
    int updateQA(@Param("question") String question, @Param("answer") String answer, @Param("clientId") UUID clientId);
}