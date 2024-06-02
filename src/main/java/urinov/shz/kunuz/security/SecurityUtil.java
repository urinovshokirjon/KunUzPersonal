package urinov.shz.kunuz.security;


import urinov.shz.kunuz.auth.dto.JwtDTO;
import urinov.shz.kunuz.exp.AppForbiddenException;
import urinov.shz.kunuz.profile.ProfileRole;
import urinov.shz.kunuz.util.JWTUtil;

public class SecurityUtil {

    public static JwtDTO getJwtDTO(String token) {
        String jwt = token.substring(7); // Bearer eyJhb
        JwtDTO dto = JWTUtil.decode(jwt);
        return dto;
    }

    public static JwtDTO getJwtDTO(String token, ProfileRole requiredRole) {
        JwtDTO dto = getJwtDTO(token);
        if(!dto.getRole().equals(requiredRole)){
            throw new AppForbiddenException("Method not allowed");
        }
        return dto;
    }
}
