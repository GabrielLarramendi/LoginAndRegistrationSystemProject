package com.larramendiProject.RegisterLoginSystem.service;

import com.larramendiProject.RegisterLoginSystem.dto.UserDTO;
import com.larramendiProject.RegisterLoginSystem.entity.User;

public class UserService {

    public String createUser(UserDTO userDto) {
        User saveUser = new User(userDto.getId(), userDto.getName(), userDto.getEmail(), userDto.getPassword());
    }

}
