package com.userservice.domain.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@Data
@Schema(description = "Update Password Dto")
public class UpdatePasswordDto {

    @Schema(description = "Old Password", example = "pass123PASS")
    @NotNull
    @Size(min = 6,max = 20)
    @Pattern(regexp = "^(?:(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])" +
            "|(?=.*[0-9])(?=.*[a-z])(?=.*[!\"#$%&'()*+,-./:;<=>?@\\[\\]^_`{|}~])" +
            "|(?=.*[a-z])(?=.*[A-Z])(?=.*[!\"#$%&'()*+,-./:;<=>?@\\[\\]^_`{|}~])" +
            "|(?=.*[0-9])(?=.*[A-Z])(?=.*[!\"#$%&'()*+,-./:;<=>?@\\[\\]^_`{|}~]))(?=[^а-яА-Я]).{6,20}")
    private final String oldPassword;

    @Schema(description = "New Password", example = "newPass12345")
    @NotNull
    @Size(min = 6,max = 20)
    @Pattern(regexp = "^(?:(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])" +
            "|(?=.*[0-9])(?=.*[a-z])(?=.*[!\"#$%&'()*+,-./:;<=>?@\\[\\]^_`{|}~])" +
            "|(?=.*[a-z])(?=.*[A-Z])(?=.*[!\"#$%&'()*+,-./:;<=>?@\\[\\]^_`{|}~])" +
            "|(?=.*[0-9])(?=.*[A-Z])(?=.*[!\"#$%&'()*+,-./:;<=>?@\\[\\]^_`{|}~]))(?=[^а-яА-Я]).{6,20}")
    private final String newPassword;

    @Schema(description = "Confirm New Password", example = "newPass12345")
    @NotNull
    @Size(min = 6,max = 20)
    @Pattern(regexp = "^(?:(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])" +
            "|(?=.*[0-9])(?=.*[a-z])(?=.*[!\"#$%&'()*+,-./:;<=>?@\\[\\]^_`{|}~])" +
            "|(?=.*[a-z])(?=.*[A-Z])(?=.*[!\"#$%&'()*+,-./:;<=>?@\\[\\]^_`{|}~])" +
            "|(?=.*[0-9])(?=.*[A-Z])(?=.*[!\"#$%&'()*+,-./:;<=>?@\\[\\]^_`{|}~]))(?=[^а-яА-Я]).{6,20}")
    private final String confirmNewPassword;

}
