package ru.d2k.parkle.controller.rest;

import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import ru.d2k.parkle.controller.ApiPaths;
import ru.d2k.parkle.dto.*;
import ru.d2k.parkle.service.rest.UserService;
import ru.d2k.parkle.service.security.cookie.CookieNames;
import ru.d2k.parkle.service.security.cookie.CustomCookieService;

import java.util.Optional;

@RestController
@RequestMapping(value = ApiPaths.AUTH_API)
@RequiredArgsConstructor
public class AuthRestController {
    private final UserService userService;
    private final CustomCookieService cookieService;

    @PostMapping("/login")
    public ResponseEntity<?> authentication(
            @Valid @RequestBody UserAuthDto uadto,
            HttpServletResponse response
    ) {
        Optional<UserResponseDto> dto = userService.getUserByUserAuthDto(uadto, response);

        return dto.isPresent() ?
                ResponseEntity.ok(dto.get()) :
                new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
    }

    @PostMapping("/registration")
    public ResponseEntity<?> createUser(
            @Valid @RequestBody UserCreateDto cdto,
            HttpServletResponse response
    ) {
        Optional<UserResponseDto> dto = userService.getUserByUserCreateDto(cdto, response);

        return dto.isPresent() ?
                ResponseEntity.status(HttpStatus.CREATED).body(dto.get()) :
                new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
    }

    // TODO: Переделать с /{login} на /me
    @PatchMapping("/update/{login}")
    public ResponseEntity<UserResponseDto> updateUserById(
            @PathVariable String login,
            @Valid @RequestBody UserUpdateDto dto
    ) {
        UserResponseDto responseDto = userService.updateUser(login, dto);

        // TODO: Сделать здесь обновление токена пользователя (перевыпуск с обновлёнными данными)!

        return ResponseEntity.ok(responseDto);
    }

    // TODO: Переделать с /{login} на /me
    @DeleteMapping("/delete/{login}")
    public ResponseEntity<?> deleteUserByLogin(@PathVariable String login, HttpServletResponse response) {
        boolean result = userService.deleteUser(login);

        this.logout(response);

        return result
                ? ResponseEntity.ok().build()
                : ResponseEntity
                    .internalServerError()
                    .body("User was not deleted or not exists!");
    }

    @GetMapping("/isAuthed")
    public ResponseEntity<?> isAuthed(
            @CookieValue(name = CookieNames.JwtToken, defaultValue = "") String jwt
    ) {
        if (!jwt.isBlank()) {
            Optional<UserResponseDto> dto = userService.getUserByJwt(jwt);

            // TODO: Перепроверить содержание messageForDev.
            return dto.isPresent() ?
                    ResponseEntity.ok(dto.get()) :
                    new ResponseEntity<>(
                            new ErrorResponseDto(
                                    "Пользователь не авторизован в системе",
                                    "User not exists in system by jwt in request"
                            ),
                            HttpStatus.UNAUTHORIZED
                    );
        }

        return new ResponseEntity<>(
                new ErrorResponseDto(
                        "Пользователь не авторизован в системе",
                        "User's request hasn't jwt cookie for authentication"
                ),
                HttpStatus.UNAUTHORIZED
        );
    }

    @GetMapping("/logout")
    public ResponseEntity<?> logout(HttpServletResponse response) {
        ResponseCookie emptyCookie = cookieService.createEmptyResponseCookie(
                CookieNames.JwtToken,
                true,
                false,
                "/",
                "Lax"
        );
        response.addHeader(HttpHeaders.SET_COOKIE, emptyCookie.toString());

        return ResponseEntity.ok().build();
    }

    @GetMapping("/ping")
    public ResponseEntity<?> ping() {
        return ResponseEntity.ok().build();
    }
}
