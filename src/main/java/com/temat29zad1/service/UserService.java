package com.temat29zad1.service;

import com.temat29zad1.repository.PasswordResetTokenRepository;
import com.temat29zad1.repository.UserRepository;
import com.temat29zad1.user.User;
import com.temat29zad1.user.UserDto;
import com.temat29zad1.user.UserMapper;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    private final UserRepository userRepository;

    private final PasswordResetTokenRepository passwordResetTokenRepository;

    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, PasswordResetTokenRepository passwordResetTokenRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordResetTokenRepository = passwordResetTokenRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public Optional<UserDto> findUserDtoByEmail(String email) {
        return userRepository.findUserByEmail(email).map(UserMapper::convertToUserDto);
    }

    public List<UserDto> findAllUsers() {
        return userRepository.findAll().stream().map(UserMapper::convertToUserDto).toList();
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

    public Boolean updateUser(UserDto userDto, String token) {
        if (userDto.getPassword().isEmpty()) {
            userDto.setPassword(userRepository.findById(userDto.getId()).get().getPassword());
        }
        User user = UserMapper.convertToUser(userDto);
        user.setId(userDto.getId());
        try {
            userRepository.updateUserById(user.getFirstName(), user.getLastName(),
                    user.getPassword(), user.getRole(), user.getId());
            if ((token != null)) {
                passwordResetTokenRepository.deleteTokenByToken(token);
            }
            return true;
        } catch (DataIntegrityViolationException e) {
            System.out.println(e.getMessage());
            return false;
        }
    }

    public UserDto findUserDtoById(Long id) {
        return userRepository.findById(id).map(UserMapper::convertToUserDto).get();
    }

    public Optional<User> findUserByEmail(String email) {
        return userRepository.findUserByEmail(email);
    }

    public void deleteTokenByToken(String token) {
        passwordResetTokenRepository.deleteTokenByToken(token);
    }

    public String passwordEncode(String password) {
        return passwordEncoder.encode(password);
    }
}
