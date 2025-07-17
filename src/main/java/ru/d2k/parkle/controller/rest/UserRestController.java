package ru.d2k.parkle.controller.rest;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.d2k.parkle.controller.ApiPaths;
import ru.d2k.parkle.dto.UserCreateDto;
import ru.d2k.parkle.dto.UserResponseDto;
import ru.d2k.parkle.service.rest.UserService;

import java.util.UUID;
import java.util.Set;

@RestController
@RequestMapping(ApiPaths.USER_API)
@RequiredArgsConstructor
public class UserRestController {
    private final UserService userService;

    @GetMapping("/{id}")
    public ResponseEntity<UserResponseDto> findUserById(@PathVariable UUID id) {
        return ResponseEntity.ok(
                userService.findUserById(id)
        );
    }

    @GetMapping
    public ResponseEntity<Set<UserResponseDto>> findUsers() {
        return ResponseEntity.ok(
                userService.findUsers()
        );
    }

    @PostMapping("/new")
    public ResponseEntity<UserResponseDto> createUser(@RequestBody UserCreateDto dto) {
        return ResponseEntity.ok(
                userService.createUser(dto)
        );
    }

}
