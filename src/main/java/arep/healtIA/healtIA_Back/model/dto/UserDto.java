package arep.healtIA.healtIA_Back.model.dto;

import arep.healtIA.healtIA_Back.model.RoleEntity;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Data transfer object of the user.
 * @author Cristan Duarte.
 * @author Johann Amaya.
 * @author Sebastian Zamora.
 * @version 1.0
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserDto {
    @NotBlank
    private String fullName;
    @NotBlank
    @Email
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
}
