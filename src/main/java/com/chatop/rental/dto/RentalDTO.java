package com.chatop.rental.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.LocalDate;

public class RentalDTO {
    private Long id;
    private String name;
    private Double surface;
    private Double price;

    @JsonProperty("picture")
    private String[] pictures;
    private String description;

    @JsonProperty("owner_id")
    private Long ownerId;

    @JsonProperty("created_at")
    private LocalDate createdAt;

    @JsonProperty("updated_at")
    private LocalDate updatedAt;

    // default constructor
    public RentalDTO() {}

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

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
        return pictures;
    }

    public void setPictures(String[] pictures) {
        this.pictures = pictures;
    }

    @JsonIgnore
    public void setPicturesFromString(String baseUrl, String picturesStr) {
        if (picturesStr == null || picturesStr.trim().isEmpty()) {
            this.pictures = new String[0];
        } else {
            if (picturesStr.startsWith("http")) {
                this.pictures = new String[] {picturesStr};
            } else {
                this.pictures = new String[] {baseUrl + picturesStr};
            }
        }
    }

    @JsonIgnore
    public String getPicturesAsString() {
        if (pictures == null || pictures.length == 0) {
            return "";
        }
        return String.join(",", pictures);
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

    public LocalDate getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDate createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDate getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDate updatedAt) {
        this.updatedAt = updatedAt;
    }
}
