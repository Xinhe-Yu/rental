package com.chatop.rental.entities;

import jakarta.persistence.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class Message {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "rental_id", nullable = false)
    private Rental rental;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(length = 2000, nullable = false)
    private String message;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    // No-Argument Constructor
    public Message() {
        // JPA requires a no-arg constructor
    }

    // Constructor with parameters
    public Message(Rental rental, User user, String message) {
        this.rental = rental;
        this.user = user;
        this.message = message;
    }

    public Long getId() {
        return id;
    }

    public Rental getRental() {
        return rental;
    }

    public User getUser() {
        return user;
    }

    public String getMessage() {
        return message;
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
