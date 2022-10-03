package com.userservice.controller;

import com.userservice.domain.dto.*;
import com.userservice.domain.entity.Client;
import com.userservice.domain.entity.UserProfile;
import com.userservice.domain.mapper.Mapper;
import com.userservice.service.ClientService;
import com.userservice.service.SettingService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.tags.Tags;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.UUID;

@Controller
@RequestMapping("auth/user/settings")
@RequiredArgsConstructor
@Log4j2
@CrossOrigin

@Tag(name="SettingsController", description="This controller allows you to change notification settings," +
        " as well as personal information")
public class SettingsController {

    private final SettingService settingService;
    private final Mapper mapper;

    @Operation(
            summary = "Get Notification Settings",
            description = "This method allows you to get information about the available notifications from the client. "+
                    "Please remove the prefix '/auth' in your requests"
    )
    @SecurityRequirement(name = "Bearer Authentication")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Ok",
                    content =  { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = NotificationDto.class))}),
            @ApiResponse(responseCode = "400", description = "Invalid data", content = @Content),
            @ApiResponse(responseCode = "401", description = "Token expired", content =  @Content),
            @ApiResponse(responseCode = "500", description = "Server Error", content = @Content)})
    @GetMapping("notifications/all")
    public ResponseEntity<NotificationDto> getNotificationSettings(@RequestParam UUID clientId) {
        log.info("SettingsController. GetMethod 'getNotificationSettings'. @RequestParam clientId: {}", clientId);
        UserProfile userProfile = settingService.getNotificationSettings(clientId);
        NotificationDto notificationDto = mapper.userProfileToNotificationDto(userProfile);
        log.info("SettingsController. Return from method 'getNotificationSettings': {}", notificationDto);
        return ResponseEntity.ok(notificationDto);
    }
    @Operation(
            summary = "Update Sms Notification",
            description = "This method allows you to change the sms notification settings. "+
                    "Please remove the prefix '/auth' in your requests"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Ok", content = @Content),
            @ApiResponse(responseCode = "400", description = "Invalid data", content = @Content),
            @ApiResponse(responseCode = "401", description = "Token expired", content = @Content),
            @ApiResponse(responseCode = "500", description = "Server Error", content = @Content)})
    @SecurityRequirement(name = "Bearer Authentication")
    @PatchMapping("notifications/sms")
    public ResponseEntity<HttpStatus> updateSmsNotification(@RequestParam UUID clientId,@Valid
                                                        @RequestBody NotificationRequestDto notificationStatus) {
        log.info("SettingsController. PatchMethod 'updateSmsNotification'. RequestParam clientId: {},  " +
                        "@RequestBody notificationStatus: {}", clientId, notificationStatus);
        String updateSmsNotification = settingService.changeSmsNotification
                (clientId, notificationStatus.getNotificationStatus());
        log.info("SettingsController. Return from method 'updateSmsNotification': {}", updateSmsNotification);
        return ResponseEntity.status(200).build();
    }

    @Operation(
            summary = "Update Push Notification",
            description = "This method allows you to change the push notification settings. "+
                    "Please remove the prefix '/auth' in your requests"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Ok", content = @Content),
            @ApiResponse(responseCode = "400", description = "Invalid data", content = @Content),
            @ApiResponse(responseCode = "401", description = "Token expired", content = @Content),
            @ApiResponse(responseCode = "500", description = "Server Error", content = @Content)})
    @SecurityRequirement(name = "Bearer Authentication")
    @PatchMapping("notifications/push")
    public ResponseEntity<String> updatePushNotification(@RequestParam UUID clientId,@Valid
                                                         @RequestBody NotificationRequestDto notificationStatus) {
        log.info("SettingsController. PatchMethod 'updatePushNotification'. @RequestParam clientId: {}," +
                " @RequestBody notificationStatus: {}", clientId, notificationStatus);
        String updatePushNotification = settingService.changePushNotification
                (clientId, notificationStatus.getNotificationStatus());
        log.info("SettingsController. Return from method 'updatePushNotification': {}", updatePushNotification);
        return ResponseEntity.ok().build();
    }

    @Operation(
            summary = "Update Push Notification",
            description = "This method allows you to change the email notification settings. "+
                    "Please remove the prefix '/auth' in your requests"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Operation completed successfully", content = @Content),
            @ApiResponse(responseCode = "400", description = "Invalid data", content = @Content),
            @ApiResponse(responseCode = "401", description = "Token expired", content = @Content),
            @ApiResponse(responseCode = "500", description = "Server Error", content = @Content)})
    @SecurityRequirement(name = "Bearer Authentication")
    @PatchMapping("notifications/email")
    public ResponseEntity<String> updateEmailSubscription(@RequestParam UUID clientId,@Valid
                                                          @RequestBody NotificationRequestDto notificationStatus) {
        log.info("SettingsController. PatchMethod 'updateEmailSubscription'. @RequestParam: {},"+
                " @RequestBody: {} ",clientId,notificationStatus);
        String updateEmailSubscription = settingService.changeEmailSubscription
                (clientId, notificationStatus.getNotificationStatus());
        log.info("SettingsController. Return from method 'updateEmailSubscription': {}", updateEmailSubscription);
        return ResponseEntity.ok(updateEmailSubscription);
    }

    @Operation(
            summary = "Update Password",
            description = "This method allows you to update password. "+
                    "Please remove the prefix '/auth' in your requests"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Operation completed successfully", content = @Content),
            @ApiResponse(responseCode = "400", description = "Invalid data/Wrong old password entered", content = @Content),
            @ApiResponse(responseCode = "401", description = "Token expired", content = @Content),
            @ApiResponse(responseCode = "409", description = "Passwords do not match", content = @Content),
            @ApiResponse(responseCode = "500", description = "Server Error", content = @Content)})
    @SecurityRequirement(name = "Bearer Authentication")
    @PatchMapping("password")
    public ResponseEntity<String> updatePassword(@RequestParam UUID clientId,@Valid
                                                 @RequestBody UpdatePasswordDto updatePassword) {
        log.info("SettingsController. PatchMethod 'updatePassword'. @RequestParam clientId: {},"+
                " @RequestBody @Valid updatePassword: {}",clientId,updatePassword);
        String strUpdatePassword = settingService.changePassword(
                clientId, updatePassword.getOldPassword(), updatePassword.getNewPassword(), updatePassword.getConfirmNewPassword());
        log.info("SettingsController. Return from method 'updatePassword': {} ",strUpdatePassword);
        return ResponseEntity.ok(strUpdatePassword);
    }

    @Operation(
            summary = "Update Email",
            description = "This method allows you to update email. "+
                    "Please remove the prefix '/auth' in your requests"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Operation completed successfully", content = @Content),
            @ApiResponse(responseCode = "400", description = "Invalid data", content = @Content),
            @ApiResponse(responseCode = "401", description = "Token expired", content = @Content),
            @ApiResponse(responseCode = "409", description = "Email already exists", content = @Content),
            @ApiResponse(responseCode = "500", description = "Server Error", content = @Content)})
    @SecurityRequirement(name = "Bearer Authentication")
    @PatchMapping(value = "email")
    public ResponseEntity<String> updateEmail(@RequestParam UUID clientId,@Valid
                                              @RequestBody UpdateEmailDto updateEmailDto) {
        log.info("SettingsController. PatchMethod 'updateEmail'. @RequestParam clientId: {}"+
                " @RequestBody @Valid updateEmailDto: {}", clientId, updateEmailDto);
        String updateEmail = settingService.changeEmail(clientId, updateEmailDto.getNewEmail());
        log.info("SettingsController. Return from method 'updateEmail': {}", updateEmail);
        return ResponseEntity.ok(updateEmail);
    }

    @Operation(
            summary = "Update Security Question and Answer",
            description = "This method allows you to update security question and answer. "+
                    "Please remove the prefix '/auth' in your requests"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Operation completed successfully", content = @Content),
            @ApiResponse(responseCode = "400", description = "Invalid data", content = @Content),
            @ApiResponse(responseCode = "401", description = "Token expired", content = @Content),
            @ApiResponse(responseCode = "500", description = "Server Error", content = @Content)})
    @SecurityRequirement(name = "Bearer Authentication")
    @PatchMapping("controls")
    public ResponseEntity<String> updateSecurityQA(@RequestParam UUID clientId,
                                                   @RequestBody @Valid UpdateQADto updateQADto) {
        log.info("SettingsController. PatchMethod 'updateSecurityQA'. @RequestParam clientId: {} "+
                 "@RequestBody @Valid updateQADto: {} ", clientId, updateQADto);
        String updatedSecurityQA = settingService.changeQA(
                clientId, updateQADto.getSecurityQuestion(), updateQADto.getSecurityAnswer());
        log.info("SettingsController. Return from method 'updateSecurityQA': {} ", updatedSecurityQA);
        return ResponseEntity.ok(updatedSecurityQA);
    }

    @Operation(
            summary = "Get Client Information",
            description = "In this method, you can get information about the client. "+
                    "Please remove the prefix '/auth' in your requests"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK", content = { @Content(mediaType = "application/json",
                    schema = @Schema(implementation = ClientInformationDto.class))}),
            @ApiResponse(responseCode = "400", description = "Invalid data", content = @Content),
            @ApiResponse(responseCode = "401", description = "Token expired", content = @Content),
            @ApiResponse(responseCode = "500", description = "Server Error", content = @Content)})
    @SecurityRequirement(name = "Bearer Authentication")
    @GetMapping("information/client")
    public ResponseEntity<ClientInformationDto> getClientInformation(@RequestParam UUID clientId) {
        log.info("SettingsController. GetMethod 'getClientInformation'. @RequestParam clientId: {}", clientId);
        Client client = settingService.getClientInformation(clientId);
        ClientInformationDto clientInformationDto = mapper.clientToInformationDto(client);
        log.info("SettingsController. Return from method 'clientInformationDto': {}", clientInformationDto);
        return ResponseEntity.ok(clientInformationDto);
    }


}
