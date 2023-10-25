package com.temat29zad1.user;

import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Service
public class PasswordResetService {

    private final PasswordResetTokenRepository passwordResetTokenRepository;
    private final UserRepository userRepository;

    public PasswordResetService(PasswordResetTokenRepository passwordResetTokenRepository, UserRepository userRepository) {
        this.passwordResetTokenRepository = passwordResetTokenRepository;
        this.userRepository = userRepository;
    }

    public Boolean checkIfTokenIsValid(String token) {
        Optional<PasswordResetToken> passToken = passwordResetTokenRepository.findPasswordResetTokenByToken(token);
        return passToken.isPresent() && !passToken.get().getExpiryDateTime().isBefore(LocalDateTime.now());
    }

    public String generatePasswordResetToken(String email) {
        User user = userRepository.findUserByEmail(email).get();
        UUID uuid = UUID.randomUUID();
        PasswordResetToken resetToken = new PasswordResetToken();
        resetToken.setUser(user);
        resetToken.setToken(uuid.toString());
        resetToken.setExpiryDateTime(LocalDateTime.now().plusMinutes(30L));
        resetToken.setUser(user);
        PasswordResetToken token = passwordResetTokenRepository.save(resetToken);
        return token.getToken();
    }
}
