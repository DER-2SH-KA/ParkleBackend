package ru.d2k.parkle.controller.rest;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import ru.d2k.parkle.controller.ApiPaths;
import ru.d2k.parkle.dto.*;
import ru.d2k.parkle.service.rest.UserService;
import ru.d2k.parkle.service.security.JwtService;
import ru.d2k.parkle.utils.jwt.JwtUtil;

import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping(value = ApiPaths.AUTH_API)
@RequiredArgsConstructor
public class AuthRestController {
    private final AuthenticationManager authenticationManager;
    private final UserDetailsService userDetailsService;
    private final JwtUtil jwtUtil;
    private final JwtService jwtService;

    private final UserService userService;

    @Value("${jwt.expiration}")
    private Long jwtExpiration;

    /**
     * Authentication user in system by login and password in body.
     * @param uadto {@link UserAuthDto} object for login.
     * @return {@link UserResponseDto} object of authenticated user.
     * **/
    @PostMapping("/login")
    public ResponseEntity<?> authentication(
            @Valid @RequestBody UserAuthDto uadto,
            HttpServletResponse response
    ) {
        UserResponseDto responseDto = userService.authentication(uadto);

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        uadto.getLogin(),
                        uadto.getPassword()
                )
        );

        UserDetails userDetails = (UserDetails) authentication.getPrincipal();

        String jwtToken = jwtUtil.generateToken(userDetails);

        ResponseCookie jwtCookie = ResponseCookie.from("jwt-token", jwtToken)
                .httpOnly(true)
                .secure(false)
                .path("/")
                .maxAge((int) (jwtExpiration / 1000))
                // .sameSite("None")
                .build();

        response.addHeader(HttpHeaders.SET_COOKIE, jwtCookie.toString());

        return ResponseEntity.ok(responseDto);
    }

    /**
     * Registration of new user in system.
     * @param cdto {@link UserCreateDto} object of new user.
     * @return {@link UserResponseDto} object of created user.
     * **/
    @PostMapping("/registration")
    public ResponseEntity<?> createUser(
            @Valid @RequestBody UserCreateDto cdto,
            HttpServletResponse response
    ) {

        UserResponseDto responseDto = userService.createUser(cdto);

        UserDetails userDetails = userDetailsService.loadUserByUsername(cdto.getLogin());
        String jwtToken = jwtUtil.generateToken(userDetails);

        ResponseCookie jwtCookie = ResponseCookie.from("jwt-token", jwtToken)
                .httpOnly(true)
                .secure(false)
                .path("/")
                .maxAge((int) (jwtExpiration / 1000))
                // .sameSite("None")
                .build();

        response.addHeader(HttpHeaders.SET_COOKIE, jwtCookie.toString());

        return ResponseEntity.ok( responseDto );
    }

    /**
     * Update user information.
     * @param login user's login.
     * @param dto {@link UserUpdateDto} object for update user.
     * @return {@link UserResponseDto} object of updated user.
     * **/
    @PutMapping("/update/{login}")
    public ResponseEntity<UserResponseDto> updateUserById(
            @PathVariable String login,
            @Valid @RequestBody UserUpdateDto dto
    ) {
        UserResponseDto responseDto = userService.updateUser(login, dto);

        // TODO: Сделать здесь обновление токена пользователя (перевыпуск с обновлёнными данными)!

        return ResponseEntity.ok(responseDto);
    }

    /**
     * Delete user by ID.
     * @param login user's login.
     * @return OK status.
     * **/
    @DeleteMapping("/delete/{login}")
    public ResponseEntity<?> deleteUserById(@PathVariable String login) {
        boolean result = userService.deleteUser(login);

        return result
                ? ResponseEntity.ok().build()
                : ResponseEntity
                    .internalServerError()
                    .body("User was not deleted or not exists!");
    }

    public ResponseEntity<?> isAuthed(HttpServletRequest request) {
        Optional<UUID> uuid = jwtService.getUserUuidByJwtToken(request);

        return uuid.isPresent() ?
                ResponseEntity.ok(uuid.get()) :
                new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
    }
}
