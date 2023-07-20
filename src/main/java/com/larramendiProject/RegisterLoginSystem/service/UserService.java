package com.larramendiProject.RegisterLoginSystem.service;

import com.larramendiProject.RegisterLoginSystem.dto.LoginDTO;
import com.larramendiProject.RegisterLoginSystem.dto.UpdateUserNameDTO;
import com.larramendiProject.RegisterLoginSystem.dto.UserDTO;
import com.larramendiProject.RegisterLoginSystem.response.LoginResponse;
import com.larramendiProject.RegisterLoginSystem.response.UpdateResponse;

import java.util.List;

public interface UserService {

    List<UserDTO> findAll();

    UserDTO findById(Long id);

    UserDTO saveUser(UserDTO userDto);

    UpdateResponse updateUserName(UpdateUserNameDTO updateUserNameDTO, Long id);

    UpdateResponse updateUserEmail(UserDTO userDTO, Long id);

    UpdateResponse updateUserPassword(UserDTO userDTO, Long id);

    void deleteUser(Long id);

    LoginResponse loginUser(LoginDTO loginDTO);


}
