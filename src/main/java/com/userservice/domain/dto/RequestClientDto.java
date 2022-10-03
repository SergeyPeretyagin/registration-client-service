package com.userservice.domain.dto;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

import javax.annotation.Nullable;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.util.UUID;

@Data
@Builder
@Schema(description = "Request Client Dto")
public class RequestClientDto {
    @Schema(description = "Phone number", example = "441234567890")
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
    @Schema(description = "Security Question", example = "Name of my dog")
    @NotNull
    @Size(min = 5,max = 50)
    @Pattern(regexp = "^[\\p{L} .?[0-9]]+$")
    private final String securityQuestion;
    @Schema(description = "Security Answer", example = "Buddy")
    @NotNull
    @Size(min = 5,max = 50)
    @Pattern(regexp = "^[\\p{L} .?[0-9]]+$")
    private final String securityAnswer;
    @Schema(description = "Email. Maybe null or '' ", example = "buddy@gmail.com")
    @Builder.Default
    @Size(max = 50)
    @Nullable
    @Pattern(regexp = "^(?=.{1,50}@)[A-Za-z0-9_-]+(\\.[A-Za-z0-9_-]+)*@[^-][A-Za-z0-9-]+(\\.[A-Za-z0-9-]+)*(\\.[A-Za-z]{2,})|$")
    private final String email = null;

}
