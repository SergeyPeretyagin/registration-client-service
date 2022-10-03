package com.userservice.service;

import java.util.concurrent.CompletableFuture;

public interface VerificationSenderService {
    CompletableFuture<Boolean> sendCodeToEmail(String email);

    String sendCodeToPhone(String phone);
}
