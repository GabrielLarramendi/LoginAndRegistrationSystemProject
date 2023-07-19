package com.larramendiProject.RegisterLoginSystem.service;

import com.larramendiProject.RegisterLoginSystem.dto.LoginDTO;
import com.larramendiProject.RegisterLoginSystem.dto.UserDTO;
import com.larramendiProject.RegisterLoginSystem.login.response.LoginResponse;

public interface UserService {
    String saveUser(UserDTO userDto);

    LoginResponse loginUser(LoginDTO loginDTO);
}
