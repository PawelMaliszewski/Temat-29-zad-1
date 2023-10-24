package com.temat29zad1.user;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final PasswordResetTokenRepository passwordResetTokenRepository;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder, PasswordResetTokenRepository passwordResetTokenRepository) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.passwordResetTokenRepository = passwordResetTokenRepository;
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

    public Boolean updateUser(UserDto userDto, String token, String password) {
        User user = null;
        if (token != null) {
            Optional<User> userByToken = userRepository.findUserByPasswordResetToken_Token(token);
            if (userByToken.isPresent()) {
                user = userByToken.get();
                user.setPassword(passwordEncoder(password));
            }
        } else if (password != null || !userDto.getPassword().isEmpty()) {
            User userOldData = userRepository.findById(userDto.getId()).get();
            String userDtoPassword = userDto.getPassword();
            changeEmailIfNeeded(userDto, userOldData);
            user = UserMapper.convertToUser(userDto);
            user.setPassword(passwordEncoder(userDtoPassword));
        } else {
            User userOldData = userRepository.findById(userDto.getId()).get();
            changeEmailIfNeeded(userDto, userOldData);
            user = UserMapper.convertToUser(userDto);
            user.setPassword(userOldData.getPassword());
        }
        try {
            userRepository.updateUserById(user.getFirstName(), user.getLastName(), user.getEmail(),
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


    private void changeEmailIfNeeded(UserDto userDto, User userOldData) {
        if (userDto.getEmail() == null) {
            userDto.setEmail(userOldData.getEmail());
        }
    }

    public boolean checkIfEmailExists(String email) {
        return userRepository.existsByEmail(email);
    }

    public String returnUserEmailByToken(String token) {
        return userRepository.findUserByPasswordResetToken_Token(token).get().getEmail();
    }

    public UserDto findUserDtoById(Long id) {
        return userRepository.findById(id).map(UserMapper::convertToUserDto).get();
    }

    public void deleteTokenByToken(String token) {
        passwordResetTokenRepository.deleteTokenByToken(token);
    }

    private String passwordEncoder(String password) {
        return passwordEncoder.encode(password);
    }

}
