package ru.d2k.parkle.controller.rest;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.d2k.parkle.controller.ApiPaths;
import ru.d2k.parkle.dto.UserAuthDto;
import ru.d2k.parkle.dto.UserCreateDto;
import ru.d2k.parkle.dto.UserResponseDto;
import ru.d2k.parkle.dto.UserUpdateDto;
import ru.d2k.parkle.service.rest.UserService;

import java.util.UUID;

@Slf4j
@RestController
@RequestMapping(value = ApiPaths.AUTH_API)
@RequiredArgsConstructor
public class AuthRestController {
    private final UserService userService;

    /**
     * Authentication user in system by login and password in body.
     * @param uadto {@link UserAuthDto} object for login.
     * @return {@link UserResponseDto} object of authenticated user.
     * **/
    @PostMapping("/login")
    public ResponseEntity<UserResponseDto> authentication(@RequestBody UserAuthDto uadto) {
        UserResponseDto responseDto = userService.authentication(uadto);

        return ResponseEntity.ok(responseDto);
    }

    /**
     * Registration of new user in system.
     * @param cdto {@link UserCreateDto} object of new user.
     * @return {@link UserResponseDto} object of created user.
     * **/
    @PostMapping("/registration")
    public ResponseEntity<UserResponseDto> createUser(@RequestBody UserCreateDto cdto) {
        log.info("Given POST user request with DTO: {}", cdto);

        UserResponseDto responseDto = userService.createUser(cdto);

        log.info("POST request was served");
        return ResponseEntity.ok(responseDto);
    }

    /**
     * Update user information.
     * @param id user's ID.
     * @param dto {@link UserUpdateDto} object for update user.
     * @return {@link UserResponseDto} object of updated user.
     * **/
    @PutMapping("/update/{id}")
    public ResponseEntity<UserResponseDto> updateUserById(@PathVariable UUID id, @RequestBody UserUpdateDto dto) {
        log.info("Given PUT user request with ID: {}", id);

        UserResponseDto responseDto = userService.updateUser(id, dto);

        log.info("PUT request was served");
        return ResponseEntity.ok(responseDto);
    }

    /**
     * Delete user by ID.
     * @param id user's ID.
     * @return OK status.
     * **/
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteUserById(@PathVariable UUID id) {
        log.info("Given DELETE user request by ID: {}", id);

        userService.deleteUser(id);

        log.info("DELETE request was served");
        return ResponseEntity.ok().build();
    }
}
