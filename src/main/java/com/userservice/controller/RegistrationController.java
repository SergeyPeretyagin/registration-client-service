package com.userservice.controller;

import com.userservice.domain.dto.*;
import com.userservice.domain.entity.Client;
import com.userservice.domain.entity.ClientStatus;
import com.userservice.domain.mapper.ClientMapper;
import com.userservice.domain.mapper.MapperToClient;
import com.userservice.service.RegistrationService;
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
import java.util.Optional;

@Controller
@RequestMapping("registration")
@RequiredArgsConstructor
@Log4j2
@CrossOrigin
@Tag(name="RegistrationController", description="This controller allows you to check the registration of the client, " +
        "as well as register in the application")
public class RegistrationController {
    private final RegistrationService registrationService;
    private final ClientMapper clientMapper;

    private final MapperToClient mapperToClient;
    @Operation(
            summary = "The visitor becomes a customer of the bank",
            description = "This method registers the visitor in the data base"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Operation complete successfully",
                    content =  @Content),
            @ApiResponse(responseCode = "400", description = "User already exists",
                    content = @Content),
            @ApiResponse(responseCode = "500", description = "Server Error",
                    content = @Content)})
    @PostMapping
    public ResponseEntity<String> registration(@Valid @RequestBody RequestClientNonRegisteredDto nonRegisteredDto){
        log.info("RegistrationController. PostMethod 'becomesClientOfBank'{}", nonRegisteredDto);
        Client client = mapperToClient.reguestClientNonRegisteredDtoToClient(nonRegisteredDto);
        String answer = registrationService.registrationClientInBank(client);
        log.info("RegistrationController. PostMethod 'becomesClientOfBank'. Operation completed successfully " +
                        "the new client of bank {}", client);
        return ResponseEntity.ok().body(answer);
    }

    @Operation(
            summary = "Sign Up Client",
            description = "This method registers the client in the application"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Operation complete successfully",
                    content =  @Content),
            @ApiResponse(responseCode = "400", description = "Invalid data",
                    content = @Content),
            @ApiResponse(responseCode = "500", description = "Registration failed",
                    content = @Content)})
    @PatchMapping("user-profile")
    public ResponseEntity<String> signUp (@Valid @RequestBody RequestClientDto request) {
        log.info("RegistrationController. PatchMethod 'signUp' {} ", request);
        Client client = registrationService.registerClient(clientMapper.requestClientDtoToClient(request));
        log.info("RegistrationController. PatchMethod 'signUp'. Operation completed successfully" +
                "client is registered {}",client);
        return ResponseEntity.ok().body("Operation completed successfully");
    }

    @Operation(
            summary = "Sign Up Not Client",
            description = "This method registers the NON client in the application"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Operation completed successfully",
                    content =  @Content),
            @ApiResponse(responseCode = "400", description = "Invalid data",
                    content = @Content),
            @ApiResponse(responseCode = "500", description = "Registration failed",
                    content = @Content)})
    @PostMapping("user-profile/new")
    public ResponseEntity<String> signUpNotClient(@Valid @RequestBody RequestNonClientDto request) {
        log.info("RegistrationController. PostMethod 'signUp'(new) {}", request);
         Client client= registrationService.registerNonClient(
                clientMapper.registerNonClientDtoToClient(request));
        ResponseNonClientDto  responseNonClientDto = clientMapper.clientToResponseNonClientDto(client);
        log.info("RegistrationController. Return from method 'signUp'(new): {}",responseNonClientDto);
        return ResponseEntity.ok().body("Operation completed successfully");
    }

    @Operation(
            summary = "Check Registration",
            description = "This method check registration a client"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Operation completed successfully",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(oneOf = {NonRegisteredDto.class,NonRegisteredClientDto.class}))}),
            @ApiResponse(responseCode = "400", description = "Invalid data",
                    content = @Content)})
    @GetMapping()
    public ResponseEntity<NonRegisteredDto> checkRegistration(@RequestParam String mobilePhone) {
        log.info("RegistrationController. GetMethod 'checkRegistration'{}", mobilePhone);
        Optional<Client> client = registrationService.checkRegistration(mobilePhone);
        if (client.isEmpty()){
            NonRegisteredDto nonRegisteredDto = new NonRegisteredDto(mobilePhone, ClientStatus.NOT_CLIENT, "Operation completed successfully");
            log.info("RegistrationController. Return from method 'checkRegistration': {}",nonRegisteredDto);
            return ResponseEntity.ok(nonRegisteredDto);
        }
        NonRegisteredDto nonRegisteredDto = new NonRegisteredClientDto(mobilePhone,client.get().getClientStatus()
                ,"Operation completed successfully", client.get().getId());
        log.info("RegistrationController. Return from method 'checkRegistration': {}",nonRegisteredDto);
        return ResponseEntity.ok(nonRegisteredDto);
    }

}
