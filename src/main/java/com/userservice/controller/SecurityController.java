package com.userservice.controller;

import com.userservice.domain.dto.NonRegisteredDto;
import com.userservice.domain.dto.PassportNumberRequestDto;
import com.userservice.domain.dto.ResponseVerificationDto;
import com.userservice.domain.dto.UserVerificationDto;
import com.userservice.service.SecurityService;
import com.userservice.service.VerificationSenderService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Controller
@RequestMapping("security")
@RequiredArgsConstructor
@Log4j2
@CrossOrigin
@Tag(name="SecurityController", description="In this controller, " +
        "you can get a verification code, as well as check it")
public class SecurityController {
    private final SecurityService securityService;
    private final VerificationSenderService verificationSenderService;

    @Operation(
            summary = "Get Phone By Passport Number",
            description = "This method gives you a phone number based on your passport"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Return mobile phone",
                    content =  { @Content(mediaType = "text/plain",
                            schema = @Schema(implementation = String.class))}),
            @ApiResponse(responseCode = "400", description = "Invalid data",
                    content = @Content)})
    @PostMapping("session")
    public ResponseEntity<String> getPhoneByPassportNumber(@RequestBody @Valid PassportNumberRequestDto passportNumber) {
        log.info("SecurityController. PostMethod 'getPhoneByPassport'. @RequestBody passportNumber: {}",
                passportNumber.getPassportNumber());
        String phoneByPassport = securityService.getPhoneByPassport(passportNumber.getPassportNumber());
        log.info("RegistrationController. Return from method 'getPhoneByPassport': {} ", phoneByPassport);
        return ResponseEntity.ok(phoneByPassport);
    }

    @Operation(
            summary = "Get VerificationCode",
            description = "This method outputs an sms code"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Ok",
                    content =  @Content),
            @ApiResponse(responseCode = "400", description = "Invalid data", content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ResponseVerificationDto.class))} ),
            @ApiResponse(responseCode = "500", description = "Server Error",
                    content = @Content)})
    @GetMapping("/session")
    public ResponseEntity<String> getVerificationCode(@RequestParam String receiver) {
            log.info("SecurityController. GetMethod 'getVerificationCode'. @RequestParam {}", receiver );
            return ResponseEntity.ok(securityService.sendCodeToPhone(receiver));
    }
    @Operation(
            summary = "User Verification",
            description = "This method verification an sms code"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Operation completed successfully",
                    content =  @Content),
            @ApiResponse(responseCode = "400", description = "Invalid data", content = { @Content(mediaType = "application/json",
                    schema = @Schema(implementation = UserVerificationDto.class))} ),
            @ApiResponse(responseCode = "406", description = "Server Error",
                     content = { @Content(mediaType = "application/json",
                    schema = @Schema(implementation = ResponseVerificationDto.class))})})
    @PostMapping("session/verification")
    public ResponseEntity<String> userVerification(@Valid @RequestBody UserVerificationDto userVerificationDto) {
        log.info("SecurityController. PostMethod 'userVerification'. @RequestParam userVerificationDto: {}" ,userVerificationDto);
        String userVerification = securityService.verify(userVerificationDto);
        log.info("RegistrationController. Return from method 'userVerification': {}", userVerification);
        return ResponseEntity.ok().body(userVerification);
    }

}
