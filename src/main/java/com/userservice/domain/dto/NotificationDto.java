package com.userservice.domain.dto;

import com.fasterxml.jackson.annotation.*;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class NotificationDto {

    @JsonRawValue
    private final String email;
    private final Boolean smsNotification;
    private final Boolean pushNotification;
    private final Boolean emailSubscription;
}
