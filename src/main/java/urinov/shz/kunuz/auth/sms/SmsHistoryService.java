package urinov.shz.kunuz.auth.sms;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import urinov.shz.kunuz.exp.AppBadException;

import java.time.LocalDateTime;

@Service
public class SmsHistoryService {
    @Autowired
    private SmsHistoryRepository smsHistoryRepository;

    public void createSmsHistory(String smsCode, String phone) {
        SmsHistoryEntity smsHistoryEntity = new SmsHistoryEntity();
        smsHistoryEntity.setSmsCode(smsCode);
        smsHistoryEntity.setPhone(phone);
        smsHistoryEntity.setCreateDate(LocalDateTime.now());
        smsHistoryRepository.save(smsHistoryEntity);

    }

    public void checkEmailLimit(String phone) {

        LocalDateTime to = LocalDateTime.now();
        LocalDateTime from = to.minusMinutes(2);

        long count = smsHistoryRepository.countByPhoneAndCreateDateBetween(phone, from, to);
        if (count >= 3) {
            throw new AppBadException("Sms limit reached. Please try after some time");
        }
    }

}
