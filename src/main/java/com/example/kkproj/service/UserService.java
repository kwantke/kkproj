package com.example.kkproj.service;

import com.example.kkproj.model.UserVo;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService implements UserDetailsService {


  public UserVo loadUserByUserName(String userId) {
    UserEntity userEntity = userRepository.findByUserId()
    return new UserVo();
  }

  @Override
  public UserDetails loadUserByUsername(String userId) throws UsernameNotFoundException {

    return null;
  }
}
