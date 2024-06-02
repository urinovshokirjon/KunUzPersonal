package urinov.shz.kunuz.auth.email;

import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import urinov.shz.kunuz.exp.AppBadException;


import java.time.LocalDateTime;

@Service
public class EmailHistoryService {
    @Autowired
    private EmailHistoryRepository emailHistoryRepository;
    @Autowired
    private JavaMailSender javaMailSender;

    public void createEmailHistory(String emailCode, String toEmail) {
        EmailHistoryEntity emailHistoryEntity = new EmailHistoryEntity();
        emailHistoryEntity.setMessage(emailCode);
        emailHistoryEntity.setEmail(toEmail);
        emailHistoryEntity.setCreateDate(LocalDateTime.now());
        emailHistoryRepository.save(emailHistoryEntity);

    }

    public void checkEmailLimit(String email) {

        LocalDateTime to = LocalDateTime.now();
        LocalDateTime from = to.minusMinutes(2);

        long count = emailHistoryRepository.countByEmailAndCreateDateBetween(email,from,to);
        if(count >=3) {
            throw new AppBadException("Sms limit reached. Please try after some time");
        }
    }

    // Profile sendEmail
    public void sendEmail(String sendingEmail, String emailCode) {
        try {
            MimeMessage msg = javaMailSender.createMimeMessage();
            msg.setFrom("urinov@gmail.uz");
            MimeMessageHelper helper = null;
            helper = new MimeMessageHelper(msg, true);
            helper.setTo(sendingEmail);
            helper.setSubject("Accountni tasdiqlash");

            String formatText = "<style>\n" +
                    "    a:link, a:visited {\n" +
                    "        background-color: #f44336;\n" +
                    "        color: white;\n" +
                    "        padding: 14px 25px;\n" +
                    "        text-align: center;\n" +
                    "        text-decoration: none;\n" +
                    "        display: inline-block;\n" +
                    "    }\n" +
                    "\n" +
                    "    a:hover, a:active {\n" +
                    "        background-color: red;\n" +
                    "    }\n" +
                    "</style>\n" +
                    "<div style=\"text-align: center\">\n" +
                    "    <h1>Welcome to kun.uz web portal</h1>\n" +
                    "    <br>\n" +
                    "    <p>Please button lick below to complete registration</p>\n" +
                    "    <div style=\"text-align: center\">\n" +
                    "        <a href=\"%s\" target=\"_blank\">This is a link</a>\n" +
                    "    </div>";

//            String url = "http://192.168.1.251:8080/auth/verifyEmail?emailCode=" + emailCode + "&email=" + sendingEmail;
            String url = "http://localhost:8080/auth/verifyEmail?emailCode=" + emailCode + "&email=" + sendingEmail;
            String text = String.format(formatText, url);
            helper.setText(text, true);
            javaMailSender.send(msg);
            createEmailHistory(emailCode, sendingEmail);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

}
