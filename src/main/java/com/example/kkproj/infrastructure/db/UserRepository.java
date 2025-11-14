package com.example.kkproj.infrastructure.db;


import com.example.kkproj.domain.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<UserEntity, Long> {
  Optional<UserEntity> findByUserId(String userid);
}
