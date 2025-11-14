package com.example.kkproj.domain;

public enum UserRole {
  ADMIN,
  USER;

  public static UserRole fromString(String role) {
    if(role == null) return USER;
    try {
      return UserRole.valueOf(role.toUpperCase());

    } catch (IllegalArgumentException e) {
      return USER;
    }
  }
}
