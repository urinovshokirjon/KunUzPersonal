package urinov.shz.kunuz.profile;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import urinov.shz.kunuz.exp.AppBadException;
import urinov.shz.kunuz.profile.dto.*;
import urinov.shz.kunuz.util.FilterResponseDTO;
import urinov.shz.kunuz.util.MD5Util;
import urinov.shz.kunuz.util.Result;

import java.util.ArrayList;
import java.util.List;

@Service
public class ProfileService {

    @Autowired
    private ProfileRepository profileRepository;
    @Autowired
    private ProfileFilterRepository profileFilterRepository;

    //  1. Create profile (ADMIN)
    public Result createProfile(ProfileCreateDTO profileCreateDTO) {
        Boolean existsByPhoneOrEmail = profileRepository.existsByPhoneOrEmail(profileCreateDTO.getPhone(), profileCreateDTO.getEmail());

        if (existsByPhoneOrEmail) {
            return new Result("Phone yoki email oldin ro'yxatdan o'tgan",false);
        }

        ProfileEntity profileEntity = new ProfileEntity();
        profileEntity.setName(profileCreateDTO.getName());
        profileEntity.setSurname(profileCreateDTO.getSurname());
        profileEntity.setPhone(profileCreateDTO.getPhone());
        profileEntity.setEmail(profileCreateDTO.getEmail());
        profileEntity.setPassword(MD5Util.getMD5(profileCreateDTO.getPassword()));
        profileEntity.setStatus(profileCreateDTO.getStatus());
        profileEntity.setRole(profileCreateDTO.getRole());
        profileRepository.save(profileEntity);
        return new Result("Foydalanuvchi yaratildi",true);
    }

    //  2. Update Profile (ADMIN)
    public Result update(Integer id, ProfileUpdateAdminDTO profile) {
        ProfileEntity profileEntity = getProfileById(id);
        profileEntity.setName(profile.getName());
        profileEntity.setSurname(profile.getSurname());
        profileEntity.setStatus(profile.getStatus());
        profileEntity.setRole(profile.getRole());

//        profileEntity.setEmail(profile.getEmail());
//        profileEntity.setPhone(profile.getPhone());
//        profileEntity.setPassword(profile.getPassword());
        profileRepository.save(profileEntity);
        return new Result("Foydalanuvchi o'zgartirildi",true);
    }


   // 3. Update Profile Detail (ANY) (Profile updates own details)
    public Result updateProfileOwe(int id, ProfileUpdateUserDTO profileUpdateDTO) {

        ProfileEntity profileEntity = getProfileById(id);
        profileEntity.setName(profileUpdateDTO.getName());
        profileEntity.setSurname(profileUpdateDTO.getSurname());
//        profileEntity.setPhone(profileUpdateDTO.getPhone());
//        profileEntity.setEmail(profileCreateDTO.getEmail());
//        profileEntity.setPassword(profileCreateDTO.getPassword());
        profileRepository.save(profileEntity);
        return new Result("Foydalanuvchi tahrirlandi",true);

    }

    // 4. Profile List (ADMIN) (Pagination)
    public PageImpl<ProfileResponseDTO> getProfilePage(int page, int size) {

        Pageable pageable= PageRequest.of(page, size);
        Page<ProfileEntity> profileEntities = profileRepository.findAllByVisibleTrueOrderById(pageable);
        List<ProfileResponseDTO> profileResponseDTOList = profileEntities.stream().map(this::getProfileResponseDTO).toList();

        return new PageImpl<>(profileResponseDTOList,pageable,profileEntities.getTotalElements());

    }

    // 5. Delete Profile By Id (ADMIN)
    public Result deleteProfile(int id) {
        ProfileEntity profileEntity = getProfileById(id);
        profileEntity.setVisible(false);
        profileRepository.save(profileEntity);
//        profileRepository.delete(profileEntity);
        return new Result("Foydalanuvchi o'chirildi",true);
    }

    //  7. Filter (name,surname,phone,role,created_date_from,created_date_to)
    public PageImpl<ProfileResponseDTO> getProfilePageFilter(int page, int size, ProfileFilterDTO profileFilterDTO) {

        List<ProfileResponseDTO> profileResponseDTOList=new ArrayList<>();

        FilterResponseDTO<ProfileEntity> profileEntityFilterList=profileFilterRepository.getProfileFilterPage(profileFilterDTO,page,size);

        for (ProfileEntity profileEntity : profileEntityFilterList.getContent()) {
            ProfileResponseDTO profileResponseDTO = getProfileResponseDTO(profileEntity);
            profileResponseDTOList.add(profileResponseDTO);
        }
        return new PageImpl<>(profileResponseDTOList,PageRequest.of(page,size),profileEntityFilterList.getTotalCount());
    }


    public ProfileEntity getProfileById(int id) {
      return   profileRepository.findById(id).orElseThrow(()->{
            throw new AppBadException("Bunday profile topilmadi");
        });
    }

    public ProfileResponseDTO getProfileResponseDTO(ProfileEntity profileEntity) {
        ProfileResponseDTO profileResponseDTO = new ProfileResponseDTO();
        profileResponseDTO.setId(profileEntity.getId());
        profileResponseDTO.setName(profileEntity.getName());
        profileResponseDTO.setSurname(profileEntity.getSurname());
        profileResponseDTO.setPhone(profileEntity.getPhone());
        profileResponseDTO.setEmail(profileEntity.getEmail());
        profileResponseDTO.setRole(profileEntity.getRole().toString());
        profileResponseDTO.setStatus(profileEntity.getStatus().toString());
        profileResponseDTO.setCreateDate(profileEntity.getCreateDate());
        return profileResponseDTO;
    }



}
