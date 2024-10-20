package arep.healtIA.healtIA_Back.service;

import arep.healtIA.healtIA_Back.exception.HealtIAException;
import arep.healtIA.healtIA_Back.model.UserEntity;
import arep.healtIA.healtIA_Back.model.dto.UserDto;
import arep.healtIA.healtIA_Back.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * All services that need to be done with the user.
 * @author Cristan Duarte.
 * @author Johann Amaya.
 * @author Sebastian Zamora.
 * @version 1.0
 */
@Service
public class UserService {

    private final UserRepository userRepository;
    public final PasswordEncoder passwordEncoder;

    /**
     * Contructor of all services that need to be done with the user.
     * @param userRepository repository of the user.
     */
    @Autowired
    public UserService(UserRepository userRepository, PasswordEncoder paswordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = paswordEncoder;
    }

    /**
     * Show all users.
     * @return List<UserEntity> Return a list with all users.
     */
    public List<UserEntity> showAllUsers() {
        return userRepository.findAll();
    }

    /**
     * Get a specific user.
     * @param email: email of the user to get.
     * @return UserEntity Return the user.
     * @throws HealtIAException If the user is not found.
     */
    public UserEntity getUser(String email) throws HealtIAException {
        Optional<UserEntity> user = userRepository.findByEmail(email);
        if(user.isPresent()){
            return user.get();
        }else{
            throw new HealtIAException(HealtIAException.USER_NOT_FOUND);
        }
    }

    /**
     * Create a user.
     * @param userDto: The basic information of the user.
     * @throws HealtIAException
     */
    public void createUser(UserDto userDto) throws HealtIAException {
        Optional<UserEntity> user = userRepository.findByEmail(userDto.getEmail());
        if(user.isEmpty()){
            userRepository.save(new UserEntity(userDto.getFullName(), userDto.getEmail(), passwordEncoder.encode(userDto.getHashPassword()), userDto.getBornDate()));
        }else {
            throw new HealtIAException(HealtIAException.USER_ALREADY_EXISTS);
        }
    }

    /**
     * Update a user.
     * @param userDto: The new information of the user.
     * @param email: email of the user to update.
     * @throws HealtIAException
     */
    public void updateUser(UserDto userDto, String email) throws HealtIAException {
        Optional<UserEntity> user = userRepository.findByEmail(email);
        if(user.isPresent()){
            UserEntity userEntity = user.get();
            userEntity.setFullName(userDto.getFullName());
            userEntity.setEmail(userDto.getEmail());
            userEntity.setHashPassword(userDto.getHashPassword());
            userEntity.setPhone(userDto.getPhone());
            userEntity.setBornDate(userDto.getBornDate());
            userEntity.setCity(userDto.getCity());
            userEntity.setRole(userDto.getRole());
            userEntity.setBloodGroup(userDto.getBloodGroup());
            userEntity.setWeight(userDto.getWeight());
            userEntity.setHeight(userDto.getHeight());
            userRepository.save(userEntity);
        }else{
            throw new HealtIAException(HealtIAException.USER_NOT_FOUND);
        }
    }

    /**
     * Delete a user.
     * @param email: email of the user to delete.
     * @throws HealtIAException
     */
    public void deleteUser(String email) throws HealtIAException {
        Optional<UserEntity> user = userRepository.findByEmail(email);
        if(user.isPresent()){
            userRepository.delete(user.get());
        }else{
            throw new HealtIAException(HealtIAException.USER_NOT_FOUND);
        }
    }
}
