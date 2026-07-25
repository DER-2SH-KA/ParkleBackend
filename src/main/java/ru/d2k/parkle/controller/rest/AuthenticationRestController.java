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
import ru.d2k.parkle.dto.UserAuthDto;
import ru.d2k.parkle.dto.UserResponseDto;
import ru.d2k.parkle.dto.UserUpdateDto;
import ru.d2k.parkle.service.rest.AuthService;
import ru.d2k.parkle.service.security.cookie.CookieNames;
import ru.d2k.parkle.service.security.jwt.JwtService;
import ru.d2k.parkle.utils.type.Pair;
import java.util.Optional;

@RequiredArgsConstructor
@RestController
@RequestMapping(ApiRoutes.API + ApiRoutes.AUTH_API)
// TODO: Вынести логику авторизации вне контроллера в отдельный класс.
public class AuthenticationRestController {

    @Autowired
    private final JwtService jwtService;

    @Autowired
    private final AuthService service;

    @PostMapping("/login")
    public ResponseEntity<?> authentication(@Valid @RequestBody UserAuthDto authenticateUserDto,
                                            HttpServletResponse response) {
        Pair<String, Optional<UserResponseDto>> jwtAndDto = service.login(authenticateUserDto);

        String jwt = jwtAndDto.getKey();
        Optional<UserResponseDto> dto = jwtAndDto.getValue();

        ResponseCookie jwtCookie = jwtService.createJwtCookie(jwt);
        response.addHeader(HttpHeaders.SET_COOKIE, jwtCookie.toString());

        return dto.isPresent() ? ResponseEntity.ok(dto.get()) : new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
    }

    // TODO: Переделать с /{login} на /me
    @PatchMapping("/update/{login}")
    public ResponseEntity<UserResponseDto> updateByLogin(@PathVariable("login") String login,
                                                         @Valid @RequestBody UserUpdateDto updateUserDto,
                                                         HttpServletResponse response) {
        Pair<String, UserResponseDto> jwtAndDto = service.update(login, updateUserDto);

        String jwt = jwtAndDto.getKey();
        UserResponseDto dto = jwtAndDto.getValue();

        ResponseCookie jwtCookie = jwtService.createJwtCookie(jwt);
        response.addHeader(HttpHeaders.SET_COOKIE, jwtCookie.toString());

        return ResponseEntity.ok(dto);
    }

    // TODO: Переделать с /{login} на /me
    @DeleteMapping("/delete/{login}")
    public ResponseEntity<?> deleteByLogin(@PathVariable("login") String login, HttpServletResponse response) {
        boolean result = service.delete(login);

        this.logout(response);

        return result ? ResponseEntity.ok().build() : (ResponseEntity.internalServerError()
                .body("User was not deleted!"));
    }

    @GetMapping("/isAuthed")
    public ResponseEntity<?> isAuthed(@CookieValue(name = CookieNames.JwtToken, defaultValue = "") String jwt) {
        if (!jwt.isBlank()) {
            Optional<UserResponseDto> dto = service.getUserIfJwtPresent(jwt);

            return dto.isPresent() ? ResponseEntity.ok(dto.get()) : new ResponseEntity<>(new ErrorResponseDto(
                    "Пользователь не авторизован в системе",
                    "User not exists in system by jwt in request"), HttpStatus.UNAUTHORIZED);
        }

        return new ResponseEntity<>(new ErrorResponseDto("Пользователь не авторизован в системе",
                "User's request hasn't jwt cookie for authentication"), HttpStatus.UNAUTHORIZED);
    }

    @GetMapping("/logout")
    public ResponseEntity<?> logout(HttpServletResponse response) {
        ResponseCookie emptyCookie = jwtService.createJwtExpiredCookie();
        response.addHeader(HttpHeaders.SET_COOKIE, emptyCookie.toString());

        return ResponseEntity.ok().build();
    }

    @GetMapping("/ping")
    public ResponseEntity<?> ping() {
        return ResponseEntity.ok().build();
    }
}