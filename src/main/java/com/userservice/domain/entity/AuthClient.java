package com.userservice.domain.entity;

import com.userservice.domain.dto.ClientDto;
import lombok.Getter;
import org.springframework.security.core.userdetails.User;

import java.util.Collection;
import java.util.UUID;
import java.util.stream.Collectors;

@Getter
public class AuthClient extends User {
    private final UUID id;
    private final String passportDataPassportNumber;
    private final ClientStatus clientStatus;
    private final String mobilePhone;

    public AuthClient(String username, String password, Collection<EnumRole> roles, UUID id,
                      String passportDataPassportNumber, ClientStatus clientStatus, String mobilePhone) {
        super(username, password, roles);
        this.id = id;
        this.passportDataPassportNumber = passportDataPassportNumber;
        this.clientStatus = clientStatus;
        this.mobilePhone = mobilePhone;
    }

    public AuthClient(ClientDto clientDto) {
        super(clientDto.getFirstName(), clientDto.getPassword(),
                clientDto.getRoles().stream().map(Role::getEnumRole).collect(Collectors.toList()));
        this.id = clientDto.getId();
        this.passportDataPassportNumber = clientDto.getPassport();
        this.clientStatus = clientDto.getClientStatus();
        this.mobilePhone = clientDto.getMobilePhone();
    }
}
