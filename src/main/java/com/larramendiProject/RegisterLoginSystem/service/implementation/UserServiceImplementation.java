package com.larramendiProject.RegisterLoginSystem.service.implementation;

import com.larramendiProject.RegisterLoginSystem.dto.LoginDTO;
import com.larramendiProject.RegisterLoginSystem.dto.UserDTO;
import com.larramendiProject.RegisterLoginSystem.entity.User;
import com.larramendiProject.RegisterLoginSystem.exceptions.EmailAlreadyExistsException;
import com.larramendiProject.RegisterLoginSystem.exceptions.IdNotFoundException;
import com.larramendiProject.RegisterLoginSystem.response.LoginResponse;
import com.larramendiProject.RegisterLoginSystem.repository.UserRepository;
import com.larramendiProject.RegisterLoginSystem.response.UpdateResponse;
import com.larramendiProject.RegisterLoginSystem.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserServiceImplementation implements UserService {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;


    @Override
    public List<UserDTO> findAll() {
        List<User> savedListUsers = userRepository.findAll();
        return savedListUsers.stream().map(this::mapUserToUserDto).toList();
    }

    @Override
    public UserDTO findById(Long id) {
        User savedUser = userRepository
                .findById(id)
                .orElseThrow(() -> new IdNotFoundException("Usuario com o Id '" + id + "' nao encontrado."));
        return mapUserToUserDto(savedUser);
    }

    @Override
    public UserDTO saveUser(UserDTO userDto) {
        User user = new User(
                userDto.getId(),
                userDto.getName(),
                userDto.getEmail(),
                this.passwordEncoder.encode(userDto.getPassword())
        );

        User userByEmail = userRepository.findByEmail(userDto.getEmail());
        if (userByEmail != null && userByEmail.getEmail() != null) {
            throw new EmailAlreadyExistsException("Esse e-mail ja esta cadastrado!");
        }
        ;
        userRepository.save(user);
        return mapUserToUserDto(user);
    }

    @Override
    public UpdateResponse updateUserName(UserDTO userDTO, Long id) {
        User user = userRepository.findById(id).get();
        if (user != null) {
            String password = userDTO.getPassword();
            String encodedPassword = user.getPassword();
            boolean isPwdRight = passwordEncoder.matches(password, encodedPassword);
            if (isPwdRight) {
                user.setName(userDTO.getName());
                userRepository.save(user);
                return new UpdateResponse("Nome alterado com sucesso!", true);
            } else {
                return new UpdateResponse("Senha incorreta.", true);
            }
        }
        else {
            return new UpdateResponse("Usuario nao encontrado.", false);
        }
    }

    @Override
    public UpdateResponse updateUserEmail(UserDTO userDTO, Long id) {
        User user = userRepository.findById(id).get();
        String newEmail = userDTO.getEmail();
        String currentEmail = user.getEmail();

        if (user != null) {
            String password = userDTO.getPassword();
            String encodedPassword = user.getPassword();
            boolean isPwdRight = passwordEncoder.matches(password, encodedPassword);
            if (isPwdRight) {
                if (newEmail != null && newEmail.equals(currentEmail)) {
                    throw new EmailAlreadyExistsException("O novo email é o mesmo que o email atualmente registrado na sua conta.");
                }
                User existentUserByEmail = userRepository.findByEmail(userDTO.getEmail());
                if (existentUserByEmail != null) {
                    throw new EmailAlreadyExistsException("Este email já está sendo utilizado por outro usuário!");
                }
                user.setEmail(userDTO.getEmail());
                userRepository.save(user);
                return new UpdateResponse("Email alterado com sucesso!", true);
            } else {
                return new UpdateResponse("Senha incorreta.", false);
            }
        }
        else {
            return new UpdateResponse("Usuario nao encontrado.", false);
        }
    }

    @Override
    public void deleteUser (Long id){
        User user = userRepository
                .findById(id)
                .orElseThrow(() -> new IdNotFoundException("Usuario com o Id " + id + " nao encontrado."));
        userRepository.delete(user);
    }

    @Override
    public LoginResponse loginUser (LoginDTO loginDTO){
        User user = userRepository.findByEmail(loginDTO.getEmail());
        if (user != null) {
            String password = loginDTO.getPassword();
            String encodedPassword = user.getPassword();
            boolean isPwdRight = passwordEncoder.matches(password, encodedPassword);
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
        } else {
            return new LoginResponse("Email not exits", false);
        }
    }

    public UserDTO mapUserToUserDto (User user){
        UserDTO userDTO = new UserDTO();
        userDTO.setId(user.getId());
        userDTO.setName(user.getName());
        userDTO.setEmail(user.getEmail());
        userDTO.setPassword(user.getPassword());
        return userDTO;
    }

}


