package com.userservice.service.impl;

import com.userservice.domain.entity.EnumRole;
import com.userservice.domain.entity.Role;
import com.userservice.repository.RoleRepository;
import com.userservice.service.RoleService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@RequiredArgsConstructor
public class RoleServiceImpl implements RoleService {
    private final RoleRepository roleRepository;
    @Override
    @Transactional(readOnly = true)
    public Role findByEnumRole(EnumRole enumRole) {
        return roleRepository.findByEnumRole(enumRole);
    }
}
