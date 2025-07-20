package ru.d2k.parkle.controller.rest;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.d2k.parkle.controller.ApiPaths;
import ru.d2k.parkle.dto.UserCreateDto;
import ru.d2k.parkle.dto.UserResponseDto;
import ru.d2k.parkle.dto.UserUpdateDto;
import ru.d2k.parkle.service.rest.UserService;

import java.util.UUID;
import java.util.Set;

@Slf4j
@RestController
@RequestMapping(ApiPaths.USER_API)
@RequiredArgsConstructor
public class UserRestController {
    private final UserService userService;

    @GetMapping
    public ResponseEntity<Set<UserResponseDto>> findUsers() {
        log.info("Given GET request to return all users");

        Set<UserResponseDto> userResponseDtos = userService.findUsers();

        log.info("GET request was served");
        return ResponseEntity.ok( userResponseDtos );
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserResponseDto> findUserById(@PathVariable UUID id) {
        log.info("Given GET request to return user by ID: {}", id);

        UserResponseDto responseDto = userService.findUserById(id);

        log.info("GET request was served");
        return ResponseEntity.ok( responseDto );
    }

    @PostMapping("/new")
    public ResponseEntity<UserResponseDto> createUser(@RequestBody UserCreateDto dto) {
        log.info("Given POST user request with DTO: {}", dto);

        UserResponseDto responseDto = userService.createUser(dto);

        log.info("POST request was served");
        return ResponseEntity.ok(responseDto);
    }

    @PutMapping("/{id}")
    public ResponseEntity<UserResponseDto> updateUserById(@PathVariable UUID id, @RequestBody UserUpdateDto dto) {
        log.info("Given PUT user request with ID: {}", id);

        UserResponseDto responseDto = userService.updateUser(id, dto);

        log.info("PUT request was served");
        return ResponseEntity.ok(responseDto);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteUserById(@PathVariable UUID id) {
        log.info("Given DELETE user request by ID: {}", id);

        userService.deleteUser(id);

        log.info("DELETE request was served");
        return ResponseEntity.ok().build();
    }
}
