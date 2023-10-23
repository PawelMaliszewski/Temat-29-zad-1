package com.temat29zad1.service;

import com.temat29zad1.user.User;
import com.temat29zad1.passwordResetservice.PasswordResetToken;
import com.temat29zad1.repository.PasswordResetTokenRepository;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class EmailService {

    private final JavaMailSender javaMailSender;
    private final PasswordResetTokenRepository passwordResetTokenRepository;

    public EmailService(JavaMailSender javaMailSender, PasswordResetTokenRepository passwordResetTokenRepository) {
        this.javaMailSender = javaMailSender;
        this.passwordResetTokenRepository = passwordResetTokenRepository;
    }

    private final Logger logger = LoggerFactory.getLogger(EmailService.class);

    public void sendEmail(User user) {
        try {
            logger.info("Wysyłam wiadomość");
            MimeMessage mimeMessage = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, "utf-8");
            helper.setFrom("bootcamp@spoko.pl");
            helper.setTo(user.getEmail());
            helper.setReplyTo("bootcamp@spoko.pl");
            helper.setSubject("Zmiana hasła");
            helper.setText("Aby ustawić nowe hasło naciśnij na link: " + "<a href="
                           + generatePasswordResetToken(user) + ">link do nowego hasła</a> , link traci ważnośc po 30 minutach.", true);
            javaMailSender.send(helper.getMimeMessage());
        } catch (MessagingException e) {
            logger.warn("BŁĄD - nie udało się wysłać wiadomości");
        }
        logger.info("wiadomość wysłana");
    }

    private String generatePasswordResetToken(User user) {
        UUID uuid = UUID.randomUUID();
        LocalDateTime currentDateTime = LocalDateTime.now();
        LocalDateTime expiryDateTime = currentDateTime.plusMinutes(30L);
        PasswordResetToken resetToken = new PasswordResetToken();
        resetToken.setUser(user);
        resetToken.setToken(uuid.toString());
        resetToken.setExpiryDateTime(expiryDateTime);
        resetToken.setUser(user);
        PasswordResetToken token = passwordResetTokenRepository.save(resetToken);
        if (token != null) {
            String endPointUrl = "http://localhost:8080/set-new-password";
            return endPointUrl + "/" + resetToken.getToken();
        }
        return "błąd !";
    }
}
