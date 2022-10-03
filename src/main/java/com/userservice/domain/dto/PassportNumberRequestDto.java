package com.userservice.domain.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "Passport Number Request Dto")
public class PassportNumberRequestDto {

    @Schema(description = "Passport number", example = "GBO123456789")
    @NotNull
    @Pattern(regexp = "GB[RDOSPN]\\s?\\d{9}")
    private String passportNumber;
}
