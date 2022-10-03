package com.userservice.domain.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

import java.util.UUID;

@Data
@Builder
@Schema(description = "Auth Dto")
public class AuthDto {
    public AuthDto(UUID id, String password) {
        this.id = id;
        this.password = password;
    }
    @Schema(description = "Client id", example = "5db79fa7-cf26-4367-9064-60157a7e1ddb")
    private  UUID id;
    @Schema(description = "Client password", example = "GBO123465789" )
    private  String password;



}
