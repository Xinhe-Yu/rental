package com.chatop.rental.dto;

import java.util.List;

public class RentalListResponseDTO implements ApiResponseDTO {
  private List<RentalDTO> rentals;

  public RentalListResponseDTO(List<RentalDTO> rentals) {
    this.rentals = rentals;
  }

  public List<RentalDTO> getRentals() {
    return rentals;
  }

  public void setError(List<RentalDTO> rentals) {
    this.rentals = rentals;
  }
}
