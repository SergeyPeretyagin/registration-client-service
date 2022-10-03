package com.userservice.domain.dto;

import com.userservice.domain.entity.ClientStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.Pattern;


@Data
@AllArgsConstructor
public class NonRegisteredDto {
    private final String mobilePhone;
    private final ClientStatus clientStatus;
    private final String message;
}
