package com.userservice.controller;

import com.userservice.domain.dto.AuthDto;
import com.userservice.domain.dto.AuthenticationRequestDto;
import com.userservice.domain.entity.UserProfile;
import com.userservice.domain.mapper.UserProfileMapper;
import com.userservice.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.servers.Server;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("login")
@CrossOrigin
@Tag(name="AuthenticateController", description="This controller receives requests only " +
        "from the service gateway to receive id and password. For internal use only ")
public class AuthController {

    private final UserProfileMapper userProfileMapper;

    private final AuthService authService;

    @Operation(
            summary = "get id by phone number Or passport",
            description = "Only the gateway service interacts with this method. " +
                    "'http://10.10.14.16:8089/serviceuser/login' - this is link for original method"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = AuthDto.class)) }),
            @ApiResponse(responseCode = "400", description = "Invalid data",
                    content = @Content),
            @ApiResponse(responseCode = "500", description = "Internal Server Error",
                    content = @Content)})

    @PostMapping()

    public ResponseEntity<AuthDto> getIdByPhoneNumberOrPassport(@RequestBody AuthenticationRequestDto request) {
        log.info("AuthController. PostMethod 'getIdByPhoneNumber'. @RequestBody request: {}", request);
        UserProfile foundUserProfile = authService.getUserProfileByPhoneNumberOrPassport(request.getLogin());
        AuthDto authDto = userProfileMapper.responseUserProfileToAuthDto(foundUserProfile);
        log.info("AuthController. Return from method 'getIdByPhoneNumber': {}",authDto);
        return ResponseEntity.ok(authDto);
    }
}
