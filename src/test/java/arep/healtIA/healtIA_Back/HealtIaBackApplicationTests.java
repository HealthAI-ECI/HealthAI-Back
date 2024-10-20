package arep.healtIA.healtIA_Back;

import arep.healtIA.healtIA_Back.controller.UserController;
import arep.healtIA.healtIA_Back.exception.HealtIAException;
import arep.healtIA.healtIA_Back.model.UserEntity;
import arep.healtIA.healtIA_Back.model.dto.UserDto;
import arep.healtIA.healtIA_Back.service.UserService;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@SpringBootTest
@AutoConfigureMockMvc
class HealtIaBackApplicationTests {

	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private UserService userService; // MockBean para simular el servicio

	@Autowired
	private UserController userController;

	@Test
	void contextLoads() {
		// Verifica que el contexto de la aplicación se carga correctamente
	}

	@Test
	void testCreateUser_Success() throws HealtIAException {
		UserDto userDto = new UserDto();
		userDto.setFullName("John Doe");
		userDto.setEmail("john@example.com");
		userDto.setHashPassword("hashedPassword");
		userDto.setBornDate("1990-01-01");

		// Simular la creación de un usuario
		doNothing().when(userService).createUser(any());

		ResponseEntity<Object> response = userController.createUser(userDto);
		assertEquals(HttpStatus.CREATED, response.getStatusCode()); // Verifica que la respuesta sea 201 Created
	}

	@Test
	void testGetUser_Success() throws HealtIAException {
		UserEntity user = new UserEntity("John Doe", "john@example.com", "hashedPassword", "1990-01-01");
		when(userService.getUser("john@example.com")).thenReturn(user);

		ResponseEntity<Object> response = userController.getUser("john@example.com");
		assertEquals(HttpStatus.ACCEPTED, response.getStatusCode()); // Verifica que la respuesta sea 202 Accepted
		assertEquals(user, response.getBody());
	}

	@Test
	void testGetUser_NotFound() throws HealtIAException {
		when(userService.getUser(any())).thenThrow(new HealtIAException(HealtIAException.USER_NOT_FOUND));

		ResponseEntity<Object> response = userController.getUser("nonexistent@example.com");
		assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode()); // Verifica que la respuesta sea 404 Not Found
		assertEquals(HealtIAException.USER_NOT_FOUND, response.getBody());
	}

	@Test
	void testUpdateUser_Success() throws HealtIAException {
		UserDto userDto = new UserDto();
		userDto.setFullName("John Doe Updated");
		userDto.setEmail("john@example.com");

		// Simular la actualización de un usuario
		doNothing().when(userService).updateUser(any(), any());

		ResponseEntity<Object> response = userController.updateUser(userDto, "john@example.com");
		assertEquals(HttpStatus.ACCEPTED, response.getStatusCode()); // Verifica que la respuesta sea 202 Accepted
	}

	@Test
	void testDeleteUser_Success() throws HealtIAException {
		// Simular la eliminación de un usuario
		doNothing().when(userService).deleteUser(any());

		ResponseEntity<Object> response = userController.deleteUser("john@example.com");
		assertEquals(HttpStatus.ACCEPTED, response.getStatusCode()); // Verifica que la respuesta sea 202 Accepted
		assertEquals("User deleted successfully", response.getBody());
	}

	@Test
	void testDeleteUser_NotFound() throws HealtIAException {
		doThrow(new HealtIAException(HealtIAException.USER_NOT_FOUND)).when(userService).deleteUser(any());

		ResponseEntity<Object> response = userController.deleteUser("nonexistent@example.com");
		assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode()); // Verifica que la respuesta sea 404 Not Found
		assertEquals(HealtIAException.USER_NOT_FOUND, response.getBody());
	}
}
