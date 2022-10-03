package com.userservice.domain.dto;

import com.userservice.domain.entity.ClientStatus;
import lombok.Builder;
import lombok.Data;

import java.sql.Date;
import java.util.UUID;

@Data
@Builder
public class ResponseNonClientDto {
    private final UUID id;
    private final Boolean smsNotification;
    private final Boolean pushNotification;
    private final String mobilePhone;
    private final String passwordEncoded;
    private final String firstName;
    private final String lastName;
    private final String middleName;
    private final String passportNumber;
    private final String securityQuestion;
    private final String securityAnswer;
    private final String email;
    private final ClientStatus clientStatus;
    private final String countryOfResidence;
    private final Date appRegistrationDate;
    private final Date accessionDate;
}
