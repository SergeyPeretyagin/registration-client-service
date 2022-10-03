package com.userservice.domain.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jdk.jfr.BooleanFlag;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import javax.validation.constraints.NotNull;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Notification Request Dto")
public class NotificationRequestDto {


    @Schema(description = "Notification Status", example = "true")
    @NotNull
    @BooleanFlag
    private Boolean notificationStatus;
}
