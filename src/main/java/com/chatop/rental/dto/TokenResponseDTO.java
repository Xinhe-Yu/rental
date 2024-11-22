package com.chatop.rental.dto;

public class TokenResponseDTO implements ApiResponseDTO {
  private String token;

  public TokenResponseDTO(String token) {
    this.token = token;
  }

  public String getToken() {
    return token;
  }

  public void setToken(String token) {
    this.token = token;
  }
}
