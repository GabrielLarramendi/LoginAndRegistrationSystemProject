package com.larramendiProject.RegisterLoginSystem.service;

import com.larramendiProject.RegisterLoginSystem.dto.*;
import com.larramendiProject.RegisterLoginSystem.response.LoginResponse;
import com.larramendiProject.RegisterLoginSystem.response.UpdateResponse;

import java.util.List;

public interface UserService {

    List<UserDTO> findAll();

    UserDTO findById(Long id);

    UserDTO saveUser(UserDTO userDto);

    UserDTO updateData(UserDTO userDTO, Long id);

    UpdateResponse updateUserPassword(UpdatePasswordDTO updatePasswordDTO, Long id);

    void deleteUser(Long id);

    LoginResponse loginUser(LoginDTO loginDTO);


}
