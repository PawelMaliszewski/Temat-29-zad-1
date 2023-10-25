package com.temat29zad1.email;

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

    public EmailService(JavaMailSender javaMailSender) {
        this.javaMailSender = javaMailSender;
    }

    private final Logger logger = LoggerFactory.getLogger(EmailService.class);

    public void sendPasswordResetEmail(String email, String linkWithToken) {
        try {
            logger.info("Wysyłam wiadomość");
            MimeMessage mimeMessage = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, "utf-8");
            helper.setFrom("bootcamp@spoko.pl");
            helper.setTo(email);
            helper.setReplyTo("bootcamp@spoko.pl");
            helper.setSubject("Zmiana hasła");
            helper.setText("Aby ustawić nowe hasło naciśnij na link: " + "<a href="
                           + linkWithToken + ">link do nowego hasła</a> ,"
                           + " link traci ważność po 30 minutach.", true);
            javaMailSender.send(helper.getMimeMessage());
        } catch (MessagingException e) {
            logger.warn("BŁĄD - nie udało się wysłać wiadomości");
        }
        logger.info("wiadomość wysłana");
    }
}
