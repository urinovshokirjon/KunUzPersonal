package urinov.shz.kunuz.profile;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import urinov.shz.kunuz.auth.dto.JwtDTO;
import urinov.shz.kunuz.profile.dto.*;
import urinov.shz.kunuz.util.HttpRequestUtil;
import urinov.shz.kunuz.util.Result;

@RestController
@RequestMapping("/profile")
public class ProfileController {
    @Autowired
    private ProfileService profileService;

    //  1. Create profile (ADMIN)
    @PostMapping("/create")
    public ResponseEntity<Result> createProfile(@RequestBody ProfileCreateDTO profileCreateDTO,
                                                HttpServletRequest request){

        Result result =profileService.createProfile(profileCreateDTO);
        return ResponseEntity.status(result.isSuccess()? HttpStatus.CREATED:HttpStatus.BAD_REQUEST).body(result);
    }
    //  2. Update Profile (ADMIN)
    @PutMapping("/update/{id}")
    public ResponseEntity<Result> update(@PathVariable("id") Integer id,
                                          @Valid @RequestBody ProfileUpdateAdminDTO profile) {
        Result result =profileService.update(id, profile);
        return ResponseEntity.status(result.isSuccess()? HttpStatus.CREATED:HttpStatus.BAD_REQUEST).body(result);
    }

    // 3. Update Profile Detail (ANY) (Profile updates own details)
    @PutMapping("/update-own/{id}")
    public ResponseEntity<Result> updateProfileOwe(@PathVariable("id") Integer id,
                                                   @Valid  @RequestBody ProfileUpdateUserDTO profileUpdateDTO){

        Result result =profileService.updateProfileOwe(id,profileUpdateDTO);
        return ResponseEntity.status(result.isSuccess()? HttpStatus.OK:HttpStatus.CONFLICT).body(result);
    }

   // 4. Profile List (ADMIN) (Pagination)
    @GetMapping("/page")
    public ResponseEntity<PageImpl<ProfileResponseDTO>> getProfilePage(@RequestParam int page,
                                                                       @RequestParam int size){
        PageImpl<ProfileResponseDTO> profileResponseDTOPage=profileService.getProfilePage(page-1,size);
        return ResponseEntity.status(HttpStatus.OK).body(profileResponseDTOPage);
    }

    // 5. Delete Profile By Id (ADMIN)
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Result> deleteProfile(@PathVariable int id){

        Result result =profileService.deleteProfile(id);
        return ResponseEntity.status(result.isSuccess()? HttpStatus.OK:HttpStatus.CONFLICT).body(result);
    }

    //  7. Filter (name,surname,phone,role,created_date_from,created_date_to)
    @PostMapping("page-filter")
    public ResponseEntity<PageImpl<ProfileResponseDTO>> getProfilePageFilter(
            @RequestParam("page") int page,
            @RequestParam("size") int size,
            @RequestBody ProfileFilterDTO profileFilterDTO){

      PageImpl<ProfileResponseDTO> profileResponseDTOPage= profileService.getProfilePageFilter(page-1,size,profileFilterDTO);
      return ResponseEntity.ok().body(profileResponseDTOPage);
    }




}
