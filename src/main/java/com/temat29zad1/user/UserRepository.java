package com.temat29zad1.user;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findUserByEmail(String email);

    List<User> findAllByRoleEquals(Role role);

    @Modifying
    @Transactional
    @Query("UPDATE app_user SET firstName = :firstName, lastName = :lastName, email = :email, password = :password, role = :role WHERE id = :id")
    void updateUserById(@Param(value = "firstName") String firstName, @Param(value = "lastName") String lastName,
                        @Param(value = "email") String email, @Param(value = "password") String password,
                        @Param(value = "role") Role role, @Param(value = "id") Long id);

    Optional<User> findUserByPasswordResetToken_Token(String token);

    boolean existsByEmail(String email);

}
