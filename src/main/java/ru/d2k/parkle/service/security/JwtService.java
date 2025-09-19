package ru.d2k.parkle.service.security;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
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

    public boolean hasJwtInCookie(HttpServletRequest request) {
        final Optional<String> jwt = JwtUtil.extractJwtFromCookie(request);

        return jwt.isPresent();
    }

    private UUID getUserUuidByUsername(String username) {
        UserResponseDto dto = userService.findUserByLogin(username);

        return dto.getId();
    }


    public Optional<UUID> getUserUuidByJwtToken(HttpServletRequest request) {
        if (hasJwtInCookie(request)) {
            final Optional<String> jwt = JwtUtil.extractJwtFromCookie(request);

            String username = jwtUtil.extractUsername(
                    jwt.orElseThrow(() -> new NullPointerException("Trying extract username from null JWT is false"))
            );

            return Optional.ofNullable( getUserUuidByUsername(username) );
        }

        return Optional.empty();
    }
}
