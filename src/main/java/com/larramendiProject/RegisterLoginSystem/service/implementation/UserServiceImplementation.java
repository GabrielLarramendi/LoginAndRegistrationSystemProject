package com.larramendiProject.RegisterLoginSystem.service.implementation;

import com.larramendiProject.RegisterLoginSystem.model.entity.User;
import com.larramendiProject.RegisterLoginSystem.exceptions.EmailAlreadyExistsException;
import com.larramendiProject.RegisterLoginSystem.exceptions.EmptyFieldException;
import com.larramendiProject.RegisterLoginSystem.exceptions.IdNotFoundException;
import com.larramendiProject.RegisterLoginSystem.exceptions.InvalidPasswordException;
import com.larramendiProject.RegisterLoginSystem.model.dto.LoginDTO;
import com.larramendiProject.RegisterLoginSystem.model.dto.UpdatePasswordDTO;
import com.larramendiProject.RegisterLoginSystem.model.dto.UserDTO;
import com.larramendiProject.RegisterLoginSystem.model.response.LoginResponse;
import com.larramendiProject.RegisterLoginSystem.repository.UserRepository;
import com.larramendiProject.RegisterLoginSystem.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserServiceImplementation implements UserService {

    @Autowired
    private final UserRepository userRepository;

    @Autowired
    private final PasswordEncoder passwordEncoder;

    public UserServiceImplementation(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public List<UserDTO> findAll() {
        List<User> savedListUsers = userRepository.findAll();
        List<UserDTO> userDTOList = savedListUsers.stream().map(this::mapUserToUserDto).toList();
        return userDTOList;
    }

    @Override
    public UserDTO findById(Long id) {
        if (id == null) {
            throw new NullPointerException("Algum valor deve ser passado!");
        }

        User savedUser = userRepository
                .findById(id)
                .orElseThrow(() -> new IdNotFoundException("Usuario com o Id '" + id + "' nao encontrado."));
        return mapUserToUserDto(savedUser);
    }

    @Override
    public UserDTO saveUser(UserDTO userDto) {
        if (userDto.getName().isEmpty() || userDto.getEmail().isEmpty() || userDto.getPassword().isEmpty()) {
            throw new EmptyFieldException("Todos os campos sao obrigatorios!");
        }

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

        userRepository.save(user);
        return mapUserToUserDto(user);
    }

    @Override
    public UserDTO updateData(UserDTO userDTO, Long id) {
        User user = userRepository
                .findById(id)
                .orElseThrow(() -> new IdNotFoundException("Usuario com o Id '" + id + "' nao encontrado."));

        if (!userDTO.getName().isEmpty()) {
            user.setName(userDTO.getName());
        }

        if (!userDTO.getEmail().isEmpty()) {
            String newEmail = userDTO.getEmail();
            String currentEmail = user.getEmail();

            String newPassword = userDTO.getPassword();
            String encodedPassword = user.getPassword();
            boolean isPwdRight = passwordEncoder.matches(newPassword, encodedPassword);
            if (isPwdRight) {
                if (newEmail != null && newEmail.equals(currentEmail)) {
                    throw new EmailAlreadyExistsException("O novo email é o mesmo que o email atualmente registrado na sua conta.");
                }
                User existentUserByEmail = userRepository.findByEmail(userDTO.getEmail());
                if (existentUserByEmail != null) {
                    throw new EmailAlreadyExistsException("Este email já está sendo utilizado por outro usuário!");
                }
                user.setEmail(userDTO.getEmail());
            }
            else {
                throw new InvalidPasswordException("Senha incorreta!");
            }
        }

        userRepository.save(user);
        return mapUserToUserDto(user);
    }

    @Override
    public UserDTO updateUserPassword(UpdatePasswordDTO updatePasswordDTO, Long id) {
        User user = userRepository
                .findById(id)
                .orElseThrow(() -> new IdNotFoundException("Usuario com o Id '" + id + "' nao encontrado."));

        String currentPwd = updatePasswordDTO.getOldPwd();
        String encodedPassword = user.getPassword();

        boolean isPwdRight = passwordEncoder.matches(currentPwd, encodedPassword);
        if (isPwdRight) {
            String newPwd = updatePasswordDTO.getNewPwd();
            String confirmNewPwd = updatePasswordDTO.getConfirmNewPwd();
            if (newPwd.equals(confirmNewPwd)) {
                user.setPassword(this.passwordEncoder.encode(updatePasswordDTO.getNewPwd()));
            }
            else {
                throw new InvalidPasswordException("A confirmacao da senha nao confere com a nova senha informada.");
            }
        } else {
            throw new InvalidPasswordException("Senha atual incorreta.");
        }

        userRepository.save(user);
        return mapUserToUserDto(user);
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
                return new LoginResponse("Logado com sucesso!", true);
            } else {
                return new LoginResponse("Senha incorreta", false);
            }
        } else {
            return new LoginResponse("Email nao existe", false);
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