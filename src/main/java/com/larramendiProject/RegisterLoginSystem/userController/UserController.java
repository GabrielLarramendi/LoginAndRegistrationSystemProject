package com.larramendiProject.RegisterLoginSystem.userController;

import com.larramendiProject.RegisterLoginSystem.dto.*;
import com.larramendiProject.RegisterLoginSystem.response.LoginResponse;
import com.larramendiProject.RegisterLoginSystem.response.UpdateResponse;
import com.larramendiProject.RegisterLoginSystem.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin
@RequestMapping("api/login-and-register-system")
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping(path = "/users")
    public ResponseEntity<List<UserDTO>> findAll() {
        List<UserDTO> list = userService.findAll();
        return ResponseEntity.ok().body(list);
    }

    @GetMapping(path = "user/{id}")
    public ResponseEntity<UserDTO> findById(@PathVariable Long id) {
        UserDTO user = userService.findById(id);
        return ResponseEntity.ok().body(user);
    }

    @PostMapping(path = "/save")
    public ResponseEntity<UserDTO> saveUser(@Valid @RequestBody UserDTO userDto) {
        UserDTO user = userService.saveUser(userDto);
        return ResponseEntity.ok(user);
    }

    @PutMapping(path = "update/name/{id}")
    public ResponseEntity<UpdateResponse> updateUserName(@Valid @RequestBody UpdateUserNameDTO updateUserNameDTO, @PathVariable Long id) {
        UpdateResponse updateMessage = userService.updateUserName(updateUserNameDTO, id);
        return ResponseEntity.ok(updateMessage);
    }

    @PutMapping(path = "update/email/{id}")
    public ResponseEntity<UpdateResponse> updateUserEmail(@Valid @RequestBody UpdateUserEmailDTO updateUserEmailDTO, @PathVariable Long id) {
        UpdateResponse updateMessage = userService.updateUserEmail(updateUserEmailDTO, id);
        return ResponseEntity.ok(updateMessage);
    }

    @PutMapping(path = "update/password/{id}")
    public ResponseEntity<UpdateResponse> updateUserPassword(@RequestBody UpdatePasswordDTO updatePasswordDTO, @PathVariable Long id) {
        UpdateResponse updateMessage = userService.updateUserPassword(updatePasswordDTO, id);
        return ResponseEntity.ok(updateMessage);
    }

    @PostMapping(path = "/login")
    public ResponseEntity<LoginResponse> loginUser(@RequestBody LoginDTO loginDTO) {
        LoginResponse loginMessage = userService.loginUser(loginDTO);
        return ResponseEntity.ok(loginMessage);
    }

    @DeleteMapping(path = "delete/{id}")
    public ResponseEntity<String> deleteUSer(@PathVariable Long id) {
        userService.deleteUser(id);
        return ResponseEntity.ok().body("Usuario deletado com sucesso!");
    }
}
