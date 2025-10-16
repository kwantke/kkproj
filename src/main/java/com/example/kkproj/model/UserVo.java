package com.example.kkproj.model;

import com.example.kkproj.model.entity.UserEntity;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.sql.Timestamp;
import java.util.Collection;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserVo implements UserDetails {

  private Long id;
  private String userUuid;
  private String userId;
  private String userName;
  private String password;
  private UserRole userRole;
  private Timestamp registeredAt;
  private Timestamp updatedAt;
  private Timestamp deletedAt;

  @Override
  public Collection<? extends GrantedAuthority> getAuthorities() {
    if (userRole == null) return List.of();
    return List.of(new SimpleGrantedAuthority(userRole.name()));
  }

  @Override
  public String getUsername() {
    return null;
  }

  @Override
  public boolean isAccountNonExpired() {
    return UserDetails.super.isAccountNonExpired();
  }

  @Override
  public boolean isAccountNonLocked() {
    return UserDetails.super.isAccountNonLocked();
  }

  @Override
  public boolean isCredentialsNonExpired() {
    return UserDetails.super.isCredentialsNonExpired();
  }

  @Override
  public boolean isEnabled() {
    return UserDetails.super.isEnabled();
  }

  public static UserVo toVo(UserEntity entity) {
    return UserVo.builder()
            .id(entity.getId())
            .userUuid(entity.getUserUuid())
            .userId(entity.getUserId())
            .userName(entity.getUsername())
            .password(entity.getPassword())
            .userRole(UserRole.fromString(entity.getRole()))
            .build();
  }
}
