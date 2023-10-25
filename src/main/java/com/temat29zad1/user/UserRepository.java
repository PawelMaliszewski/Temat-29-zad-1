package com.temat29zad1.user;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findUserByEmail(String email);

    Optional<User> findUserByPasswordResetToken_Token(String token);

    boolean existsByEmail(String email);

}
