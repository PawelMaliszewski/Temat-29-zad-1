package com.temat29zad1.repository;

import com.temat29zad1.passwordResetservice.PasswordResetToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

public interface PasswordResetTokenRepository extends JpaRepository<PasswordResetToken, Long> {

    Optional<PasswordResetToken> findPasswordResetTokenByToken(String token);

    @Transactional
    @Modifying
    @Query("DELETE PasswordResetToken t WHERE t.user.id = :id")
    void deleteTokenByUserId(Long id);

    @Transactional
    @Modifying
    @Query("DELETE PasswordResetToken t WHERE t.token = :token")
    void deleteTokenByToken(String token);
}
