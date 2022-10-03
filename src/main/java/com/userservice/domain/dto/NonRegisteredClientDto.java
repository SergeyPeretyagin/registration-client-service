package com.userservice.domain.dto;

import com.userservice.domain.entity.ClientStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;

import java.util.UUID;

@Getter
//@Data
//@AllArgsConstructor
public class NonRegisteredClientDto extends  NonRegisteredDto{
    private final UUID id;

    public NonRegisteredClientDto(String mobilePhone, ClientStatus clientStatus,String message, UUID id) {
        super(mobilePhone, clientStatus, message);
        this.id = id;
    }
}
