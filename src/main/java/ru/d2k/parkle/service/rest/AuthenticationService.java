package ru.d2k.parkle.service.rest;

import jakarta.servlet.http.HttpServletResponse;
import ru.d2k.parkle.dto.UserAuthenticationDto;
import ru.d2k.parkle.dto.UserResponseDto;
import ru.d2k.parkle.dto.UserUpdateDto;
import java.util.Optional;

public interface AuthenticationService {

    UserResponseDto authenticate(UserAuthenticationDto userAuthenticationDto, HttpServletResponse response);
    UserResponseDto updateByLoginAndUpdateJwt(String login, UserUpdateDto userUpdateDto, HttpServletResponse response);
    boolean deleteByLoginAndUnauthorizeUser(String login, HttpServletResponse response);
    void logout(HttpServletResponse response);
    Optional<UserResponseDto> getUserResponseDtoFromSecurityContext();
}