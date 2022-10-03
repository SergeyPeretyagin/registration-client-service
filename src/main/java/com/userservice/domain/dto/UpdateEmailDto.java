package com.userservice.domain.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@Data
@Schema(description = "Update Email Dto")
@NoArgsConstructor
@AllArgsConstructor
public class UpdateEmailDto {

    @Schema(description = "New Password", example = "newemail@gmail.com")
//    @Email
    @Size(max = 50)
//    @Pattern(regexp = "^(?=.{1,50}@)[A-Za-z0-9_-]+(\\.[A-Za-z0-9_-]+)*@[^-][A-Za-z0-9-]+(\\.[A-Za-z0-9-]+)*(\\.[A-Za-z]{2,})")
    @Email
    private String newEmail;

}
