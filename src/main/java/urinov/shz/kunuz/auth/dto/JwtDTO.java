package urinov.shz.kunuz.auth.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import urinov.shz.kunuz.profile.ProfileRole;


@Setter
@Getter
public class JwtDTO {
    private Integer id;
    private ProfileRole role;
    private String username;

    public JwtDTO(Integer id) {
        this.id = id;
    }

    public JwtDTO(Integer id, ProfileRole role, String username) {
        this.id = id;
        this.role = role;
        this.username = username;
    }
    public JwtDTO(Integer id, ProfileRole role) {
        this.id = id;
        this.role = role;
    }

}
