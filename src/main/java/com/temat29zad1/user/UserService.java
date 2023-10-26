package com.temat29zad1.user;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    private final PasswordResetService passwordResetService;

    private final PasswordResetTokenRepository passwordResetTokenRepository;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder, PasswordResetService passwordResetService, PasswordResetTokenRepository passwordResetTokenRepository) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.passwordResetService = passwordResetService;
        this.passwordResetTokenRepository = passwordResetTokenRepository;
    }

    public Optional<UserDto> findUserDtoByEmail(String email) {
        return userRepository.findUserByEmail(email).map(UserMapper::convertToUserDto);
    }

    public List<UserDto> findAllUsers() {
        return userRepository.findAll().stream().map(UserMapper::convertToUserDto).toList();
    }

    public Boolean registerUser(UserDto userDto) {
        User user = UserMapper.convertToUser(userDto);
        user.setRole(Role.USER);
        user.setPassword(passwordEncoder.encode(userDto.getPassword()));
        try {
            userRepository.save(user);
            return true;
        } catch (DataIntegrityViolationException e) {
            System.out.println(e.getMessage());
            return false;
        }
    }

    @Transactional
    public void updateUsersFullName(UserFullNameDto userDto, String currentUserEmail) {
        Optional<User> optionalUser = userRepository.findUserByEmail(currentUserEmail);
        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            user.setFirstName(userDto.getFirstName());
            user.setLastName(userDto.getLastName());
        }
    }

    public boolean checkIfEmailExists(String email) {
        return userRepository.existsByEmail(email);
    }

    public void deleteTokenByToken(String token) {
        passwordResetTokenRepository.deleteTokenByToken(token);
    }

    @Transactional
    public void reverseRole(Long id) {
        Optional<User> optionalUser = userRepository.findById(id);
        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            if (user.getRole().equals(Role.USER)) {
                user.setRole(Role.ADMIN);
            } else {
                user.setRole(Role.USER);
            }
        }
    }

    @Transactional
    public boolean updateUserPasswordByToken(String userToken, String password) {
        Boolean validated = passwordResetService.checkIfTokenIsValid(userToken);
        if (validated) {
            Optional<User> optionalUser = userRepository.findUserByPasswordResetToken_Token(userToken);
            if (optionalUser.isPresent()) {
                User user = optionalUser.get();
                user.setPassword(passwordEncoder.encode(password));
                passwordResetTokenRepository.deleteTokenByToken(userToken);
                return true;
            }
        }
        deleteTokenByToken(userToken);
        return false;
    }

    @Transactional
    public boolean updatePassword(SecurityContext securityContext, String password, String oldPassword) {
        Optional<User> optionalUser = userRepository.findUserByEmail(securityContext.getAuthentication().getName());
        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            if (passwordEncoder.matches(oldPassword, user.getPassword())) {
                user.setPassword(passwordEncoder.encode(password));
                return true;
            }
        }
        return false;
    }
}
