package ru.d2k.parkle.service.rest.impl;

import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.d2k.parkle.dto.UserAuthenticationDto;
import ru.d2k.parkle.dto.UserDtoInterface;
import ru.d2k.parkle.dto.UserResponseDto;
import ru.d2k.parkle.dto.UserUpdateDto;
import ru.d2k.parkle.entity.cache.UserCache;
import ru.d2k.parkle.exception.entity.UserEntityNotFoundException;
import ru.d2k.parkle.model.CustomUserDetails;
import ru.d2k.parkle.service.rest.AuthenticationService;
import ru.d2k.parkle.service.security.authentication.CustomAuthenticationManagerService;
import ru.d2k.parkle.service.security.cookie.JwtCookieService;
import ru.d2k.parkle.service.security.jwt.JwtService;
import ru.d2k.parkle.utils.mapper.UserMapper;
import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
@Service
public class AuthenticationServiceImpl implements AuthenticationService {

    private final UserServiceImpl userService;
    private final UserMapper userMapper;
    private final CustomAuthenticationManagerService authenticationManagerService;
    private final JwtService jwtService;
    private final JwtCookieService jwtCookieService;

    @Override
    @Transactional(readOnly = true)
    public UserResponseDto authenticate(UserAuthenticationDto userAuthenticationDto, HttpServletResponse response) {
        UserCache userCache = this.getUserCacheByUserDtoImplements(userAuthenticationDto);

        if (userCache != null) {
            String jwt = this.generateJwtByUserDtoImplements(userAuthenticationDto);
            jwtCookieService.setCookieWithJwtInResponse(jwt, response);

            return userMapper.toResponseDto(userCache);
        }

        throw new UserEntityNotFoundException("Can't find user by UserAuthenticationDto: " + userAuthenticationDto.toString());
    }

    @Override
    @Transactional
    public UserResponseDto updateByLoginAndUpdateJwt(String login, UserUpdateDto userUpdateDto, HttpServletResponse response) {
        UserCache updatedUserCache = userService.updateByLogin(login, userUpdateDto);

        String jwt = jwtService.generateTokenByUserCache(updatedUserCache);
        jwtCookieService.setCookieWithJwtInResponse(jwt, response);

        return userMapper.toResponseDto(updatedUserCache);
    }

    @Override
    @Transactional
    public boolean deleteByLoginAndUnauthorizeUser(String login, HttpServletResponse response) {
        userService.deleteByLogin(login);

        boolean isUserDeleted = !userService.existsByLogin(login);

        if (isUserDeleted) {
            jwtCookieService.setExpiredJwtCookieInResponse(response);
        }

        return isUserDeleted;
    }

    @Override
    public void logout(HttpServletResponse response) {
        jwtCookieService.setExpiredJwtCookieInResponse(response);
    }

    @Override
    public Optional<UserResponseDto> getUserResponseDtoFromSecurityContext() {
        CustomUserDetails customUserDetails = this.getCustomUserDetailsFromSecurityContext();

        if (customUserDetails != null) {
            return Optional.of(userMapper.toResponseDto(customUserDetails.getCache()));
        }

        return Optional.empty();
    }

    private String generateJwtByUserDtoImplements(UserDtoInterface userDto) {
        CustomUserDetails customUserDetails = this.getCustomUserDetailsByUserDtoImplements(userDto);

        return jwtService.generateTokenByUserCache(customUserDetails.getCache());
    }

    private UserCache getUserCacheByUserDtoImplements(UserDtoInterface userDto) {
        CustomUserDetails customUserDetails = this.getCustomUserDetailsByUserDtoImplements(userDto);

        return customUserDetails.getCache();
    }

    private CustomUserDetails getCustomUserDetailsFromSecurityContext() {
        return (CustomUserDetails) SecurityContextHolder.getContext()
                .getAuthentication()
                .getPrincipal();
    }

    private CustomUserDetails getCustomUserDetailsByUserDtoImplements(UserDtoInterface userDto) {
        Authentication signedAuthentication = authenticationManagerService
                .createHttpUnauthorizedAuthentication(userDto);

        return (CustomUserDetails) signedAuthentication.getPrincipal();
    }
}