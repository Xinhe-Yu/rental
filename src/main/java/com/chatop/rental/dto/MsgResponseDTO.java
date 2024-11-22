package com.chatop.rental.dto;

public class MsgResponseDTO implements ApiResponseDTO {
  private String message;

  public MsgResponseDTO(String message) {
    this.message = message;
  }

  public String getMessage() {
    return message;
  }

  public void setMessage(String message) {
    this.message = message;
  }
}
