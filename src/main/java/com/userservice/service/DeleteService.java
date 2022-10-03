package com.userservice.service;

import java.util.UUID;

public interface DeleteService {

    String deleteUserByUUID(UUID uuid);

    String deleteUserFromUserProfileDB(UUID uuid);
    String deleteUserByPhoneNumber(String phoneNumber);
}
