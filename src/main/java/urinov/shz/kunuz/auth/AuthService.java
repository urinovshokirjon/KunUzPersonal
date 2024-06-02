package urinov.shz.kunuz.auth;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import urinov.shz.kunuz.auth.dto.LoginDto;
import urinov.shz.kunuz.auth.dto.UserCreateDTO;
import urinov.shz.kunuz.auth.email.EmailHistoryEntity;
import urinov.shz.kunuz.auth.email.EmailHistoryRepository;
import urinov.shz.kunuz.auth.email.EmailHistoryService;
import urinov.shz.kunuz.auth.sms.SmsHistoryEntity;
import urinov.shz.kunuz.auth.sms.SmsHistoryRepository;
import urinov.shz.kunuz.auth.sms.SmsHistoryService;
import urinov.shz.kunuz.auth.sms.SmsService;
import urinov.shz.kunuz.exp.AppBadException;
import urinov.shz.kunuz.profile.ProfileEntity;
import urinov.shz.kunuz.profile.ProfileRepository;
import urinov.shz.kunuz.profile.ProfileRole;
import urinov.shz.kunuz.profile.ProfileStatus;
import urinov.shz.kunuz.profile.dto.ProfileResponseDTO;
import urinov.shz.kunuz.util.JWTUtil;
import urinov.shz.kunuz.util.MD5Util;
import urinov.shz.kunuz.util.RandomUtil;
import urinov.shz.kunuz.util.Result;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Service
public class AuthService {
    @Autowired
    private ProfileRepository profileRepository;
    @Autowired
    private EmailHistoryRepository emailHistoryRepository;
    @Autowired
    private EmailHistoryService emailHistoryService;
    @Autowired
    private SmsService smsService;
    @Autowired
    private SmsHistoryRepository smsHistoryRepository;
    @Autowired
    private SmsHistoryService smsHistoryService;

    // Profile registration Email
    public Result registrationEmail(UserCreateDTO dto) {
        Boolean existsedByPhoneOrEmail = profileRepository.existsByPhoneOrEmail(dto.getPhone(), dto.getEmail());
        if (existsedByPhoneOrEmail) {
            return new Result("Bunday telefon yoki email oldin ro'yxatga olingan", false);
        }
        ProfileEntity entity = new ProfileEntity();
        entity.setName(dto.getName());
        entity.setSurname(dto.getSurname());
        entity.setEmail(dto.getEmail());
        entity.setPhone(dto.getPhone());
        entity.setPassword(MD5Util.getMD5(dto.getPassword()));

        entity.setCreateDate(LocalDateTime.now());
        entity.setRole(ProfileRole.ROLE_USER);
        entity.setStatus(ProfileStatus.INACTIVE);
        profileRepository.save(entity);

//         Emailga sms yuborish methodini chaqiramiz;

        String emailCode = UUID.randomUUID().toString();
        emailHistoryService.sendEmail(entity.getEmail(), emailCode);


        return new Result("Muvaffaqiyatli ro'yxatdan o'tdingiz. Akkounting ACTIVE qilish uchun email code tasdiqlang", true);

    }


    // Profile verifyEmail
    public Result verifyEmail(String emailCode, String email) {
        Optional<EmailHistoryEntity> historyEntityOptional = emailHistoryRepository.findByMessageAndEmail(emailCode, email);
        if (historyEntityOptional.isEmpty()) {
            return new Result("Email yoki emailCode xato", false);
        }
        Optional<ProfileEntity> profileEntityOptional = profileRepository.findByEmail(email);
        if (profileEntityOptional.isEmpty()) {
            return new Result("Email yoki emailCode xato", false);
        }
        ProfileEntity profileEntity = profileEntityOptional.get();
        profileEntity.setStatus(ProfileStatus.ACTIVE);
        profileRepository.save(profileEntity);
        return new Result("Akkound tasdiqlandi", true);
    }

    // Resent Email code
    public Result verificationResendEmail(String email) {

        Optional<ProfileEntity> profileEntityOptional = profileRepository.findByEmailAndVisibleTrue(email);
        if (profileEntityOptional.isEmpty()) {
            throw new AppBadException("Email not exists");
        }

        ProfileEntity profileEntity = profileEntityOptional.get();

        if (!profileEntity.getVisible() || !profileEntity.getStatus().equals(ProfileStatus.INACTIVE)) {
            throw new AppBadException("Registration not completed");
        }
        emailHistoryService.checkEmailLimit(profileEntity.getEmail());
        String emailCode = UUID.randomUUID().toString();
        emailHistoryService.sendEmail(profileEntity.getEmail(), emailCode);
        return new Result("To complete your registration please verify your email.", true);
    }

