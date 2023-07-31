package com.larramendiProject.RegisterLoginSystem.userController;

import com.larramendiProject.RegisterLoginSystem.model.dto.LoginDTO;
import com.larramendiProject.RegisterLoginSystem.model.dto.UpdatePasswordDTO;
import com.larramendiProject.RegisterLoginSystem.model.dto.UserDTO;
import com.larramendiProject.RegisterLoginSystem.model.response.LoginResponse;
import com.larramendiProject.RegisterLoginSystem.model.response.UpdateResponse;
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

    @PutMapping(path = "updateData/{id}")
    public ResponseEntity<UserDTO> updateUserData(@RequestBody UserDTO userDTO, @PathVariable Long id) {
        UserDTO updatingUser = userService.updateData(userDTO, id);
        return ResponseEntity.ok(updatingUser);
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
