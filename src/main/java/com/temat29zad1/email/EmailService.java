package com.temat29zad1.email;

import com.temat29zad1.user.PasswordResetService;
import com.temat29zad1.user.UserDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

@Service
public class EmailService {

    private final JavaMailSender javaMailSender;
    private final PasswordResetService passwordResetService;

    public EmailService(JavaMailSender javaMailSender, PasswordResetService passwordResetService) {
        this.javaMailSender = javaMailSender;
        this.passwordResetService = passwordResetService;
    }

    private final Logger logger = LoggerFactory.getLogger(EmailService.class);

    public void sendEmail(UserDto userDto) {
        try {
            logger.info("Wysyłam wiadomość");
            MimeMessage mimeMessage = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, "utf-8");
            helper.setFrom("bootcamp@spoko.pl");
            helper.setTo(userDto.getEmail());
            helper.setReplyTo("bootcamp@spoko.pl");
            helper.setSubject("Zmiana hasła");
            helper.setText("Aby ustawić nowe hasło naciśnij na link: " + "<a href="
                           + passwordResetService.generatePasswordResetToken(userDto) + ">link do nowego hasła</a> ,"
                           + " link traci ważność po 30 minutach.", true);
            javaMailSender.send(helper.getMimeMessage());
        } catch (MessagingException e) {
            logger.warn("BŁĄD - nie udało się wysłać wiadomości");
        }
        logger.info("wiadomość wysłana");
    }
}
