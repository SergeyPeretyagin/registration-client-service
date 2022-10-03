package com.userservice.repository;

import com.userservice.domain.entity.EnumRole;
import com.userservice.domain.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface RoleRepository extends JpaRepository<Role, UUID> {
    Role findByEnumRole(EnumRole enumRole);

}