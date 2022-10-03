package com.userservice.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PhoneNumberDto {
    @NotNull
    @Pattern(regexp = "44\\d{10}")
    private String phoneNumber;
}
