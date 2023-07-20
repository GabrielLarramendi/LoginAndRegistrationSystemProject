package com.larramendiProject.RegisterLoginSystem.userController;

import com.larramendiProject.RegisterLoginSystem.dto.LoginDTO;
import com.larramendiProject.RegisterLoginSystem.dto.UpdateUserEmailDTO;
import com.larramendiProject.RegisterLoginSystem.dto.UpdateUserNameDTO;
import com.larramendiProject.RegisterLoginSystem.dto.UserDTO;
import com.larramendiProject.RegisterLoginSystem.response.LoginResponse;
import com.larramendiProject.RegisterLoginSystem.response.UpdateResponse;
import com.larramendiProject.RegisterLoginSystem.service.UserService;
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
    public ResponseEntity<UserDTO> saveUser(@RequestBody UserDTO userDto) {
        UserDTO user = userService.saveUser(userDto);
        return ResponseEntity.ok(user);
    }

    @PutMapping(path = "update/name/{id}")
    public ResponseEntity<UpdateResponse> updateUserName(@RequestBody UpdateUserNameDTO updateUserNameDTO, @PathVariable Long id) {
        UpdateResponse updateMessage = userService.updateUserName(updateUserNameDTO, id);
        return ResponseEntity.ok(updateMessage);
    }

    @PutMapping(path = "update/email/{id}")
    public ResponseEntity<UpdateResponse> updateUserEmail(@RequestBody UpdateUserEmailDTO updateUserEmailDTO, @PathVariable Long id) {
        UpdateResponse updateMessage = userService.updateUserEmail(updateUserEmailDTO, id);
        return ResponseEntity.ok(updateMessage);
    }

    @PutMapping(path = "update/password/{id}")
    public ResponseEntity<UpdateResponse> updateUserPassword(@RequestBody UserDTO userDTO, @PathVariable Long id) {
        UpdateResponse updateMessage = userService.updateUserPassword(userDTO, id);
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
