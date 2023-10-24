package com.temat29zad1.user;

import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Service
public class PasswordResetService {

    private final PasswordResetTokenRepository passwordResetTokenRepository;

    public PasswordResetService(PasswordResetTokenRepository passwordResetTokenRepository) {
        this.passwordResetTokenRepository = passwordResetTokenRepository;
    }


    public Optional<PasswordResetToken> returnTokenIfValidated(String token) {
        Optional<PasswordResetToken> passToken = passwordResetTokenRepository.findPasswordResetTokenByToken(token);
        if (passToken.isEmpty() || passToken.get().getExpiryDateTime().isBefore(LocalDateTime.now())) {
            return Optional.empty();
        }
        return passToken;
    }

    public void deleteExpiredTokenIfExists(Long id) {
        Optional<PasswordResetToken> byUserId = passwordResetTokenRepository.findByUser_Id(id);
        if (byUserId.isPresent()) {
            if (returnTokenIfValidated(byUserId.get().getToken()).isEmpty()) {
                passwordResetTokenRepository.deleteTokenByToken(byUserId.get().getToken());
            }
        }
    }

    public String generatePasswordResetToken(UserDto userDto) {
        User user = UserMapper.convertToUser(userDto);
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
