package com.userservice.domain.entity;

import org.springframework.security.core.GrantedAuthority;

public enum EnumRole implements GrantedAuthority {
    ROLE_USER,
    ROLE_ADMIN,
    ROLE_DEMO,
    ROLE_UNAUTHORIZED,
    ROLE_UNVERIFIED,
    ROLE_OPERATOR;

    @Override
    public String getAuthority() {
        return name();
    }
}
