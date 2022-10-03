package com.userservice.domain.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;

import javax.validation.constraints.Pattern;

@Data
@AllArgsConstructor
@Schema(description = "User Verification Dto")
public class UserVerificationDto {

    @Schema(description = "Mobile Phone", example = "441234567890")
    @Pattern(regexp = "44\\d{10}")
    private  String receiver;


    @Schema(description = "Verification Code", example = "123456")
    @Pattern(regexp = "\\d{6}")
    private  String verificationCode;

}
