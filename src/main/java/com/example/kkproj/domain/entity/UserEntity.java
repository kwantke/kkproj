package com.example.kkproj.domain.entity;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Entity
@Getter
@Setter
@Table(name = "users")

public class UserEntity {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(name="user_uuid", nullable = false, unique = true, updatable = false, length = 36)
  private String userUuid;


  @Column(name="user_id", nullable = false, unique = true, length = 36)
  private String userId;

  @Column(nullable = false, unique = false, length = 48)
  private String username;

  @Column(nullable = false)
  private String password;

  @Column(name="role")
  private String role;

  @PrePersist
  public void PrePersist() {
    if (userUuid == null) {
      userUuid = UUID.randomUUID().toString();
    }
  }



}
