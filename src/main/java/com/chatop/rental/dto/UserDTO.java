package com.chatop.rental.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.LocalDate;

public class UserDTO {
    @JsonProperty("id")
    private Long id;

    private String name;
    private String email;

    @JsonIgnore
    private String password;

    @JsonProperty("created_at")
    private LocalDate createdAt;

    @JsonProperty("updated_at")
    private LocalDate updatedAt;

    public void setId(Long id) {
        this.id = id;
    }

    public void setCreatedAt(LocalDate createdAt){
        this.createdAt = createdAt;
    }

    public void setUpdatedAt(LocalDate updatedAt) {
        this.updatedAt = updatedAt;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
