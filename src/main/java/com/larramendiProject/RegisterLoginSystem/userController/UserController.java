package com.larramendiProject.RegisterLoginSystem.userController;

import com.larramendiProject.RegisterLoginSystem.dto.LoginDTO;
import com.larramendiProject.RegisterLoginSystem.dto.UserDTO;
import com.larramendiProject.RegisterLoginSystem.login.response.LoginResponse;
import com.larramendiProject.RegisterLoginSystem.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin
@RequestMapping("api/login-and-register-system")
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping(path = "/save")
    public String saveUser(@RequestBody UserDTO userDto) {
        String id = userService.saveUser(userDto);
        return id;
    }

    @PostMapping(path = "/login")
    public ResponseEntity<?> loginUser(@RequestBody LoginDTO loginDTO) {
        LoginResponse loginMessage = userService.loginUser(loginDTO);
        return ResponseEntity.ok(loginMessage);
    }
}
