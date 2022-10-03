package com.userservice.domain.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ClientInformationDto {
    String countryOfResidence;
    String email;
    String firstName;
    String lastName;
    String middleName;
    String mobilePhone;
    String passportNumber;

}
