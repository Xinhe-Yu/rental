package com.chatop.rental.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public class RentalListDTO extends RentalDTO {
  @JsonProperty("picture")
  private String picture;

  public void setPicture(String baseUrl, String picture) {
    if (picture.startsWith("http")) {
      this.picture = picture;
    } else {
      this.picture = baseUrl + picture;
    }
  }

  public String getPicture() {
    return picture;
  }

}
