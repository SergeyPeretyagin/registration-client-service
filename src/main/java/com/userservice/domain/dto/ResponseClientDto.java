package com.userservice.domain.dto;

import lombok.Builder;
import lombok.Data;
import lombok.Value;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.util.Date;
import java.util.UUID;

@Data
@Builder
public class ResponseClientDto {

    private final String id;
    private final Boolean smsNotification;
    private final Boolean pushNotification;
    private final String mobilePhone;
    private final String password;
    private final String securityQuestion;
    private final String securityAnswer;
    private final String email;
    private final String clientStatus;
    private final Date dateAppRegistration;

}
