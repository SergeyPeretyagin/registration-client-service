package com.userservice.controller;

import com.userservice.service.DeleteService;
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

import javax.validation.constraints.NotNull;
import java.util.UUID;

@Controller
@RequestMapping("user-service")
@RequiredArgsConstructor
@Log4j2
@CrossOrigin
@Tag(name="DeleteController", description="This controller is for " +
        "deletion by id and phone number")
public class DeleteController {

    private final DeleteService deleteService;

    @Operation(
            summary = "Delete User By UUID",
            description = "The method deletes the client by UUID"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "User account deleted successfully",
                    content =  @Content),
            @ApiResponse(responseCode = "404", description = "This user does not exist",
                    content = @Content),
            @ApiResponse(responseCode = "500", description = "Delete error, user not deleted",
                    content = @Content)})
    @DeleteMapping("user")
    public ResponseEntity<String> deleteUserByUUID(@NotNull  @RequestParam UUID clientId){
        log.info("DeleteController. DeleteMethod 'deleteUserFromDB'. @RequestParam {}", clientId);
        String response = deleteService.deleteUserByUUID(clientId);
        return ResponseEntity.status(204).body(response);
    }

    @Operation(
            summary = "Delete User By Phone Number",
            description = "The method deletes the client by phone number"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "User account deleted successfully",
                    content =  @Content),
            @ApiResponse(responseCode = "404", description = "This user does not exist",
                    content = @Content),
            @ApiResponse(responseCode = "500", description = "Delete error, user not deleted",
                    content = @Content)})
    @DeleteMapping("client/phone-number")
    public ResponseEntity<String> deleteUserByPhoneNumber(@NotNull @RequestParam String phoneNumber){
        log.info("Delete user by mobile phone {}", phoneNumber);
        String answer = deleteService.deleteUserByPhoneNumber(phoneNumber);
        log.info("The user was deleted by phone number {}, {} ", phoneNumber,answer);
        return ResponseEntity.status(204).body(answer);
    }
}
