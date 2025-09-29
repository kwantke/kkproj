package com.example.kkproj.service;

import com.example.kkproj.model.UserVo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {


  public UserVo loadUserByUserName(String userId) {

    return new UserVo();
  }
}
