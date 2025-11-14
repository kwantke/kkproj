package com.example.kkproj.presentation.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LoginRequest {

  @NotBlank(message = "UserId must not be blank")
  private String userId;
  @NotBlank(message = "Password must not be blank")
  private String password;

}
