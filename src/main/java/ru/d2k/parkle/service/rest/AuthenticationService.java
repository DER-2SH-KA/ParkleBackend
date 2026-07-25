package ru.d2k.parkle.service.rest;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import ru.d2k.parkle.dto.UserAuthenticationDto;
import ru.d2k.parkle.dto.UserResponseDto;
import ru.d2k.parkle.dto.UserUpdateDto;
import ru.d2k.parkle.model.CustomUserDetails;
import ru.d2k.parkle.service.security.authentication.CustomAuthenticationManagerService;
import ru.d2k.parkle.service.security.jwt.JwtService;
import ru.d2k.parkle.utils.type.Pair;
import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
@Service
public class AuthenticationService {

    @Autowired
    private final UserService userService;

    @Autowired
    private final CustomAuthenticationManagerService authenticationManagerService;

    @Autowired
    private final JwtService jwtService;

    public Pair<String, Optional<UserResponseDto>> login(UserAuthenticationDto authenticateUserDto) {
        Authentication signedAuthentication = authenticationManagerService
                .createHttpUnauthorizedAuthentication(authenticateUserDto);
        CustomUserDetails customUserDetails = (CustomUserDetails) signedAuthentication.getPrincipal();

        String jwt = jwtService.generateTokenByUserCache(customUserDetails.getCache());

        Optional<UserResponseDto> userResponseDto = userService.getUserByUserCache(customUserDetails.getCache());

        return new Pair<>(jwt, userResponseDto);
    }

    public Pair<String, UserResponseDto> updateByLogin(String login, UserUpdateDto updateUserDto) {
        UserResponseDto updatedUserDto = userService.updateByLogin(login, updateUserDto);

        Authentication signedAuthentication = authenticationManagerService
                .createHttpUnauthorizedAuthentication(updateUserDto);
        CustomUserDetails customUserDetails = (CustomUserDetails) signedAuthentication.getPrincipal();

        String jwtToken = jwtService.generateTokenByUserCache(customUserDetails.getCache());

        return new Pair<>(jwtToken, updatedUserDto);
    }

    public boolean deleteByLogin(String login) {
        return userService.deleteByLogin(login);
    }

    public Optional<UserResponseDto> getUserByJwt(String jwt) {
        return userService.getUserByJwt(jwt);
    }
}