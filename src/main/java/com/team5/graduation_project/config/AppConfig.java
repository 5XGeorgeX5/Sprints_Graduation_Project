package com.team5.graduation_project.config;

import com.team5.graduation_project.Exceptions.ResourceNotFound;
import com.team5.graduation_project.Repository.AccountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@RequiredArgsConstructor
public class AppConfig {

    private final AccountRepository accountRepository;

    @Bean
    public UserDetailsService userDetailsService(){
        return username -> accountRepository.findByUsername(username).orElseThrow(() -> new ResourceNotFound("User not found"));
    }

    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration context) throws Exception{
        return context.getAuthenticationManager();
    }
}
