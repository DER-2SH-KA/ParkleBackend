package ru.d2k.parkle.service.rest;

import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import ru.d2k.parkle.dto.UserAuthenticationDto;
import ru.d2k.parkle.dto.UserDtoInterface;
import ru.d2k.parkle.dto.UserResponseDto;
import ru.d2k.parkle.dto.UserUpdateDto;
import ru.d2k.parkle.entity.cache.UserCache;
import ru.d2k.parkle.exception.UserNotFoundException;
import ru.d2k.parkle.model.CustomUserDetails;
import ru.d2k.parkle.service.security.authentication.CustomAuthenticationManagerService;
import ru.d2k.parkle.service.security.cookie.JwtCookieService;
import ru.d2k.parkle.service.security.jwt.JwtService;
import ru.d2k.parkle.utils.mapper.UserMapper;
import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
@Service
public class AuthenticationService {

    @Autowired
    private final UserService userService;

    @Autowired
    private final UserMapper userMapper;

    @Autowired
    private final CustomAuthenticationManagerService authenticationManagerService;

    @Autowired
    private final JwtService jwtService;

    @Autowired final JwtCookieService jwtCookieService;

    public UserResponseDto authenticate(UserAuthenticationDto userAuthenticationDto, HttpServletResponse response) {
        UserCache userCache = this.getUserCacheByUserDtoImplements(userAuthenticationDto);

        if (userCache != null) {
            String jwt = this.generateJwtByUserDtoImplements(userAuthenticationDto);
            jwtCookieService.setCookieWithJwtInResponse(jwt, response);

            return userMapper.toResponseDto(userCache);
        }

        throw new UserNotFoundException("Can't find user by UserAuthenticationDto: " + userAuthenticationDto.toString());
    }

    public UserResponseDto updateByLoginAndUpdateJwt(String login, UserUpdateDto userUpdateDto, HttpServletResponse response) {
        UserCache updatedUserCache = userService.updateByLogin(login, userUpdateDto);

        String jwt = jwtService.generateTokenByUserCache(updatedUserCache);
        jwtCookieService.setCookieWithJwtInResponse(jwt, response);

        return userMapper.toResponseDto(updatedUserCache);
    }

    public boolean deleteByLoginAndUnauthorizeUser(String login, HttpServletResponse response) {
        boolean isUserDeleted = userService.deleteByLogin(login);

        if (isUserDeleted) {
            jwtCookieService.setExpiredJwtCookieInResponse(response);
        }

        return isUserDeleted;
    }

    public void logout(HttpServletResponse response) {
        jwtCookieService.setExpiredJwtCookieInResponse(response);
    }

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