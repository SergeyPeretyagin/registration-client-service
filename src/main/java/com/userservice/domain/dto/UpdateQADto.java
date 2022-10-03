package com.userservice.domain.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@Data
@Schema(description = "Update QA Dto")
public class UpdateQADto {

    @Schema(description = "Security Question", example = "Name of my dog")
    @NotNull
    @Size(min = 5,max = 50)
    @Pattern(regexp = "^[\\p{L} #*+%[0-9]]+$")
    private final String securityQuestion;

    @Schema(description = "Security Answer", example = "Buddy")
    @NotNull
    @Size(min = 5,max = 50)
    @Pattern(regexp = "^[\\p{L} #*+%[0-9]]+$")
    private final String securityAnswer;
}
