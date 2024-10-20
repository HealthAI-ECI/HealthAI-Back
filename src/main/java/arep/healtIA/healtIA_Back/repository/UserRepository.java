package arep.healtIA.healtIA_Back.repository;

import arep.healtIA.healtIA_Back.model.UserEntity;
import org.springframework.data.domain.Example;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Repository of the user.
 * @author Cristan Duarte.
 * @author Johann Amaya.
 * @author Sebastian Zamora.
 * @version 1.0
 */
@Repository
public interface UserRepository extends MongoRepository<UserEntity, String> {

    /**
     * Find a user by email.
     * @param email: email of the user.
     * @return Optional<UserEntity> Return the user.
     */
    Optional<UserEntity> findByEmail(String email);

    /**
     * Find a user by full name.
     * @param username: full name of the user.
     * @return Optional<UserEntity> Return the user.
     */
    Optional<UserEntity> findByFullName(String username);
}
