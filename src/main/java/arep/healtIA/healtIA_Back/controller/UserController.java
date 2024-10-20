package arep.healtIA.healtIA_Back.controller;

import arep.healtIA.healtIA_Back.exception.HealtIAException;
import arep.healtIA.healtIA_Back.model.UserEntity;
import arep.healtIA.healtIA_Back.model.dto.UserDto;
import arep.healtIA.healtIA_Back.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.media.Content;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * The controller of all requests that have to do with users.
 * @author Cristan Duarte.
 * @author Johann Amaya.
 * @author Sebastian Zamora.
 * @version 1.0
 */
@RestController
@RequestMapping("/api/user")
public class UserController {

    private final UserService userService;

    /**
     * Constructor of the class.
     * @param userService: service of the user.
     */
    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    /**
     * Get all users.
     * @return ResponseEntity<Object> Return a json with all users.
     */
    @Operation(summary = "Get all users", description = "This endpoint returns all users.")
    @ApiResponse(responseCode = "200", description = "Return a list of users.", content = @Content)
    @ApiResponse(responseCode = "500", description = "Internal Server Error.", content = @Content)
    @GetMapping
    public ResponseEntity<Object> getAllUsers() {
        try {
            return new ResponseEntity<>(userService.showAllUsers(), HttpStatus.ACCEPTED);
        } catch (Exception e) {
            Logger.getLogger(UserEntity.class.getName()).log(Level.SEVERE, null, e);
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Get a specific user.
     * @param email: email of the user to get.
     * @return ResponseEntity<Object> Return a json with the user's information.
     */
    @Operation(summary = "Get a specific user", description = "This endpoint returns a specific user by email.")
    @ApiResponse(responseCode = "200", description = "Return the user.", content = @Content)
    @ApiResponse(responseCode = "404", description = "User not found.", content = @Content)
    @GetMapping("/getUser/{email}")
    public ResponseEntity<Object> getUser(@PathVariable String email) {
        try {
            UserEntity user = userService.getUser(email);
            if(user != null){
                return new ResponseEntity<>(user, HttpStatus.ACCEPTED);
            }else{
                return new ResponseEntity<>(HealtIAException.USER_NOT_FOUND, HttpStatus.NOT_FOUND);
            }
        } catch (HealtIAException e) {
            if (e.getMessage().equals(HealtIAException.USER_NOT_FOUND)) {
                return new ResponseEntity<>(HealtIAException.USER_NOT_FOUND, HttpStatus.NOT_FOUND);
            }
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (Exception e) {
            Logger.getLogger(UserEntity.class.getName()).log(Level.SEVERE, null, e);
            return new ResponseEntity<>("Internal server error", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    /**
     * Create a user.
     * @param userDto: user to create.
     * @return ResponseEntity<Object> with the state of the operation.
     */
    @Operation(summary = "Create a new user", description = "This endpoint creates a new user.")
    @ApiResponse(responseCode = "201", description = "User created successfully.", content = @Content)
    @ApiResponse(responseCode ="409", description = "User already exists.", content = @Content)
    @ApiResponse(responseCode = "500", description = "Internal Server Error.", content = @Content)
    @PostMapping("/create")
    public ResponseEntity<Object> createUser(@RequestBody UserDto userDto) {
        try {
            userService.createUser(userDto);
            return new ResponseEntity<>("User created successfully", HttpStatus.CREATED);
        } catch (Exception e) {
            Logger.getLogger(UserEntity.class.getName()).log(Level.SEVERE, null, e);
            if(e.getMessage().contains(HealtIAException.USER_ALREADY_EXISTS)){
                return new ResponseEntity<>(e.getMessage(), HttpStatus.CONFLICT);
            }else {
                return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }
    }

    /**
     * Update a user.
     * @param email: email of the user to update.
     * @return ResponseEntity<Object> with the state of the operation.
     */
    @Operation(summary = "Update a user", description = "This endpoint updates a user by email.")
    @ApiResponse(responseCode = "200", description = "User updated successfully.", content = @Content)
    @ApiResponse(responseCode = "404", description = "User not found.", content = @Content)
    @ApiResponse(responseCode = "500", description = "Internal Server Error.", content = @Content)
    @PutMapping("/update/{email}")
    public ResponseEntity<Object> updateUser(@RequestBody UserDto userDto, @PathVariable String email) {
        try {
            userService.updateUser(userDto, email);
            return new ResponseEntity<>("User updated successfully", HttpStatus.ACCEPTED);
        } catch (Exception e) {
            Logger.getLogger(UserEntity.class.getName()).log(Level.SEVERE, null, e);
            if(e.getMessage().contains(HealtIAException.USER_NOT_FOUND)){
                return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
            }else {
                return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }
    }

    /**
     * Delete a user.
     * @param email: email of the user to delete.
     * @return ResponseEntity<Object> with the state of the operation.
     */
    @Operation(summary = "Delete a user", description = "This endpoint deletes a user by email.")
    @ApiResponse(responseCode = "200", description = "User deleted successfully.", content = @Content)
    @ApiResponse(responseCode = "404", description = "User not found.", content = @Content)
    @ApiResponse(responseCode = "500", description = "Internal Server Error.", content = @Content)
    @DeleteMapping("/delete/{email}")
    public ResponseEntity<Object> deleteUser(@PathVariable String email) {
        try {
            userService.deleteUser(email);
            return new ResponseEntity<>("User deleted successfully", HttpStatus.ACCEPTED);
        } catch (Exception e) {
            Logger.getLogger(UserEntity.class.getName()).log(Level.SEVERE, null, e);
            if(e.getMessage().contains(HealtIAException.USER_NOT_FOUND)){
                return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
            }else {
                return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }
    }
}
