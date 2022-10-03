package com.userservice.service;

import com.userservice.domain.entity.Verification;

import java.util.Optional;

public interface VerificationService {
    Verification save(Verification verification);

    Optional<Verification> findByReceiver(String receiver);

    void deleteVerification(String receiver);

    int deleteByCodeExpirationLessThanEqual();
}
