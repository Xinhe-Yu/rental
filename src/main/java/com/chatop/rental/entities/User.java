package com.chatop.rental.entities;

import jakarta.persistence.*;
import java.sql.Timestamp;

@Entity
public class User {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  private String name;

  @Column(unique = true)
  private String email;

  private String password;

  @Column(name = "created_at", nullable = false, updatable = false)
  private Timestamp createdAt;

  @Column(name = "updated_at")
  private Timestamp updatedAt;
}
