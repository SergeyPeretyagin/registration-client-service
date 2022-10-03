package com.userservice.service;

import com.userservice.domain.entity.EnumRole;
import com.userservice.domain.entity.Role;

public interface RoleService {
    Role findByEnumRole(EnumRole enumRole);
}
