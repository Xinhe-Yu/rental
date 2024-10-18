package com.chatop.rental.entities;

import jakarta.persistence.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "RENTALS")
public class Rental {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    private Double surface;

    @Column(nullable = false)
    private Double price;

    private String picture;

    @Column(length = 2000)
    private String description;

    @ManyToOne
    @JoinColumn(name = "owner_id", nullable = false, referencedColumnName = "id")
    private User owner;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    // No-Argument Constructor
    public Rental() {
        // JPA requires a no-arg constructor
    }

    // Constructor with parameters (excluding the ID)
    public Rental(String name, Double surface, Double price, String picture, String description, User owner) {
        this.name = name;
        this.surface = surface;
        this.price = price;
        this.picture = picture;
        this.description = description;
        this.owner = owner;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Double getSurface() {
        return surface;
    }

    public void setSurface(Double surface) {
        this.surface = surface;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public User getOwner() {
        return owner;
    }

    public LocalDate getCreatedAt() {
        return createdAt.toLocalDate(); // Convert LocalDateTime to LocalDate
    }

    public LocalDate getUpdatedAt() {
        return updatedAt.toLocalDate(); // Convert LocalDateTime to LocalDate
    }

    // Automatically set the timestamps before insert and update
    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }
}
