package com.temat29zad1.configuration;

import com.temat29zad1.user.UserService;
import com.temat29zad1.user.UserDto;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;


@Service
public class CustomUserDetailService implements UserDetailsService {

    private final UserService userService;

    public CustomUserDetailService(UserService userService) {
        this.userService = userService;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userService.findUserDtoByEmail(username)
                .map(this::createUserDetail)
                .orElseThrow(() -> new UsernameNotFoundException("Nie znależiono użytkownika z email: %s ".formatted(username)));
    }

    private UserDetails createUserDetail(UserDto userDto) {
        return User.builder()
                .username(userDto.getEmail())
                .password(userDto.getPassword())
                .roles(userDto.getRole().name())
                .build();
    }
}

