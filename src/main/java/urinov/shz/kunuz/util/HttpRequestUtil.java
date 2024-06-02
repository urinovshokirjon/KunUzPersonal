package urinov.shz.kunuz.util;

import jakarta.servlet.http.HttpServletRequest;
import urinov.shz.kunuz.auth.dto.JwtDTO;
import urinov.shz.kunuz.exp.AppForbiddenException;
import urinov.shz.kunuz.profile.ProfileRole;


public class HttpRequestUtil {

    public static JwtDTO getJwtDTO(HttpServletRequest request) {
        Integer id = (Integer) request.getAttribute("id");
        ProfileRole role = (ProfileRole) request.getAttribute("role");

        JwtDTO dto =new JwtDTO(id,role);
        return dto;
    }

    public static JwtDTO getJwtDTO(HttpServletRequest request, ProfileRole requiredRole) {
        Integer id = (Integer) request.getAttribute("id");
        ProfileRole role = (ProfileRole) request.getAttribute("role");
        JwtDTO dto = new JwtDTO(id, role);

        if (!dto.getRole().equals(requiredRole)) {
            throw new AppForbiddenException("Method not allowed");
        }
        return dto;
    }
}
