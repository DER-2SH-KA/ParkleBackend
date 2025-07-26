package ru.d2k.parkle.controller.rest;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.d2k.parkle.aspect.annotation.LoggableGetMapping;
import ru.d2k.parkle.controller.ApiPaths;
import ru.d2k.parkle.dto.UserCreateDto;
import ru.d2k.parkle.dto.UserResponseDto;
import ru.d2k.parkle.dto.UserUpdateDto;
import ru.d2k.parkle.service.rest.UserService;

import java.util.UUID;
import java.util.Set;

@RestController
@RequestMapping(ApiPaths.USER_API)
@RequiredArgsConstructor
public class UserRestController {
    private final UserService userService;

    /**
     * Get all users from database as DTO.
     * @return List of {@link UserResponseDto}.
     * **/
    @GetMapping
    @LoggableGetMapping
    public ResponseEntity<Set<UserResponseDto>> findUsers() {
        Set<UserResponseDto> userResponseDtos = userService.findUsers();

        return ResponseEntity.ok( userResponseDtos );
    }

    /**
     * Get user from database by user's ID as DTO.
     * @param id user's ID.
     * @return {@link UserResponseDto} object.
     * **/
    @GetMapping("/{id}")
    @LoggableGetMapping
    public ResponseEntity<UserResponseDto> findUserById(@PathVariable UUID id) {
        UserResponseDto responseDto = userService.findUserById(id);

        return ResponseEntity.ok( responseDto );
    }
}
