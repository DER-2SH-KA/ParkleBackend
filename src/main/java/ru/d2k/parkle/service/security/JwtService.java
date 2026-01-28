package ru.d2k.parkle.service.security;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;
import ru.d2k.parkle.dao.UserDao;
import ru.d2k.parkle.dto.UserResponseDto;
import ru.d2k.parkle.entity.User;
import ru.d2k.parkle.entity.cache.UserCache;
import ru.d2k.parkle.exception.JwtNotExistInRequestException;
import ru.d2k.parkle.exception.UserNotFoundException;
import ru.d2k.parkle.repository.UserRepository;
import ru.d2k.parkle.utils.jwt.JwtUtil;
import ru.d2k.parkle.utils.mapper.UserMapper;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class JwtService {
    private final UserDao userDao;
    private final UserMapper userMapper;
    private final JwtUtil jwtUtil;

    public boolean hasJwtInCookie(HttpServletRequest request) {
        final Optional<String> jwt = JwtUtil.extractJwtFromCookie(request);

        return jwt.isPresent();
    }

    public Optional<UserResponseDto> getUserUuidByJwtToken(HttpServletRequest request) {
        if (hasJwtInCookie(request)) {
            System.out.println("HttpServletRequest has JWT!");

            final String jwt = JwtUtil.extractJwtFromCookie(request)
                    .orElseThrow(() ->
                                    new NullPointerException("Trying extract username from null JWT is false")
                    );

            if (jwt.isBlank()) throw new JwtNotExistInRequestException();

            String username = jwtUtil.extractUsername(jwt);

            UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext()
                    .getAuthentication()
                    .getPrincipal();

            System.out.println("Is JWT Valid?: " + jwtUtil.isTokenValid(jwt, userDetails));

            UserCache userByLogin = userDao.getByLogin(username)
                    .orElseThrow(() ->
                            new UserNotFoundException("User not found with login: " + username)
                    );

            UserResponseDto dto = userMapper.toResponseDto(userByLogin);

            return jwtUtil.isTokenValid(jwt, userDetails) ?
                    Optional.ofNullable( dto) :
                    Optional.empty();
        }

        System.out.println("HttpServletRequest hasn't JWT!");

        return Optional.empty();
    }
}
