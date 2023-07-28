package com.larramendiProject.RegisterLoginSystem.service;

import com.larramendiProject.RegisterLoginSystem.dto.*;
import com.larramendiProject.RegisterLoginSystem.model.dto.LoginDTO;
import com.larramendiProject.RegisterLoginSystem.model.dto.UpdatePasswordDTO;
import com.larramendiProject.RegisterLoginSystem.model.dto.UserDTO;
import com.larramendiProject.RegisterLoginSystem.model.response.LoginResponse;
import com.larramendiProject.RegisterLoginSystem.model.response.UpdateResponse;

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
