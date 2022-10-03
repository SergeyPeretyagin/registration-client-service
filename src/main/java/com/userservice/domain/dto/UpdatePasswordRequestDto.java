package com.userservice.domain.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Update Password Request Dto")
public class UpdatePasswordRequestDto {

    @Schema(description = "Passport number", example = "GBN998871111")
    @NotNull
    @Pattern(regexp = "GB[RDOSPN]\\s?\\d{9}")
    private  String passportNumber;
    @Schema(description = "New password", example = "newPas1234")
    @NotNull
    @Pattern(regexp = "^(?:(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])" +
            "|(?=.*[0-9])(?=.*[a-z])(?=.*[!\"#$%&'()*+,-./:;<=>?@\\[\\]^_`{|}~])" +
            "|(?=.*[a-z])(?=.*[A-Z])(?=.*[!\"#$%&'()*+,-./:;<=>?@\\[\\]^_`{|}~])" +
            "|(?=.*[0-9])(?=.*[A-Z])(?=.*[!\"#$%&'()*+,-./:;<=>?@\\[\\]^_`{|}~]))(?=[^а-яА-Я]).{6,20}")
    private String newPassword;
    @Schema(description = "Confirm new password", example = "newPas1234")
    @NotNull
    @Pattern(regexp = "^(?:(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])" +
            "|(?=.*[0-9])(?=.*[a-z])(?=.*[!\"#$%&'()*+,-./:;<=>?@\\[\\]^_`{|}~])" +
            "|(?=.*[a-z])(?=.*[A-Z])(?=.*[!\"#$%&'()*+,-./:;<=>?@\\[\\]^_`{|}~])" +
            "|(?=.*[0-9])(?=.*[A-Z])(?=.*[!\"#$%&'()*+,-./:;<=>?@\\[\\]^_`{|}~]))(?=[^а-яА-Я]).{6,20}")
    private String confirmNewPassword;
}
