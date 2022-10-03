package com.userservice.domain.dto;

import com.userservice.domain.entity.ClientStatus;
import com.userservice.domain.entity.Role;
import lombok.Builder;
import lombok.Data;

import java.io.Serializable;
import java.util.Set;
import java.util.UUID;

@Data
@Builder
public class ClientDto implements Serializable {
    private final UUID id;
    private final String firstName;
    private final String passport;
    private final String password;
    private final Set<Role> roles;
    private final ClientStatus clientStatus;
    private final String mobilePhone;
}
