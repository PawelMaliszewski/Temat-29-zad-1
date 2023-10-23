package com.temat29zad1.configuration;

import com.temat29zad1.roles.Role;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SpringConfiguration {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.authorizeHttpRequests(request -> request
                .requestMatchers("/", "/styles/**", "/register", "/forgot-password", "/confirm"
                        , "/register", "/registration-form", "forgot-password", "/token-expired",
                        "/reset-password", "/set-new-password/**", "/new-password/**").permitAll()
                .requestMatchers("/user/**").hasAnyRole(Role.USER.name(), Role.ADMIN.name())
                .requestMatchers("/admin/**").hasRole(String.valueOf(Role.ADMIN))
                .anyRequest().authenticated());
        http.formLogin(login -> login.loginPage("/login").defaultSuccessUrl("/", true).permitAll());
        http.logout(logout -> logout.logoutUrl("/logout"));
        return http.build();
    }
}
