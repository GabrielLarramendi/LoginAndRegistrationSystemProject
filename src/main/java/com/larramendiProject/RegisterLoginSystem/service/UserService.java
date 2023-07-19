package com.larramendiProject.RegisterLoginSystem.service;

import com.larramendiProject.RegisterLoginSystem.dto.LoginDTO;
import com.larramendiProject.RegisterLoginSystem.dto.UserDTO;
import com.larramendiProject.RegisterLoginSystem.login.response.LoginResponse;

import java.util.List;

public interface UserService {

    List<UserDTO> findAll();

    UserDTO findById(Long id);

    UserDTO saveUser(UserDTO userDto);

    void deleteUser(Long id);

    LoginResponse loginUser(LoginDTO loginDTO);


}
