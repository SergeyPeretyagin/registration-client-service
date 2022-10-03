package com.userservice.service.impl;

import com.userservice.domain.entity.Verification;
import com.userservice.repository.VerificationRepository;
import com.userservice.service.VerificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class VerificationServiceImpl implements VerificationService {
    private final VerificationRepository verificationRepository;

    @Override
    @Transactional
    public Verification save(Verification verification) {
        return verificationRepository.save(verification);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Verification> findByReceiver(String receiver) {
        return verificationRepository.findByReceiverIgnoreCase(receiver);
    }

    @Transactional
    public void deleteVerification(String receiver){
        verificationRepository.deleteByReceiver(receiver);
    }

    @Override
    @Transactional
    public int deleteByCodeExpirationLessThanEqual() {
        return verificationRepository.deleteByCodeExpirationLessThanEqual(Timestamp.valueOf(LocalDateTime.now()));
    }
}
