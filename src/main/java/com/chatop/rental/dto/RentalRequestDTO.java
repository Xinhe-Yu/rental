package com.chatop.rental.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDate;

public class RentalRequestDTO {
    private String name;
    private Double surface;
    private Double price;

    @Schema(description = "Array of pictures (URLs or Base64 encoded strings")
    @JsonProperty("picture")
    private String[] pictures;

    private String description;

    @JsonProperty("owner_id")
    private Long ownerId;

    // default constructor
    public RentalRequestDTO() {}

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public Double getSurface() {
        return surface;
    }

    public void setSurface(Double surface) {
        this.surface = surface;
    }

    public String[] getPictures() {
        return pictures != null ? pictures : new String[0];
    }

    public void setPictures(String[] pictures) {
        this.pictures = pictures != null ? pictures : new String[0];
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Long getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(Long ownerId) {
        this.ownerId = ownerId;
    }

}
