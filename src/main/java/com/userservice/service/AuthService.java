package com.userservice.service;

import com.userservice.domain.entity.UserProfile;

import java.util.UUID;

public interface AuthService {
    UserProfile getUserProfileByPhoneNumberOrPassport(String mobilePhone);
}
