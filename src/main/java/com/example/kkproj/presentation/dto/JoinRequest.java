package com.example.kkproj.presentation.dto;

import lombok.Data;

@Data
public class JoinRequest {
  private String userId;
  private String username;
  private String password;
  private String role;
}