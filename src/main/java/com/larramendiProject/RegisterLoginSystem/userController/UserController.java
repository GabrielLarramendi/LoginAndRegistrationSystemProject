package com.larramendiProject.RegisterLoginSystem.userController;

import com.larramendiProject.RegisterLoginSystem.dto.LoginDTO;
import com.larramendiProject.RegisterLoginSystem.dto.UserDTO;
import com.larramendiProject.RegisterLoginSystem.login.response.LoginResponse;
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

    @GetMapping(path = "/allUsers")
    public ResponseEntity<List<UserDTO>> findAll() {
        List<UserDTO> list = userService.findAll();
        return ResponseEntity.ok().body(list);
    }

    @GetMapping(value = "user/{id}")
    public ResponseEntity<UserDTO> findById(@PathVariable Long id) {
        UserDTO user = userService.findById(id);
        return ResponseEntity.ok().body(user);
    }

    @PostMapping(path = "/save")
    public ResponseEntity<UserDTO> saveUser(@RequestBody UserDTO userDto) {
        UserDTO user = userService.saveUser(userDto);
        return ResponseEntity.ok(user);
    }

    @PutMapping(value = "update/name/{id}")
    public ResponseEntity<String> updateUserName(@RequestBody String name,
                                              @PathVariable Long id) {
        UserDTO user = userService.findById(id);
        return ResponseEntity.ok().body("oioi");
    }

    @DeleteMapping(value = "delete/{id}")
    public ResponseEntity<String> deleteUSer(@PathVariable Long id) {
        userService.deleteUser(id);
        return ResponseEntity.ok().body("Usuario deletado com sucesso!");
    }

    @PostMapping(path = "/login")
    public ResponseEntity<?> loginUser(@RequestBody LoginDTO loginDTO) {
        LoginResponse loginMessage = userService.loginUser(loginDTO);
        return ResponseEntity.ok(loginMessage);
    }
}
