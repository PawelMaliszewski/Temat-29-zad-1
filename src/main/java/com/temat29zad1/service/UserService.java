package com.temat29zad1.service;

import com.temat29zad1.repository.UserRepository;
import com.temat29zad1.roles.Role;
import com.temat29zad1.user.User;
import com.temat29zad1.user.UserDto;
import com.temat29zad1.user.UserMapper;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.sql.SQLException;
import java.sql.SQLWarning;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
public class UserService {

    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public Optional<UserDto> findUserDtoByEmail(String email) {
        return userRepository.findUserByEmail(email).map(UserMapper::convertToUserDto);
    }

    public Boolean register(UserDto userDto) {
        User user = UserMapper.convertToUser(userDto);
        user.setPassword(passwordEncoder.encode(userDto.getPassword()));
        try {
            userRepository.save(user);
            return true;
        } catch (DataIntegrityViolationException e) {
            System.out.println(e.getMessage());
            return false;
        }
    }

    public Boolean updateUser(UserDto userDto) {
        User user = UserMapper.convertToUser(userDto);
        String password = "";
        if (userDto.getPassword().isEmpty()) {
            password = userRepository.findById(userDto.getId()).get().getPassword();
        } else {
            password = passwordEncoder.encode(userDto.getPassword());
        }
        user.setId(userDto.getId());
        try {
            userRepository.updateUserById(user.getFirstName(), user.getLastName(),
                    password, user.getRole(), user.getId());
            return true;
        } catch (DataIntegrityViolationException e) {
            System.out.println(e.getMessage());
            return false;
        }
    }
}
