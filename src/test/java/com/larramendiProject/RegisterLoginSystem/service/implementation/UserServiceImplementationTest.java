package com.larramendiProject.RegisterLoginSystem.service.implementation;

import com.larramendiProject.RegisterLoginSystem.exceptions.EmailAlreadyExistsException;
import com.larramendiProject.RegisterLoginSystem.exceptions.EmptyFieldException;
import com.larramendiProject.RegisterLoginSystem.exceptions.InvalidPasswordException;
import com.larramendiProject.RegisterLoginSystem.model.dto.UpdatePasswordDTO;
import com.larramendiProject.RegisterLoginSystem.model.dto.UserDTO;
import com.larramendiProject.RegisterLoginSystem.model.entity.User;
import com.larramendiProject.RegisterLoginSystem.exceptions.IdNotFoundException;
import com.larramendiProject.RegisterLoginSystem.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceImplementationTest {

    @Mock
    private UserRepository userRepository;
    @Mock
    private PasswordEncoder passwordEncoder;
    @InjectMocks
    private final UserServiceImplementation userServiceImplementation = new UserServiceImplementation(userRepository, passwordEncoder);


    @Test
    void findAllUsers_ShouldReturnUserDtoList() {
        List<User> mockUserList = Arrays.asList(
                new User(1L, "Gabriel", "gabriel@gmail.com", "123"),
                new User(2L, "Maria", "maria@gmail.com", "1234"),
                new User(3L, "Joao", "joao@gmail.com", "12345")
        );

        when(userRepository.findAll()).thenReturn(mockUserList);
        List<UserDTO> userDTOList = userServiceImplementation.findAll();

        assertEquals(mockUserList.size(), userDTOList.size());

        for (int i = 0; i < mockUserList.size(); i++) {
            assertEquals(mockUserList.get(i).getId(), userDTOList.get(i).getId());
            assertEquals(mockUserList.get(i).getName(), userDTOList.get(i).getName());
            assertEquals(mockUserList.get(i).getEmail(), userDTOList.get(i).getEmail());
            assertEquals(mockUserList.get(i).getPassword(), userDTOList.get(i).getPassword());
        }
    }
    @Test
    void findAllUsers_ShouldReturnEmptyList() {
        List<User> mockUserList = new ArrayList<>();
        when(userRepository.findAll()).thenReturn(mockUserList);
        List<UserDTO> userDTOList = userServiceImplementation.findAll();
        assertTrue(userDTOList.isEmpty());
    }


    @Test
    void findById_ShouldReturnUserDTO() {
        User mockUser = new User(1L, "Gabriel", "gabriel@gmail.com", "123");
        when(userRepository.findById(mockUser.getId())).thenReturn(Optional.of(mockUser));
        UserDTO userDTO = userServiceImplementation.findById(mockUser.getId());

        assertNotNull(userDTO);
        assertEquals(mockUser.getId(), userDTO.getId());
        assertEquals(mockUser.getName(), userDTO.getName());
        assertEquals(mockUser.getEmail(), userDTO.getEmail());
        assertEquals(mockUser.getPassword(), userDTO.getPassword());
    }
    @Test
    void findById_ShouldThrowIdNotFoundException() {
        when(userRepository.findById(1L)).thenReturn(Optional.empty());
        assertThrows(IdNotFoundException.class, () -> userServiceImplementation.findById(1L));
    }
    @Test
    void findById_ShouldThrowNullPointerException() {
        assertThrows(NullPointerException.class, () -> userServiceImplementation.findById(null));
    }


    @Test
    void saveUser_ShouldSaveUserAndReturnUserDTO() {
        UserDTO userDTO = new UserDTO(1L, "Gabriel", "gabriel@gmail.com", "123");
        when(passwordEncoder.encode(userDTO.getPassword())).thenReturn(userDTO.getPassword());

        User user = new User(
                userDTO.getId(),
                userDTO.getName(),
                userDTO.getEmail(),
                passwordEncoder.encode(userDTO.getPassword())
        );

        assertEquals(user.getId(), userDTO.getId());
        assertEquals(user.getName(), userDTO.getName());
        assertEquals(user.getEmail(), userDTO.getEmail());
        assertEquals(user.getPassword(), userDTO.getPassword());
    }
    @Test
    void saveUser_emptyFields_ShouldThrowEmptyFieldException() {
        UserDTO userDTOEmptyName = new UserDTO(1L, "", "gabriel@gmail.com", "123");
        UserDTO userDTOEmptyEmail = new UserDTO(1L, "gabriel", "", "123");
        UserDTO userDTOEmptyPwd = new UserDTO(1L, "gabriel", "gabriel@gmail.com", "");
        UserDTO userDTOAllFieldsEmpty = new UserDTO(1L, "", "", "");

        assertThrows(EmptyFieldException.class, () -> userServiceImplementation.saveUser(userDTOEmptyName));
        assertThrows(EmptyFieldException.class, () -> userServiceImplementation.saveUser(userDTOEmptyEmail));
        assertThrows(EmptyFieldException.class, () -> userServiceImplementation.saveUser(userDTOEmptyPwd));
        assertThrows(EmptyFieldException.class, () -> userServiceImplementation.saveUser(userDTOAllFieldsEmpty));
    }
    @Test
    void saveUser_EmailDoesNotExist_ShouldSaveUser() {
        UserDTO userDTO = new UserDTO(1L, "Gabriel", "gabriel@gmail.com", "123");

        when(userRepository.findByEmail(userDTO.getEmail())).thenReturn(null);
        when(passwordEncoder.encode(userDTO.getPassword())).thenReturn("123");

        UserDTO savedUserDTO = userServiceImplementation.saveUser(userDTO);

        assertNotNull(savedUserDTO);
        assertEquals(userDTO.getName(), savedUserDTO.getName());
        assertEquals(userDTO.getEmail(), savedUserDTO.getEmail());
        assertEquals(userDTO.getPassword(), savedUserDTO.getPassword());
    }
    @Test
    void saveUser_EmailAlreadyExists_ShouldThrowEmailAlreadyExistsException() {
        UserDTO userDTO = new UserDTO(1L, "Gabriel", "gabriel@gmail.com", "123");
        User user = new User(1L, "Gabriel", "gabriel@gmail.com", "123");
        when(userRepository.findByEmail(userDTO.getEmail())).thenReturn(user);
        assertThrows(EmailAlreadyExistsException.class, () -> userServiceImplementation.saveUser(userDTO));
    }


    @Test
    void updateUser_UpdateName_Success() {
        //Metodo recebe user DTO
        UserDTO userDTO = new UserDTO(1L, "Gabriel Larramendi", "", "");

        User user = new User(userDTO.getId(), "Gabriel", "gabriel@gmail.com", "123");
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        User mockUser = userRepository.findById(1L).get();

        UserDTO updatedUser = userServiceImplementation.updateData(userDTO, 1L);

        assertEquals(userDTO.getName(), updatedUser.getName());
    }
    @Test
    void updateUser_UpdateEmail_ValidPassword_ValidEmail_Success() {
        UserDTO userDTO = new UserDTO(1L, "", "gabriel@gmail.com", "123");
        User user = new User(userDTO.getId(), "Gabriel", "gabriel.larramendi@gmail.com", "123");

        when(passwordEncoder.matches(user.getPassword(), userDTO.getPassword())).thenReturn(true);
        when(userRepository.findById(userDTO.getId())).thenReturn(Optional.of(user));
        when(userRepository.findByEmail(userDTO.getEmail())).thenReturn(null);

        User mockUser = userRepository.findById(userDTO.getId()).get();

        UserDTO updatedUser = userServiceImplementation.updateData(userDTO, 1L);

        assertEquals(mockUser.getEmail(), updatedUser.getEmail());
        assertEquals(userDTO.getEmail(), updatedUser.getEmail());
    }
    @Test
    void updateUser_UpdateEmail_ValidPassword_InvalidEmail_CurrentEmail_Fail_ShouldThrowEmailAlreadyExistsException() {
        UserDTO userDTO = new UserDTO(1L, "", "gabriel@gmail.com", "123");
        User user = new User(userDTO.getId(), "Gabriel", "gabriel@gmail.com", "123");
        User user1 = new User(2L, "Pedro", "gabriel@gmail.com", "123");
        List<User> usersMock = new ArrayList<>(Arrays.asList(user, user1));

        when(passwordEncoder.matches(user.getPassword(), userDTO.getPassword())).thenReturn(true);
        when(userRepository.findById(userDTO.getId())).thenReturn(Optional.of(user));

        EmailAlreadyExistsException exception = assertThrows(EmailAlreadyExistsException.class, () -> userServiceImplementation.updateData(userDTO, 1L));
        System.out.println(exception.getMessage());
    }
    @Test
    void updateUser_UpdateEmail_ValidPassword_InvalidEmail_EmailAlreadyExists_Fail_ShouldThrowEmailAlreadyExistsException() {
        UserDTO userDTO = new UserDTO(1L, "", "gabriel@gmail.com", "123");
        User user = new User(userDTO.getId(), "Gabriel", "oioi@gmail.com", "123");
        User user1 = new User(2L, "Pedro", "gabriel@gmail.com", "123");

        List<User> usersMock = new ArrayList<>(Arrays.asList(user, user1));

        when(userRepository.findById(userDTO.getId())).thenReturn(Optional.of(user));
        when(passwordEncoder.matches(user.getPassword(), userDTO.getPassword())).thenReturn(true);

        usersMock.forEach(u -> {
            if (userDTO.getEmail().equals(u.getEmail()) && !Objects.equals(userDTO.getId(), u.getId())) {
                when(userRepository.findByEmail(userDTO.getEmail())).thenReturn(user1);
            }
        });

        EmailAlreadyExistsException exception = assertThrows(EmailAlreadyExistsException.class, () -> userServiceImplementation.updateData(userDTO, 1L));
        System.out.println(exception.getMessage());
    }
    @Test
    void updateUser_UpdateEmail_InvalidPassword_Fail_ShouldThrowInvalidPasswordException() {
        UserDTO userDTO = new UserDTO(1L, "", "gabriel@gmail.com", "123");
        User user = new User(userDTO.getId(), "Gabriel", "gabriel.larramendi@gmail.com", "1234");

        when(passwordEncoder.matches(userDTO.getPassword(), user.getPassword())).thenReturn(false);
        when(userRepository.findById(userDTO.getId())).thenReturn(Optional.of(user));
        InvalidPasswordException exception = assertThrows(InvalidPasswordException.class, () -> userServiceImplementation.updateData(userDTO, 1L));
        System.out.println(exception.getMessage());
    }


    @Test
    void UpdatePassword_Success() {
        UpdatePasswordDTO updatePasswordDTO = new UpdatePasswordDTO("123", "aaa", "aaa");
        User user = new User(1L, "Gabs", "gabriel@gmail.com", "123");

        when(passwordEncoder.matches(updatePasswordDTO.getOldPwd(), user.getPassword())).thenReturn(true);
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(passwordEncoder.encode(updatePasswordDTO.getNewPwd())).thenReturn("aaa");

        UserDTO userDTO = userServiceImplementation.updateUserPassword(updatePasswordDTO, 1L);

        assertEquals(updatePasswordDTO.getNewPwd(), userDTO.getPassword());
    }
    @Test
    void updatePassword_Fail_CorrectCurrentPwd_NewPwdAndConfirmNewPwdDoNotMatch_ShouldThrowInvalidPwdException() {
        UpdatePasswordDTO updatePasswordDTO = new UpdatePasswordDTO("123", "aaa", "bbb");
        User user = new User(1L, "Gabs", "gabriel@gmail.com", "123");

        when(passwordEncoder.matches(updatePasswordDTO.getOldPwd(), user.getPassword())).thenReturn(true);
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        InvalidPasswordException exception = assertThrows(InvalidPasswordException.class, () -> userServiceImplementation.updateUserPassword(updatePasswordDTO, 1L));
        System.out.println(exception.getMessage());
    }
    @Test
    void updatePassword_Fail_IncorrectCurrentPwd_ShouldThrowInvalidPwdException() {
        UpdatePasswordDTO updatePasswordDTO = new UpdatePasswordDTO("123", "aaa", "bbb");
        User user = new User(1L, "Gabs", "gabriel@gmail.com", "123");

        when(passwordEncoder.matches(updatePasswordDTO.getOldPwd(), user.getPassword())).thenReturn(false);
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        InvalidPasswordException exception = assertThrows(InvalidPasswordException.class, () -> userServiceImplementation.updateUserPassword(updatePasswordDTO, 1L));
        System.out.println(exception.getMessage());
    }


    @Test
    void deleteUser_DeleteById_Success() {
        User user = new User(1L, "Gabs", "gabriel@gmail.com", "123");
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(user));
        doNothing().when(userRepository).delete(user);
        userServiceImplementation.deleteUser(user.getId());
        verify(userRepository, times(1)).delete(user);
    }

    @Test
    void deleteUser_Fail_ShouldThrowIdNotFoundException() {
        Long id = 2L;
        when(userRepository.findById(anyLong())).thenReturn(Optional.empty());
        assertThrows(IdNotFoundException.class, () -> userServiceImplementation.deleteUser(id));
    }
}