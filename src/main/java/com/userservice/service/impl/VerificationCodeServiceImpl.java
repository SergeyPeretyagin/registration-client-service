package com.userservice.service.impl;

import com.userservice.domain.dto.UserVerificationDto;
import com.userservice.domain.entity.Verification;
import com.userservice.domain.exception.BadRequestException;
import com.userservice.domain.exception.VerificationAcceptException;
import com.userservice.service.VerificationCodeService;
import com.userservice.service.VerificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import java.sql.Timestamp;
import java.time.Duration;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Random;
import java.util.concurrent.TimeUnit;
import java.util.function.Predicate;
import java.util.function.Supplier;

@Service
@RequiredArgsConstructor
public class VerificationCodeServiceImpl implements VerificationCodeService {
    private final VerificationService verificationService;

    @Value("${security.code-expiration}")
    private Long codeExpiration;
    Map<Predicate, Supplier<RuntimeException>> predicateSupplierMap = new HashMap<>();

    @Override
    public String generateVerificationCode(String receiver, String type) {
        Optional<Verification> optionalVerification = verificationService.findByReceiver(receiver);
        Timestamp codeExpiration = Timestamp.valueOf(
                LocalDateTime.now().plus(Duration.of(this.codeExpiration, ChronoUnit.MINUTES)));
        Verification verification;

        Integer random = new Random().nextInt(999999);
        String verificationCode = String.format("%06d", random);

        if (optionalVerification.isPresent()) {
            verification = optionalVerification.get();
            verification.setVerificationCode(verificationCode);
            verification.setCodeExpiration(codeExpiration);
        } else {
            verification = Verification.builder()
                    .receiver(receiver)
                    .verificationCode(verificationCode)
                    .codeExpiration(codeExpiration)
                    .type(type)
                    .build();
        }
        verificationService.save(verification);
        return verificationCode;
    }

    @Override
    public void checkingCodeToExpiredAndValid(Verification verification, UserVerificationDto userVerificationDto) {

        if (isDateExist(verification.getBlockExpiration())
                && isValidCode(verification.getVerificationCode(), userVerificationDto.getVerificationCode()))
            throw new BadRequestException("Code is expired");

        if (verification.getBlockExpiration() != null &&
                Duration.between(Instant.now(), verification.getBlockExpiration().toInstant()).getSeconds() < 0)
            verification.setBlockExpiration(null);

        if (LocalDateTime.now().isAfter(verification.getCodeExpiration().toLocalDateTime())
                && isValidCode(verification.getVerificationCode(), userVerificationDto.getVerificationCode()))
            throw new BadRequestException("Code is expired");

        if (verification.getBlockExpiration() != null)
            throw new VerificationAcceptException(String.valueOf(Duration
                    .between(Instant.now(), verification.getBlockExpiration().toInstant()).getSeconds())+true);
    }

    @Override
    public boolean isDateExistsAndAfter(Timestamp blockExpiration) {
        return blockExpiration!=null &&
                !LocalDateTime.now().isAfter(blockExpiration.toLocalDateTime());
    }
    @Override
    public boolean isValidCode(String verificationCode, String userVerificationCode){
        return verificationCode.equals(userVerificationCode);
    }
    @Override
    public boolean isDateExist(Timestamp blockExpiration){
        return blockExpiration!=null && LocalDateTime.now()
                .isAfter(blockExpiration.toLocalDateTime());
    }

    @Override
    public boolean isCodeExpired(Timestamp timestamp){
        return Duration.between(Instant.now(),timestamp.toInstant()).getSeconds()>0;
    }

    @Scheduled(fixedDelay = 600,timeUnit = TimeUnit.SECONDS)
    @Override
    public void deleteExpiredCode() {
        verificationService.deleteByCodeExpirationLessThanEqual();
    }

}