    // Profile registration Sms
    public Result registrationSms(UserCreateDTO dto) {
        Boolean existsedByPhoneOrEmail = profileRepository.existsByPhoneOrEmail(dto.getPhone(), dto.getEmail());
        if (existsedByPhoneOrEmail) {
            return new Result("Bunday telefon yoki email oldin ro'yxatga olingan", false);
        }
        ProfileEntity entity = new ProfileEntity();
        entity.setName(dto.getName());
        entity.setSurname(dto.getSurname());
        entity.setEmail(dto.getEmail());
        entity.setPhone(dto.getPhone());
        entity.setPassword(MD5Util.getMD5(dto.getPassword()));

        entity.setCreateDate(LocalDateTime.now());
        entity.setRole(ProfileRole.ROLE_USER);
        entity.setStatus(ProfileStatus.INACTIVE);
        profileRepository.save(entity);

        // Sms yuborish methodini chaqiramiz;

        String message = RandomUtil.getRandomSmsCode();
        String smsCode = "Bu Eskiz dan test";
        smsService.sendSms(dto.getPhone(), smsCode);
        return new Result("Muvaffaqiyatli ro'yxatdan o'tdingiz. Akkounting ACTIVE qilish uchun telefoningizga borgan sms code tasdiqlang", true);

    }

    // Profile verifySms
    public Result verifySms(String smsCode, String phone) { // 12345     915721213
        Optional<SmsHistoryEntity> smsHistoryEntityOptional = smsHistoryRepository.findTopByPhoneOrderByCreateDateDesc(phone);
        if (smsHistoryEntityOptional.isEmpty()) {
            return new Result("Telefon phone yoki smsCode noto'g'ri", false);
        }
        if (!smsHistoryEntityOptional.get().getSmsCode().equals(smsCode)) {
            return new Result("Telefon phone yoki smsCode noto'g'ri", false);
        }
        Optional<ProfileEntity> profileEntityOptional = profileRepository.findByPhone(phone);
        if (profileEntityOptional.isEmpty()) {
            return new Result("Telefon phone yoki smsCode noto'g'ri", false);
        }
        if (!profileEntityOptional.get().getStatus().equals(ProfileStatus.INACTIVE)) {
            return new Result("Telefon phone yoki smsCode noto'g'ri", false);
        }
        ProfileEntity profileEntity = profileEntityOptional.get();
        profileEntity.setStatus(ProfileStatus.ACTIVE);
        profileRepository.save(profileEntity);
        return new Result("Profile ACTIVE holatga o'tdi", true);
    }

    // Resent sms code
    public Result verificationResendSms(String phone) {

        Optional<ProfileEntity> profileEntityOptional = profileRepository.findByPhoneAndVisibleTrue(phone);
        if (profileEntityOptional.isEmpty()) {
            throw new AppBadException("Phone not exists");
        }

        ProfileEntity profileEntity = profileEntityOptional.get();

        if (!profileEntity.getVisible() || !profileEntity.getStatus().equals(ProfileStatus.INACTIVE)) {
            throw new AppBadException("Registration not completed");
        }
        smsHistoryService.checkEmailLimit(profileEntity.getPhone());
        String emailCode = UUID.randomUUID().toString();
        smsService.sendSms(profileEntity.getPhone(), emailCode);
        return new Result("To complete your registration please verify your email.", true);
    }


    // Profile login
    public ProfileResponseDTO loginProfile(LoginDto loginDto) {
        String password = MD5Util.getMD5(loginDto.getPassword());
        Optional<ProfileEntity> profileEntityOptional = profileRepository.findByEmailAndPasswordAndVisibleTrue(loginDto.getUsername(), password);
        if (profileEntityOptional.isEmpty()) {
            throw new AppBadException("Email or password incorrect");
        }
        ProfileEntity profileEntity = profileEntityOptional.get();
        ProfileResponseDTO profileResponseDTO = new ProfileResponseDTO();
        profileResponseDTO.setId(profileEntity.getId());
        profileResponseDTO.setEmail(profileEntity.getEmail());
        profileResponseDTO.setPhone(profileEntity.getPhone());
        profileResponseDTO.setRole(profileEntity.getRole().toString());
        profileResponseDTO.setStatus(profileEntity.getStatus().toString());
        profileResponseDTO.setJwt(JWTUtil.encode(profileEntity.getId(),profileEntity.getRole()));
       return profileResponseDTO;

    }





}
