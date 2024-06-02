package urinov.shz.kunuz.profile.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class ProfileUpdateUserDTO {
    @NotBlank
    private String name;
    @NotBlank
    private String surname;

}
