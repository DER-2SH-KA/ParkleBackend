package ru.d2k.parkle.controller.rest;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import ru.d2k.parkle.controller.ApiPaths;
import ru.d2k.parkle.dto.*;
import ru.d2k.parkle.service.rest.UserService;

import java.util.Optional;

@RestController
@RequestMapping(value = ApiPaths.AUTH_API)
@RequiredArgsConstructor
public class AuthRestController {
    private final UserService userService;

    /**
     * Check user is authorized in system by JWT.
     * @param request
     * @param response
     * @return {@link ResponseEntity} with {@link UserResponseDto} or error 401.
     */
    @GetMapping("/isAuthed")
    public ResponseEntity<?> isAuthed(HttpServletRequest request, HttpServletResponse response) {
        Optional<UserResponseDto> dto = userService.getUserByJwt(request, response);

        return dto.isPresent() ?
                ResponseEntity.ok(dto.get()) :
                new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
    }

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
        Optional<UserResponseDto> dto = userService.getUserByUserAuthDto(uadto, response);

        return dto.isPresent() ?
                ResponseEntity.ok(dto.get()) :
                new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
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
        Optional<UserResponseDto> dto = userService.getUserByUserCreateDto(cdto, response);

        return dto.isPresent() ?
                ResponseEntity.ok(dto.get()) :
                new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
    }

    /**
     * Update user information.
     * @param login user's login.
     * @param dto {@link UserUpdateDto} object for update user.
     * @return {@link UserResponseDto} object of updated user.
     * **/
    @PatchMapping("/update/{login}")
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
}
