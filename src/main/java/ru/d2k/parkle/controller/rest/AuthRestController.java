package ru.d2k.parkle.controller.rest;

import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import ru.d2k.parkle.controller.ApiPaths;
import ru.d2k.parkle.dto.*;
import ru.d2k.parkle.model.CustomUserDetails;
import ru.d2k.parkle.service.rest.UserService;
import ru.d2k.parkle.service.security.authentication.CustomAuthenticationManagerService;
import ru.d2k.parkle.service.security.cookie.CookieNames;
import ru.d2k.parkle.utils.jwt.JwtUtil;

import java.util.Optional;

@RestController
@RequestMapping(value = ApiPaths.AUTH_API)
@RequiredArgsConstructor
// TODO: Вынести логику авторизации вне контроллера в отдельный класс.
public class AuthRestController {
    private final UserService userService;
    private final CustomAuthenticationManagerService authenticationManagerService;
    private final JwtUtil jwtUtil;

    @PostMapping("/login")
    public ResponseEntity<?> authentication(
            @Valid @RequestBody UserAuthDto uadto,
            HttpServletResponse response
    ) {
        Authentication signedAuthentication =
                authenticationManagerService.createHttpUnauthorizedAuthentication(uadto);

        CustomUserDetails userDetails = (CustomUserDetails) signedAuthentication.getPrincipal();

        String jwtToken = jwtUtil.generateToken(userDetails);

        ResponseCookie jwtCookie = jwtUtil.createJwtCookie(jwtToken);
        response.addHeader(HttpHeaders.SET_COOKIE, jwtCookie.toString());

        Optional<UserResponseDto> dto = userService.getUserByUserCache(userDetails.getCache());

        return dto.isPresent() ?
                ResponseEntity.ok(dto.get()) :
                new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
    }

    @PostMapping("/registration")
    public ResponseEntity<?> createUser(
            @Valid @RequestBody UserCreateDto cdto,
            HttpServletResponse response
    ) {
        UserResponseDto dto = userService.createUser(cdto);

        Authentication signedAuthentication =
                authenticationManagerService.createHttpUnauthorizedAuthentication(cdto);
        CustomUserDetails userDetails = (CustomUserDetails) signedAuthentication.getPrincipal();

        String jwtToken = jwtUtil.generateToken(userDetails);

        ResponseCookie jwtCookie = jwtUtil.createJwtCookie(jwtToken);
        response.addHeader(HttpHeaders.SET_COOKIE, jwtCookie.toString());

        return new ResponseEntity(dto, HttpStatus.CREATED);
    }

    // TODO: Переделать с /{login} на /me
    @PatchMapping("/update/{login}")
    public ResponseEntity<UserResponseDto> updateUserById(
            @PathVariable String login,
            @Valid @RequestBody UserUpdateDto dto,
            HttpServletResponse response
    ) {
        UserResponseDto responseDto = userService.updateUser(login, dto);

        Authentication signedAuthentication =
                authenticationManagerService.createHttpUnauthorizedAuthentication(dto);
        CustomUserDetails userDetails = (CustomUserDetails) signedAuthentication.getPrincipal();

        String jwtToken = jwtUtil.generateToken(userDetails);

        ResponseCookie jwtCookie = jwtUtil.createJwtCookie(jwtToken);
        response.addHeader(HttpHeaders.SET_COOKIE, jwtCookie.toString());

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
        ResponseCookie emptyCookie = jwtUtil.createJwtExpiredCookie();
        response.addHeader(HttpHeaders.SET_COOKIE, emptyCookie.toString());

        return ResponseEntity.ok().build();
    }

    @GetMapping("/ping")
    public ResponseEntity<?> ping() {
        return ResponseEntity.ok().build();
    }
}
