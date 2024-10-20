package arep.healtIA.healtIA_Back.model;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.UUID;

/**
 * Entity of the user.
 * @author Cristan Naranjo.
 * @author Johann Amaya.
 * @author Sebastian Zamora.
 * @version 1.0
 */

@Data
@NoArgsConstructor
@Document(collection = "User")
public class UserEntity {
    @Id
    private String id;
    @NotBlank
    private String fullName;
    @Email
    @NotBlank
    private String email;
    @NotBlank
    private String hashPassword;
    @NotBlank
    @Max(10)
    private String phone;
    @NotBlank
    private String bornDate;
    @NotBlank
    private String city;
    @NotBlank
    private RoleEntity role;
    @NotBlank
    @Max(3)
    private String bloodGroup;
    @NotBlank
    @Max(3)
    private String weight;
    @NotBlank
    @Max(3)
    private String height;

    /**
     * Constructor of the user.
     * @param fullName: Full name of the user.
     * @param email: Email of the user.
     * @param hashPassword: Password of the user. Phone of the user.
     * @param bornDate: Born date of the user.
     */
    public UserEntity(@NotNull String fullName,@NotNull String email,@NotNull String hashPassword,@NotNull String bornDate) {
        this.fullName = fullName;
        this.email = email;
        this.hashPassword = hashPassword;
        this.bornDate = bornDate;
        this.role = RoleEntity.PATIENT;
        this.id = role.name() +"-"+ UUID.randomUUID().toString();
    }
}
