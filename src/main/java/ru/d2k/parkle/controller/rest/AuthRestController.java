package ru.d2k.parkle.controller.rest;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
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
import ru.d2k.parkle.utils.jwt.JwtUtil;

import java.util.UUID;

@RestController
@RequestMapping(value = ApiPaths.AUTH_API)
@RequiredArgsConstructor
public class AuthRestController {
    private final AuthenticationManager authenticationManager;
    private final UserDetailsService userDetailsService;
    private final JwtUtil jwtUtil;

    private final UserService userService;

    /**
     * Authentication user in system by login and password in body.
     * @param uadto {@link UserAuthDto} object for login.
     * @return {@link UserResponseDto} object of authenticated user.
     * **/
    @PostMapping("/login")
    public ResponseEntity<AuthResponseDto> authentication(@Valid @RequestBody UserAuthDto uadto) {
        UserResponseDto responseDto = userService.authentication(uadto);

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        uadto.getLogin(),
                        uadto.getPassword()
                )
        );

        UserDetails userDetails = (UserDetails) authentication.getPrincipal();

        String jwtToken = jwtUtil.generateToken(userDetails);

        return ResponseEntity.ok(new AuthResponseDto(jwtToken));
    }

    /**
     * Registration of new user in system.
     * @param cdto {@link UserCreateDto} object of new user.
     * @return {@link UserResponseDto} object of created user.
     * **/
    @PostMapping("/registration")
    public ResponseEntity<AuthResponseDto> createUser(@Valid @RequestBody UserCreateDto cdto) {

        UserResponseDto responseDto = userService.createUser(cdto);

        UserDetails userDetails = userDetailsService.loadUserByUsername(cdto.getLogin());
        String jwtToken = jwtUtil.generateToken(userDetails);

        return ResponseEntity.ok( new AuthResponseDto(jwtToken) );
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
}
