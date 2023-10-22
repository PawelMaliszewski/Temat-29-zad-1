package com.temat29zad1.repository;

import com.temat29zad1.roles.Role;
import com.temat29zad1.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findUserByEmail(String email);

    List<User> findAllByRoleEquals(Role role);

    @Query("SELECT u FROM app_user u where u.role != :role")
    List<User> findAllExceptRole(Role role);

    @Modifying
    @Transactional
    @Query("UPDATE app_user SET firstName = :firstName, lastName = :lastName, password = :password, role = :role WHERE id = :id")
    void updateUserById(@Param(value = "firstName") String firstName, @Param(value = "lastName") String lastName,
                        @Param(value = "password") String password,
                        @Param(value = "role") Role role, @Param(value = "id") Long id);
}
