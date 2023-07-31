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
import com.larramendiProject.RegisterLoginSystem.model.response.UpdateResponse;
import com.larramendiProject.RegisterLoginSystem.service.UserService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserServiceImplementation implements UserService {


    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserServiceImplementation(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public List<UserDTO> findAll() {
        List<User> savedListUsers = userRepository.findAll();
        return savedListUsers.stream().map(this::mapUserToUserDto).toList();
    }

    @Override
    public UserDTO findById(Long id) {
        if (id != null) {
            User savedUser = userRepository
                    .findById(id)
                    .orElseThrow(() -> new IdNotFoundException("Usuario com o Id '" + id + "' nao encontrado."));
            return mapUserToUserDto(savedUser);
        }
        else {
            throw new NullPointerException("Algum valor deve ser passado!");
        }
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
            emailValidation(userDTO, user);
        }

        userRepository.save(user);
        return mapUserToUserDto(user);
    }

    @Override
    public UpdateResponse updateUserPassword(UpdatePasswordDTO updatePasswordDTO, Long id) {
        User user = userRepository
                .findById(id)
                .orElseThrow(() -> new IdNotFoundException("Usuario com o Id '" + id + "' nao encontrado."));

        String currentPwdToConfirm = updatePasswordDTO.getOldPwd();
        String encodedPassword = user.getPassword();
        boolean isPwdRight = passwordEncoder.matches(currentPwdToConfirm, encodedPassword);
        if (isPwdRight) {
            String newPwd = updatePasswordDTO.getNewPwd();
            String confirmNewPwd = updatePasswordDTO.getConfirmNewPwd();
            if (newPwd.equals(confirmNewPwd)) {
                user.setPassword(this.passwordEncoder.encode(updatePasswordDTO.getNewPwd()));
                userRepository.save(user);
                return new UpdateResponse("Senha alterado com sucesso!", true);
            }
            else {
                return new UpdateResponse("A confirmacao da senha nao confere com a nova senha informada.", false);
            }
        } else {
            return new UpdateResponse("Senha atual incorreta.", false);
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
                    return new LoginResponse("Entrou", true);
                } else {
                    return new LoginResponse("Falhou", false);
                }
            } else {
                return new LoginResponse("Senha incorreta", false);
            }
        } else {
            return new LoginResponse("Email nao existe", false);
        }
    }

    public void emailValidation(UserDTO userDTO, User user) {
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
            userRepository.save(user);
        } else {
            throw new InvalidPasswordException("Senha incorreta!");
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


