package com.larramendiProject.RegisterLoginSystem.service.implementation;

import com.larramendiProject.RegisterLoginSystem.dto.UserDTO;
import com.larramendiProject.RegisterLoginSystem.entity.User;
import com.larramendiProject.RegisterLoginSystem.exceptions.IdNotFoundException;
import com.larramendiProject.RegisterLoginSystem.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UserServiceImplementationTest {

    private UserRepository userRepository;
    private UserServiceImplementation userServiceImplementation;

    private User user;
    private List<UserDTO> userDTOList;

    @BeforeEach
    public void setUp() {
        userRepository = mock(UserRepository.class);
        userServiceImplementation = new UserServiceImplementation(userRepository);

        user = new User(1L, "Joao", "joao@gmail.com", "senha");

    }

    @Test
    public void testFindAllUsers_ListNotEmpty_ShouldReturnListUserDTO() {
        List<User> userList = Arrays.asList(
                new User(1L, "John", "john@example.com", "senha"),
                new User(2L, "Alice", "alice@example.com", "senha"));

        when(userRepository.findAll()).thenReturn(userList);
        List<UserDTO> userDTOList = userServiceImplementation.findAll();
        List<UserDTO> copyUserList = userList.stream()
                                                    .map(user -> userServiceImplementation.mapUserToUserDto(user))
                                                    .toList();

        assertEquals(userDTOList, copyUserList);
    }

    @Test
    public void testFindAllUsers_EmptyList_ShouldReturnAnEmptyList() {
        List<User> userList = new ArrayList<>();
        when(userRepository.findAll()).thenReturn(userList);
    }

    @Test
    public void testFindById_UserExists_ShouldReturnUserDTO() {
        when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));
        UserDTO userDto = userServiceImplementation.findById(user.getId());
        verify(userRepository, times(1)).findById(user.getId());

        assertNotNull(userDto);
        assertEquals(user.getId(), userDto.getId());
    }

    @Test
    public void testFindById_UserDoesNotExist_ShouldThrowIdNotFoundException() {
        when(userRepository.findById(user.getId())).thenReturn(Optional.empty());
        assertThrows(IdNotFoundException.class, () -> userServiceImplementation.findById(user.getId()));
        verify(userRepository, times(1)).findById(user.getId());
    }
}