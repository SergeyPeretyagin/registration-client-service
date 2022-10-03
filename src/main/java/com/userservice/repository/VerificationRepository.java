package com.userservice.repository;

import com.userservice.domain.entity.Verification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.sql.Timestamp;
import java.util.Optional;
import java.util.UUID;

public interface VerificationRepository extends JpaRepository<Verification, UUID> {
    Optional<Verification> findByReceiverIgnoreCase(String receiver);

    @Modifying
    @Query
            ("delete from Verification v " +
                    "where v.codeExpiration < :codeExpiration and" +
                    " (v.blockExpiration <= :codeExpiration or v.blockExpiration is null )")
    int deleteByCodeExpirationLessThanEqual(Timestamp codeExpiration);

    void deleteByReceiver(String receiver);

}