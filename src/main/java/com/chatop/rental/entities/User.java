package com.chatop.rental.entities;

import jakarta.persistence.*;
import org.springframework.cglib.core.Local;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "USERS")
public class User {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(nullable = false)
  private String name;

  @Column(unique = true, nullable = false)
  private String email;

  @Column(nullable = false)
  private String password;

  @Column(name = "created_at", nullable = false, updatable = false)
  private LocalDateTime createdAt;

  @Column(name = "updated_at")
  private LocalDateTime updatedAt;

  // No-Argument Constructor
  public User() {
    // JPA requires a no-arg constructor
  }

  public User(String name, String email, String password) {
    this.name = name;
    this.email = email;
    setPassword(password);
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

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public void setPassword(String password) {
    BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
    this.password = passwordEncoder.encode(password);  // Hash the password before setting
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
