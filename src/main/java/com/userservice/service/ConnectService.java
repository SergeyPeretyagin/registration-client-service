package com.userservice.service;

import java.util.List;
import java.util.UUID;

public interface ConnectService {
    List getListCardsByClientId(UUID clientId);
}
