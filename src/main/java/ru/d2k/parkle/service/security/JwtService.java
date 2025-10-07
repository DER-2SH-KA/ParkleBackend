package ru.d2k.parkle.service.security;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;
import ru.d2k.parkle.dto.UserResponseDto;
import ru.d2k.parkle.service.rest.UserService;
import ru.d2k.parkle.utils.jwt.JwtUtil;

import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class JwtService {
    private final UserService userService;
    private final JwtUtil jwtUtil;
    private final UserDetailsService userDetailsService;

    public boolean hasJwtInCookie(HttpServletRequest request) {
        final Optional<String> jwt = JwtUtil.extractJwtFromCookie(request);

        return jwt.isPresent();
    }

    private UUID getUserUuidByUsername(String username) {
        UserResponseDto dto = userService.findUserByLogin(username);

        return dto.getId();
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

            return jwtUtil.isTokenValid(jwt.get(), userDetails) ?
                    Optional.ofNullable( userService.findUserByLogin(username) ) :
                    Optional.empty();
        }

        System.out.println("HttpServletRequest hasn't JWT!");

        return Optional.empty();
    }

    public boolean resetJwt(HttpServletRequest request, HttpServletResponse response) {
        final boolean hasJwt = this.hasJwtInCookie(request);

        if (hasJwt) return JwtUtil.resetJwt(response);

        return true;
    }
}
