package ru.d2k.parkle.controller.rest;

import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.d2k.parkle.controller.ApiRoutes;
import ru.d2k.parkle.dto.ErrorResponseDto;
import ru.d2k.parkle.dto.UserAuthenticationDto;
import ru.d2k.parkle.dto.UserResponseDto;
import ru.d2k.parkle.dto.UserUpdateDto;
import ru.d2k.parkle.exception.JwtNotExistInRequestException;
import ru.d2k.parkle.service.rest.AuthenticationService;
import ru.d2k.parkle.service.security.cookie.CookieNames;
import ru.d2k.parkle.service.security.cookie.CustomCookieService;
import ru.d2k.parkle.utils.type.Pair;
import java.util.Optional;

@RequiredArgsConstructor
@RestController
@RequestMapping(ApiRoutes.API + ApiRoutes.AUTH_API)
// TODO: Вынести логику авторизации вне контроллера в отдельный класс.
public class AuthenticationRestController {

    @Autowired
    private final CustomCookieService cookieService;

    @Autowired
    private final AuthenticationService service;

    @PostMapping("/login")
    public ResponseEntity<?> authenticate(@Valid @RequestBody UserAuthenticationDto authenticateUserDto,
                                          HttpServletResponse response) {
        Pair<String, Optional<UserResponseDto>> jwtAndUserResponseDto = service.login(authenticateUserDto);

        String jwt = jwtAndUserResponseDto.getKey();
        Optional<UserResponseDto> userResponseDto = jwtAndUserResponseDto.getValue();

        ResponseCookie jwtCookie = cookieService.createCookieWithJwt(jwt);
        response.addHeader(HttpHeaders.SET_COOKIE, jwtCookie.toString());

        return userResponseDto.isPresent() ? ResponseEntity.ok(userResponseDto.get()) :
                new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
    }

    // TODO: Переделать с /{login} на /me
    @PatchMapping("/update/{login}")
    public ResponseEntity<UserResponseDto> updateByLogin(@PathVariable("login") String login,
                                                         @Valid @RequestBody UserUpdateDto updateUserDto,
                                                         HttpServletResponse response) {
        Pair<String, UserResponseDto> jwtAndUserResponseDto = service.updateByLogin(login, updateUserDto);

        String jwt = jwtAndUserResponseDto.getKey();
        UserResponseDto userResponseDto = jwtAndUserResponseDto.getValue();

        ResponseCookie jwtCookie = cookieService.createCookieWithJwt(jwt);
        response.addHeader(HttpHeaders.SET_COOKIE, jwtCookie.toString());

        return ResponseEntity.ok(userResponseDto);
    }

    // TODO: Переделать с /{login} на /me
    @DeleteMapping("/delete/{login}")
    public ResponseEntity<?> deleteByLogin(@PathVariable("login") String login, HttpServletResponse response) {
        boolean result = service.deleteByLogin(login);

        this.logout(response);

        return result ? ResponseEntity.ok().build() : (ResponseEntity.internalServerError()
                .body("User was not deleted!"));
    }

    @GetMapping("/isAuthed")
    public ResponseEntity<?> isUserAuthenticated(
            @CookieValue(name = CookieNames.JWT_TOKEN, defaultValue = "") String jwt) {
        if (!jwt.isBlank()) {
            Optional<UserResponseDto> userResponseDto = service.getUserByJwt(jwt);

            return userResponseDto.isPresent() ? ResponseEntity.ok(userResponseDto.get()) :
                    new ResponseEntity<>(new ErrorResponseDto("Пользователь не авторизован в системе",
                    "User not exists in system by jwt in request"), HttpStatus.UNAUTHORIZED);
        }

        throw new JwtNotExistInRequestException("User's request hasn't jwt cookie for authentication");
    }

    @GetMapping("/logout")
    public ResponseEntity<?> logout(HttpServletResponse response) {
        response.addHeader(HttpHeaders.SET_COOKIE, cookieService.createCookieWithExpiredJwt().toString());

        return ResponseEntity.ok().build();
    }

    @GetMapping("/ping")
    public ResponseEntity<?> ping() {
        return ResponseEntity.ok().build();
    }
}