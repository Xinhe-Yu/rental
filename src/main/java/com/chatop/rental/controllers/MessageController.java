package com.chatop.rental.controllers;

import com.chatop.rental.configuration.CustomUserDetails;
import com.chatop.rental.dto.MessageRequestDTO;
import com.chatop.rental.dto.MsgResponseDTO;
import com.chatop.rental.services.MessageService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/messages")
@Tag(name = "Messages", description = "Message management APIs")
public class MessageController {
  private final MessageService messageService;

  public MessageController(MessageService messageService) {
    this.messageService = messageService;
  }

  @Operation(summary = "Create a new message", description = "Create a new message. Requires authentication.")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Message created with success", content = @Content(schema = @Schema(type = "object", example = "{\"message\": \"Message send with success\"}"))),
      @ApiResponse(responseCode = "400", description = "Invalid Input", content = @Content(schema = @Schema(type = "object", example = "{\"error\" : \"Invalid input\""))),
      @ApiResponse(responseCode = "401", description = "Not authenticated", content = @Content(schema = @Schema(type = "object", example = "{\"error\" : \"User not authenticated\"")))
  })
  @PostMapping
  public ResponseEntity<MsgResponseDTO> createMessage(@RequestBody MessageRequestDTO messageDTO,
      @AuthenticationPrincipal CustomUserDetails userDetails) {
    messageService.createMessage(messageDTO, userDetails);

    MsgResponseDTO response = new MsgResponseDTO("Message send with success");
    return ResponseEntity.ok(response);
  }
}
