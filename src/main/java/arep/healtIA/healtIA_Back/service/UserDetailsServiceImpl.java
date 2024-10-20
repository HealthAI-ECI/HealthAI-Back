package arep.healtIA.healtIA_Back.service;

import arep.healtIA.healtIA_Back.model.RoleEntity;
import arep.healtIA.healtIA_Back.model.UserEntity;
import arep.healtIA.healtIA_Back.repository.UserRepository;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import java.util.Collections;
import java.util.Optional;

/**
 * This class is used to implement the UserDetailsService interface, which is used to load user-specific data.
 * @author Cristan Duarte.
 * @author Johann Amaya.
 * @author Sebastian Zamora.
 * @version 1.0
 */
@Service
public class UserDetailsServiceImpl implements UserDetailsService {
    private final UserRepository userRepository;

    /**
     * Constructor of the class.
     * @param userRepository the repository of the user.
     */
    public UserDetailsServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    /**
     * This method is used to load the user by the username.
     * @param username the username of the user.
     * @return the user details.
     * @throws UsernameNotFoundException if the user is not found.
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<UserEntity> userByEmail = userRepository.findByFullName(username);
        UserEntity user = userByEmail.orElseThrow(() -> new UsernameNotFoundException("User not found"));
        RoleEntity role = user.getRole();
        GrantedAuthority authority = new SimpleGrantedAuthority("ROLE_" + role.name());
        return new User(user.getFullName(), user.getHashPassword(), true, true, true, true, Collections.singletonList(authority));
    }
}
