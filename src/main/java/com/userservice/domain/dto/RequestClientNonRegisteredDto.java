package com.userservice.domain.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "Request Client Non Registered Dto")
public class RequestClientNonRegisteredDto {
    @Schema(description = "Mobile Phone", example = "441234567890")
    @NotNull
    @Pattern(regexp = "44\\d{10}")
    private String mobilePhone;

    @Schema(description = "First name", example = "Boris")
    @NotNull
    @Size(min = 2,max = 30)
    @Pattern(regexp = "^[\\p{L} \\-']+$")
    private String firstName;

    @Schema(description = "Last name", example = "Johnson")
    @NotNull
    @Size(min = 2,max = 30)
    @Pattern(regexp = "^[\\p{L} \\-']+$")
    private String lastName;

    @Schema(description = "Middle name. Maybe null", example = "Petrovich")
    @Size(max = 30)
    @Pattern(regexp = "^([\\p{L} \\-']).{1,30}+|$")
    private String middleName;

    @Schema(description = "Passport number", example = "GBO123456789")
    @NotNull
    @Pattern(regexp = "GB[RDOSPN]\\s?\\d{9}")
    private String passportNumber;

    @Schema(description = "countryOfResidence. Maybe only 'UK resident'", example = "UK resident")
    @NotNull
    @Pattern(regexp = "UK resident")
    private String countryOfResidence;
}
