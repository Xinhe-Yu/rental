package com.chatop.rental.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public class MessageRequestDTO {
    @JsonProperty("rental_id")
    private Long rentalId;
    private String message;

    public Long getRentalId() {
        return rentalId;
    }

    public String getMessage() {
        return message;
    }
}
