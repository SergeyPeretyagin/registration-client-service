package com.userservice.controller;

import com.userservice.domain.dto.UpdatePasswordRequestDto;
import com.userservice.service.LoginService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
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
@RequestMapping("login")
@RequiredArgsConstructor
@Log4j2
@CrossOrigin
@Tag(name="LoginController", description="This controller is for password update")
public class LoginController {
    private final LoginService loginService;

    @Operation(
            summary = "update Password",
            description = "This method update Password"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "400", description = "Invalid data/Wrong old password entered",
                    content =  @Content),
            @ApiResponse(responseCode = "409", description = "Passwords do not match",
                    content = @Content),
            @ApiResponse(responseCode = "500", description = "Server Error",
                    content = @Content)})

    @PatchMapping("password")
    public ResponseEntity<String> updatePassword(@RequestBody @Valid UpdatePasswordRequestDto updatePasswordRequestDto) {
        log.info("LoginController. PatchMethod 'updatePassword'. @RequestBody newPassword: {}",
                updatePasswordRequestDto);
        String updatePassword = loginService.updatePassword(updatePasswordRequestDto.getPassportNumber(),
                updatePasswordRequestDto.getNewPassword(),updatePasswordRequestDto.getConfirmNewPassword());
        log.info("LoginController. Return from method 'updatePassword': {}", updatePassword);
        return ResponseEntity.ok(updatePassword);
    }

}
