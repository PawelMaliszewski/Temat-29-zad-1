package com.temat29zad1.user;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
 interface PasswordResetTokenRepository extends JpaRepository<PasswordResetToken, Long> {

    Optional<PasswordResetToken> findPasswordResetTokenByToken(String token);

    @Transactional
    @Modifying
    @Query("DELETE PasswordResetToken t WHERE t.token = :token")
    void deleteTokenByToken(String token);

    Optional<PasswordResetToken> findByUser_Id(Long id);
}
