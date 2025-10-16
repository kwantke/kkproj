package com.example.kkproj.model.entity;

import com.example.kkproj.model.UserRole;
import com.example.kkproj.model.UserVo;
import jakarta.persistence.*;
import lombok.Getter;

import java.util.UUID;

@Entity
@Getter
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
    if (userId == null) {
      userUuid = UUID.randomUUID().toString();
    }
  }



}
