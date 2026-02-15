package ru.d2k.parkle.service.rest;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import ru.d2k.parkle.dto.UserAuthDto;
import ru.d2k.parkle.dto.UserCreateDto;
import ru.d2k.parkle.dto.UserResponseDto;
import ru.d2k.parkle.dto.UserUpdateDto;
import ru.d2k.parkle.model.CustomUserDetails;
import ru.d2k.parkle.service.security.authentication.CustomAuthenticationManagerService;
import ru.d2k.parkle.utils.jwt.JwtUtil;

import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthService {
    private final UserService userService;
    private final CustomAuthenticationManagerService authenticationManagerService;
    private final JwtUtil jwtUtil;

    public Map.Entry<String, Optional<UserResponseDto>> login(UserAuthDto adto) {
        Authentication signedAuthentication =
                authenticationManagerService.createHttpUnauthorizedAuthentication(adto);

        CustomUserDetails userDetails = (CustomUserDetails) signedAuthentication.getPrincipal();

        String jwtToken = jwtUtil.generateToken(userDetails);

        Optional<UserResponseDto> dto = userService.getUserByUserCache(userDetails.getCache());

        return Map.entry(jwtToken, dto);
    }

    public Map.Entry<String, UserResponseDto> registration(UserCreateDto cdto) {
        UserResponseDto dto = userService.createUser(cdto);

        Authentication signedAuthentication =
                authenticationManagerService.createHttpUnauthorizedAuthentication(cdto);
        CustomUserDetails userDetails = (CustomUserDetails) signedAuthentication.getPrincipal();

        String jwtToken = jwtUtil.generateToken(userDetails);

        return Map.entry(jwtToken, dto);
    }

    public Map.Entry<String, UserResponseDto> update(String login, UserUpdateDto udto) {
        UserResponseDto dto = userService.updateUser(login, udto);

        Authentication signedAuthentication =
                authenticationManagerService.createHttpUnauthorizedAuthentication(udto);
        CustomUserDetails userDetails = (CustomUserDetails) signedAuthentication.getPrincipal();

        String jwtToken = jwtUtil.generateToken(userDetails);

        return Map.entry(jwtToken, dto);
    }

    public boolean delete(String login) {
        return userService.deleteUser(login);
    }

    public Optional<UserResponseDto> getUserIfJwtPresent(String jwt) {
        return userService.getUserByJwt(jwt);
    }
}
