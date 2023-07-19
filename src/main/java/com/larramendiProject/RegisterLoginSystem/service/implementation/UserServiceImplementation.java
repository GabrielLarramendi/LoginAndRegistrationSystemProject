package com.larramendiProject.RegisterLoginSystem.service.implementation;

import com.larramendiProject.RegisterLoginSystem.dto.LoginDTO;
import com.larramendiProject.RegisterLoginSystem.dto.UserDTO;
import com.larramendiProject.RegisterLoginSystem.entity.User;
import com.larramendiProject.RegisterLoginSystem.login.response.LoginResponse;
import com.larramendiProject.RegisterLoginSystem.repository.UserRepository;
import com.larramendiProject.RegisterLoginSystem.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserServiceImplementation implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public String saveUser(UserDTO userDto) {
        User user = new User(
                userDto.getId(),
                userDto.getName(),
                userDto.getEmail(),
                this.passwordEncoder.encode(userDto.getPassword())
        );
        userRepository.save(user);
        return user.getName();
    }

    @Override
    public LoginResponse loginUser(LoginDTO loginDTO) {
        String msg = "";
        User user = userRepository.findByEmail(loginDTO.getEmail());
        if (user != null) {
            String password = loginDTO.getPassword();
            String encodedPassword = user.getPassword();
            Boolean isPwdRight = passwordEncoder.matches(password, encodedPassword);
            if (isPwdRight) {
                Optional<User> user1 = userRepository.findOneByEmailAndPassword(loginDTO.getEmail(), encodedPassword);
                if (user1.isPresent()) {
                    return new LoginResponse("Login Success", true);
                } else {
                    return new LoginResponse("Login Failed", false);
                }
            } else {
                return new LoginResponse("password Not Match", false);
            }
        }
        else {
            return new LoginResponse("Email not exits", false);
        }
    }
}
