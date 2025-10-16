package com.example.kkproj.service;

import com.example.kkproj.dto.JoinRequest;
import com.example.kkproj.dto.LoginRequest;
import com.example.kkproj.model.UserVo;
import com.example.kkproj.model.entity.UserEntity;
import com.example.kkproj.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService implements UserDetailsService {
  private final UserRepository userRepository;
  private final PasswordEncoder encoder;


  @Override
  public UserDetails loadUserByUsername(String userId) throws UsernameNotFoundException {
    UserEntity userEntity = userRepository.findByUserId(userId)
            .orElseThrow(() -> new UsernameNotFoundException("not found"));
    return UserVo.toVo(userEntity);
  }

  public UserVo login(LoginRequest req) throws Exception {

    UserEntity userEntity = userRepository.findByUserId(req.getUserId())
            .orElseThrow(()-> new UsernameNotFoundException("not found"));

    if (!encoder.matches(req.getPassword(), userEntity.getPassword())) {
      throw new Exception();
    }


    return UserVo.toVo(userEntity);

  }

  public UserVo join(JoinRequest req) {
    // 이미 존재하는 userId 검사
    userRepository.findByUserId(req.getUserId()).ifPresent(u -> {
      throw new IllegalArgumentException("이미 존재하는 사용자입니다.");
    });

    UserEntity entity = new UserEntity();

    entity.setUserId(req.getUserId());
    entity.setUsername(req.getUsername());
    entity.setPassword(encoder.encode(req.getPassword())); // ✅ 비밀번호 암호화
    entity.setRole(req.getRole());

    userRepository.save(entity);

    return UserVo.toVo(entity);

  }
}
