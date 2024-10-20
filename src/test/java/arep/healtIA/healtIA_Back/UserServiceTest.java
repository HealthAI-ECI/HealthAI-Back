package arep.healtIA.healtIA_Back;

import arep.healtIA.healtIA_Back.exception.HealtIAException;
import arep.healtIA.healtIA_Back.model.UserEntity;
import arep.healtIA.healtIA_Back.model.dto.UserDto;
import arep.healtIA.healtIA_Back.repository.UserRepository;
import arep.healtIA.healtIA_Back.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class UserServiceTest {

    @InjectMocks
    private UserService userService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void showAllUsers_ReturnsUserList() {
        // Simula el comportamiento del repositorio
        when(userRepository.findAll()).thenReturn(List.of(new UserEntity("John Doe", "john@example.com", "hashedPassword", "1990-01-01")));

        assertEquals(1, userService.showAllUsers().size());
    }

    @Test
    void getUser_ReturnsUser_WhenExists() throws HealtIAException {
        UserEntity user = new UserEntity("John Doe", "john@example.com", "hashedPassword", "1990-01-01");
        when(userRepository.findByEmail("john@example.com")).thenReturn(Optional.of(user));

        // Verifica que se retorne el usuario correcto
        assertEquals(user, userService.getUser("john@example.com"));
    }

    @Test
    void getUser_ThrowsException_WhenUserDoesNotExist() {
        when(userRepository.findByEmail(any())).thenReturn(Optional.empty());

        // Verifica que se lance la excepción
        assertThrows(HealtIAException.class, () -> userService.getUser("nonexistent@example.com"));
    }

    @Test
    void createUser_SuccessfullyCreatesUser() throws HealtIAException {
        UserDto userDto = new UserDto();
        userDto.setFullName("John Doe");
        userDto.setEmail("john@example.com");
        userDto.setHashPassword("plainPassword");
        userDto.setBornDate("1990-01-01");

        when(userRepository.findByEmail(userDto.getEmail())).thenReturn(Optional.empty());
        when(passwordEncoder.encode(userDto.getHashPassword())).thenReturn("hashedPassword");

        // Ejecuta el método
        userService.createUser(userDto);

        // Verifica que se haya llamado al método save
        verify(userRepository, times(1)).save(any(UserEntity.class));
    }

    @Test
    void createUser_ThrowsException_WhenUserAlreadyExists() {
        UserDto userDto = new UserDto();
        userDto.setEmail("john@example.com");

        when(userRepository.findByEmail(userDto.getEmail())).thenReturn(Optional.of(new UserEntity()));

        // Verifica que se lance la excepción
        assertThrows(HealtIAException.class, () -> userService.createUser(userDto));
    }

    @Test
    void updateUser_SuccessfullyUpdatesUser() throws HealtIAException {
        UserDto userDto = new UserDto();
        userDto.setFullName("Jane Doe");
        userDto.setEmail("jane@example.com");

        UserEntity existingUser = new UserEntity("John Doe", "john@example.com", "hashedPassword", "1990-01-01");
        when(userRepository.findByEmail("john@example.com")).thenReturn(Optional.of(existingUser));

        // Ejecuta el método
        userService.updateUser(userDto, "john@example.com");

        // Verifica que se haya llamado al método save
        verify(userRepository, times(1)).save(existingUser);
        assertEquals("Jane Doe", existingUser.getFullName());
    }

    @Test
    void updateUser_ThrowsException_WhenUserDoesNotExist() {
        UserDto userDto = new UserDto();
        when(userRepository.findByEmail(any())).thenReturn(Optional.empty());

        // Verifica que se lance la excepción
        assertThrows(HealtIAException.class, () -> userService.updateUser(userDto, "nonexistent@example.com"));
    }

    @Test
    void deleteUser_SuccessfullyDeletesUser() throws HealtIAException {
        UserEntity user = new UserEntity("John Doe", "john@example.com", "hashedPassword", "1990-01-01");
        when(userRepository.findByEmail("john@example.com")).thenReturn(Optional.of(user));

        // Ejecuta el método
        userService.deleteUser("john@example.com");

        // Verifica que se haya llamado al método delete
        verify(userRepository, times(1)).delete(user);
    }

    @Test
    void deleteUser_ThrowsException_WhenUserDoesNotExist() {
        when(userRepository.findByEmail(any())).thenReturn(Optional.empty());

        // Verifica que se lance la excepción
        assertThrows(HealtIAException.class, () -> userService.deleteUser("nonexistent@example.com"));
    }
}
