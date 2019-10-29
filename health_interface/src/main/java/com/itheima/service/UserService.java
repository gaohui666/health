package com.itheima.service;

import com.itheima.pojo.User;

import java.util.Map;

public interface UserService {

    User findByUsername(String username);

    Map<String, Object> loginByRole(String username);
}
