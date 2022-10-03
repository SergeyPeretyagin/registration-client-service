package com.userservice.domain.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

import javax.annotation.Nullable;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@Data
@Builder
@Schema(description = "Request Non Client Dto")
public class RequestNonClientDto {
    @Schema(description = "Mobile Phone", example = "441234567890")
    @NotNull
    @Pattern(regexp = "44\\d{10}")
    private final String mobilePhone;

    @Schema(description = "Password", example = "pass123PASS")
    @NotNull
    @Size(min = 6,max = 20)
    @Pattern(regexp = "^(?:(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])" +
            "|(?=.*[0-9])(?=.*[a-z])(?=.*[!\"#$%&'()*+,-./:;<=>?@\\[\\]^_`{|}~])" +
            "|(?=.*[a-z])(?=.*[A-Z])(?=.*[!\"#$%&'()*+,-./:;<=>?@\\[\\]^_`{|}~])" +
            "|(?=.*[0-9])(?=.*[A-Z])(?=.*[!\"#$%&'()*+,-./:;<=>?@\\[\\]^_`{|}~]))(?=[^а-яА-Я]).{6,20}")
    private final String password;

    @Schema(description = "Security Question", example = "The best party")
    @NotNull
    @Size(min = 5,max = 50)
    @Pattern(regexp = "^[\\p{L} .?[0-9]]+$")
    private final String securityQuestion;

    @Schema(description = "Security Answer", example = "The covid party")
    @NotNull
    @Size(min = 5,max = 50)
    @Pattern(regexp = "^[\\p{L} .?[0-9]]+$")
    private final String securityAnswer;

    @Schema(description = "Email. Maybe null or '' ", example = "buddy@gmail.com")
    @Builder.Default
    @Size(max = 50)
    @Pattern(regexp = "^(?=.{1,50}@)[A-Za-z0-9_-]+(\\.[A-Za-z0-9_-]+)*@[^-][A-Za-z0-9-]+(\\.[A-Za-z0-9-]+)*(\\.[A-Za-z]{2,})|$")
    @Nullable
    private String email = null;

    @Schema(description = "First name", example = "Boris")
    @NotNull
    @Size(min = 2,max = 30)
    @Pattern(regexp = "^[\\p{L} \\-']+$")
    private final String firstName;

    @Schema(description = "Last name", example = "Johnson")
    @NotNull
    @Size(min = 2,max = 30)
    @Pattern(regexp = "^[\\p{L} \\-']+$")
    private final String lastName;

    @Schema(description = "Middle name. Maybe null", example = "Petrovich")
    @Builder.Default
    @Size(max = 30)
    @Pattern(regexp = "^([\\p{L} \\-']).{1,30}+|$")
    private final String middleName = null;

    @Schema(description = "Passport number", example = "GBO123456789")
    @NotNull
    @Pattern(regexp = "GB[RDOSPN]\\s?\\d{9}")
    private final String passportNumber;

    @Schema(description = "countryOfResidence. Maybe only 'UK resident'", example = "UK resident")
    @NotNull
    @Pattern(regexp = "UK resident")
    private final String countryOfResidence;
}
