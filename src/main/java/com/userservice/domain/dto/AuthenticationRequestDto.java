package com.userservice.domain.dto;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonDeserialize(as = AuthenticationRequestDto.class)
@Schema(description = "Authentication Request Dto")
public class AuthenticationRequestDto {
    @Schema(description = "Phone number or passport number", example = "GBN998871111")
    private String login;
    @Schema(description = "Password", example = "123QWEqwe")
    private String password;
}
