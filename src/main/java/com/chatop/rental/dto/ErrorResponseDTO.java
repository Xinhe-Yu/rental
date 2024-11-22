package com.chatop.rental.dto;

public class ErrorResponseDTO implements ApiResponseDTO {
  private String error;

  public ErrorResponseDTO(String error) {
    this.error = error;
  }

  public String getError() {
    return error;
  }

  public void setError(String error) {
    this.error = error;
  }
}
