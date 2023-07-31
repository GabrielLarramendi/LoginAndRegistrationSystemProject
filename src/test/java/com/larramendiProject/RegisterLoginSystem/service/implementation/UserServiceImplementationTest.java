package com.larramendiProject.RegisterLoginSystem.service.implementation;

import com.larramendiProject.RegisterLoginSystem.exceptions.EmptyFieldException;
import com.larramendiProject.RegisterLoginSystem.model.dto.UserDTO;
import com.larramendiProject.RegisterLoginSystem.model.entity.User;
import com.larramendiProject.RegisterLoginSystem.exceptions.IdNotFoundException;
import com.larramendiProject.RegisterLoginSystem.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UserServiceImplementationTest {

    private UserRepository userRepository;
    private UserServiceImplementation userServiceImplementation;
    private PasswordEncoder passwordEncoder;

    private User user;
    private List<UserDTO> userDTOList;

    @BeforeEach
    public void setUp() {
        userRepository = mock(UserRepository.class);
        passwordEncoder = mock(PasswordEncoder.class);
        userServiceImplementation = new UserServiceImplementation(userRepository, passwordEncoder);

        user = new User(1L, "Joao", "joao@gmail.com", passwordEncoder.encode("encodePassword"));
    }

    @Test
    public void testFindAllUsers_ListNotEmpty_ShouldReturnListUserDTO() {
        List<User> userList = Arrays.asList(
                new User(1L, "John", "john@example.com", "senha"),
                new User(2L, "Alice", "alice@example.com", "senha"),
                new User(2L, "Gabriel", "gabriel@example.com", "senha"));

        when(userRepository.findAll()).thenReturn(userList);
        List<UserDTO> userDTOList = userServiceImplementation.findAll();

        List<UserDTO> copyUserList = userList.stream()
                                                    .map(user -> userServiceImplementation.mapUserToUserDto(user))
                                                    .toList();

        assertEquals(userDTOList.size(), userList.size());
        assertEquals(userDTOList.get(0), copyUserList.get(0));
        assertTrue(userDTOList.equals(copyUserList));
    }

    @Test
    public void testFindAllUsers_EmptyList_ShouldReturnAnEmptyUserDtoList() {
        List<User> userList = new ArrayList<>();
        when(userRepository.findAll()).thenReturn(userList);
        List<UserDTO> userDTOList = userServiceImplementation.findAll();
        assertTrue(userDTOList.isEmpty());
    }

    @Test
    public void testFindById_UserExists_ShouldReturnUserDTO() {
        when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));

        UserDTO userDto = userServiceImplementation.findById(user.getId());

        verify(userRepository, times(1)).findById(user.getId());

        assertNotNull(userDto, "UserDTO nao deve ser nulo");
        assertEquals(user.getId(), userDto.getId());
        assertEquals(user.getName(), userDto.getName());
        assertEquals(user.getEmail(), userDto.getEmail());
        assertEquals(user.getPassword(), userDto.getPassword());
    }

    @Test
    public void testFindById_UserDoesNotExist_ShouldThrowIdNotFoundException() {
        when(userRepository.findById(user.getId())).thenReturn(Optional.empty());
        assertThrows(IdNotFoundException.class, () -> userServiceImplementation.findById(user.getId()));
        verify(userRepository, times(1)).findById(user.getId());
    }

    @Test
    public void testSaveUser_Success() {
        UserDTO userDTO = new UserDTO(1L, "Alex", "alex@gmail.com", "pwd");

        when(userRepository.findByEmail(userDTO.getEmail())).thenReturn(null);
        when(passwordEncoder.encode(userDTO.getPassword())).thenReturn("pwd");

        UserDTO savedUser = userServiceImplementation.saveUser(userDTO);

        verify(userRepository, times(1)).findByEmail(userDTO.getEmail());
        verify(userRepository, times(1)).save(any(User.class));

        assertEquals(userDTO, savedUser);
    }

    @Test
    public void testSaveUser_fieldsMustNotBeEmpty_ShouldThrowEmptyfieldsException() {
        UserDTO emptyName = new UserDTO(1L, "", "ab@gmail.com", "pwd");
        UserDTO emptyEmail = new UserDTO(1L, "ab", "", "pwd");
        UserDTO emptyPwd = new UserDTO(1L, "ab", "ab@gmail.com", "");
        UserDTO allFieldsEmpty = new UserDTO(1L, "", "", "");

        assertThrows(EmptyFieldException.class, () -> userServiceImplementation.saveUser(emptyName));
        assertThrows(EmptyFieldException.class, () -> userServiceImplementation.saveUser(emptyEmail));
        assertThrows(EmptyFieldException.class, () -> userServiceImplementation.saveUser(emptyPwd));
        assertThrows(EmptyFieldException.class, () -> userServiceImplementation.saveUser(allFieldsEmpty));
    }

}