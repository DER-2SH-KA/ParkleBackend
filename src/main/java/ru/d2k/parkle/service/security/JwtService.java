package ru.d2k.parkle.service.security;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;
import ru.d2k.parkle.dto.UserResponseDto;
import ru.d2k.parkle.entity.User;
import ru.d2k.parkle.exception.UserNotFoundException;
import ru.d2k.parkle.repository.UserRepository;
import ru.d2k.parkle.utils.jwt.JwtUtil;
import ru.d2k.parkle.utils.mapper.UserMapper;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class JwtService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final JwtUtil jwtUtil;
    private final UserDetailsService userDetailsService;

    public boolean hasJwtInCookie(HttpServletRequest request) {
        final Optional<String> jwt = JwtUtil.extractJwtFromCookie(request);

        return jwt.isPresent();
    }

    public Optional<UserResponseDto> getUserUuidByJwtToken(HttpServletRequest request) {
        if (hasJwtInCookie(request)) {
            System.out.println("HttpServletRequest has JWT!");

            final Optional<String> jwt = JwtUtil.extractJwtFromCookie(request);

            String username = jwtUtil.extractUsername(
                    jwt.orElseThrow(() -> new NullPointerException("Trying extract username from null JWT is false"))
            );

            UserDetails userDetails = this.userDetailsService.loadUserByUsername(username);

            System.out.println("Is JWT Valid?: " + jwtUtil.isTokenValid(jwt.get(), userDetails));

            User userByLogin = userRepository.findByLogin(username)
                    .orElseThrow(() ->
                            new UserNotFoundException("User not found with login: " + username)
                    );

            UserResponseDto dto = userMapper.toResponseDto(userByLogin);

            return jwtUtil.isTokenValid(jwt.get(), userDetails) ?
                    Optional.ofNullable( dto) :
                    Optional.empty();
        }

        System.out.println("HttpServletRequest hasn't JWT!");

        return Optional.empty();
    }
}
