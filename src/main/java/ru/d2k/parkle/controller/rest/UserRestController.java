package ru.d2k.parkle.controller.rest;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.d2k.parkle.controller.ApiPaths;
import ru.d2k.parkle.dto.UserResponseDto;
import ru.d2k.parkle.service.rest.UserService;

import java.util.Set;

@RestController
@RequestMapping(ApiPaths.USER_API)
@RequiredArgsConstructor
public class UserRestController {
    private final UserService userService;

    // TODO: ONLY FOR TESTS! DELETE AFTER ALL!
    /**
     * Get all users from database as DTO.
     * @return List of {@link UserResponseDto}.
     * **/
    @GetMapping
    public ResponseEntity<Set<UserResponseDto>> findUsers() {
        Set<UserResponseDto> userResponseDtos = userService.findUsers();

        return ResponseEntity.ok( userResponseDtos );
    }

    /**
     * Get user from database by user's ID as DTO.
     * @param login user's login.
     * @return {@link UserResponseDto} object.
     * **/
    @GetMapping("/{login}")
    public ResponseEntity<UserResponseDto> findUserById(@PathVariable String login) {
        UserResponseDto responseDto = userService.findUserByLogin(login);

        return ResponseEntity.ok( responseDto );
    }
}
