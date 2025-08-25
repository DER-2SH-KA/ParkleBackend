package ru.d2k.parkle.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.HttpStatusEntryPoint;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
    @Bean
    public PasswordEncoder passwordEncoder(@Value("${spring.security.bcrypt.strength}") int strength) {
        return new BCryptPasswordEncoder( strength );
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(request ->
                        request
                                .requestMatchers(
                                        "/",
                                        "/api/auth/login",
                                        "/api/auth/registration",
                                        "/index.html",
                                        "/*.js",
                                        "/*.css",
                                        "/*.ico",
                                        "/assets/**",
                                        "/actuator", // TODO: ТОЛЬКО ДЛЯ ТЕСТОВ! УДАЛИТЬ ПОСЛЕ!
                                        "/actuator/**"
                                ).permitAll()
                                .requestMatchers(
                                        "/api/roles",
                                        "/api/roles/**",
                                        "/api/users",
                                        "/api/websites"
                                ).hasRole("DEV")
                                .anyRequest().authenticated()

                )
                .exceptionHandling(handler ->
                    handler
                            .authenticationEntryPoint(
                                    new HttpStatusEntryPoint(HttpStatus.UNAUTHORIZED)
                            )
                );

        return http.build();
    }
}
