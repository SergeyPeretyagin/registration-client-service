package com.userservice.service;

import com.userservice.domain.dto.TokenDto;

import java.util.UUID;

public interface LoginService {

    String updatePassword(String passportNumber, String newPassword, String confirmNewPassword);

}
