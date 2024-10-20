package arep.healtIA.healtIA_Back;

import arep.healtIA.healtIA_Back.controller.UserController;

import arep.healtIA.healtIA_Back.exception.HealtIAException;
import arep.healtIA.healtIA_Back.model.UserEntity;
import arep.healtIA.healtIA_Back.model.dto.UserDto;
import arep.healtIA.healtIA_Back.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

class UserControllerTest {

    @InjectMocks
    private UserController userController;

    @Mock
    private UserService userService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getAllUsers_ReturnsUsers() {
        List<UserEntity> userList = new ArrayList<>();
        userList.add(new UserEntity()); // Añadir un usuario simulado

        when(userService.showAllUsers()).thenReturn(userList);

        ResponseEntity<Object> response = userController.getAllUsers();
        assertEquals(HttpStatus.ACCEPTED, response.getStatusCode());
        assertEquals(userList, response.getBody());
    }

    @Test
    void getUser_ReturnsUser_WhenExists() throws HealtIAException {
        UserEntity user = new UserEntity();
        when(userService.getUser("test@example.com")).thenReturn(user);

        ResponseEntity<Object> response = userController.getUser("test@example.com");
        assertEquals(HttpStatus.ACCEPTED, response.getStatusCode());
        assertEquals(user, response.getBody());
    }

    @Test
    void getUser_ReturnsNotFound_WhenUserDoesNotExist() throws HealtIAException {
        when(userService.getUser(anyString())).thenThrow(new HealtIAException(HealtIAException.USER_NOT_FOUND));

        ResponseEntity<Object> response = userController.getUser("nonexistent@example.com");
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals(HealtIAException.USER_NOT_FOUND, response.getBody());
    }

    @Test
    void createUser_CreatesUser_Successfully() throws HealtIAException {
        UserDto userDto = new UserDto();
        userController.createUser(userDto);
        verify(userService, times(1)).createUser(userDto);
    }


    @Test
    void updateUser_ReturnsAccepted_WhenUpdatedSuccessfully() throws HealtIAException {
        UserDto userDto = new UserDto();
        userController.updateUser(userDto, "test@example.com");
        verify(userService, times(1)).updateUser(userDto, "test@example.com");
    }

    @Test
    void deleteUser_ReturnsAccepted_WhenDeletedSuccessfully() throws HealtIAException {
        ResponseEntity<Object> response = userController.deleteUser("test@example.com");

        assertEquals(HttpStatus.ACCEPTED, response.getStatusCode());
        assertEquals("User deleted successfully", response.getBody());
        verify(userService, times(1)).deleteUser("test@example.com");
    }

    @Test
    void deleteUser_ReturnsNotFound_WhenUserDoesNotExist() throws HealtIAException {
        doThrow(new HealtIAException(HealtIAException.USER_NOT_FOUND)).when(userService).deleteUser(anyString());

        ResponseEntity<Object> response = userController.deleteUser("nonexistent@example.com");
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals(HealtIAException.USER_NOT_FOUND, response.getBody());
    }
}
