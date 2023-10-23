package com.temat29zad1.service;

import com.temat29zad1.passwordResetservice.PasswordResetToken;
import com.temat29zad1.repository.PasswordResetTokenRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class PasswordResetService {

    private final PasswordResetTokenRepository passwordResetTokenRepository;

    public PasswordResetService(PasswordResetTokenRepository passwordResetTokenRepository) {
        this.passwordResetTokenRepository = passwordResetTokenRepository;
    }

    public Optional<PasswordResetToken> returnTokenIfValidated(String token) {
        LocalDateTime l = LocalDateTime.now();
        Optional<PasswordResetToken> passToken = passwordResetTokenRepository.findPasswordResetTokenByToken(token);
        if (passToken.isEmpty() || passToken.get().getExpiryDateTime().isBefore(LocalDateTime.now())){
            return Optional.empty();
        };
        return passToken;
    }
}
