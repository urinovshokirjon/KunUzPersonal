package urinov.shz.kunuz.profile.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import urinov.shz.kunuz.profile.ProfileRole;
import urinov.shz.kunuz.profile.ProfileStatus;

@Data
public class ProfileUpdateAdminDTO {
    @NotBlank
    private String name;
    @NotBlank
    private String surname;

    private ProfileStatus status;

    private ProfileRole role;

}
