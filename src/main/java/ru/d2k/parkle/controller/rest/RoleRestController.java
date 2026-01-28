package ru.d2k.parkle.controller.rest;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.d2k.parkle.controller.ApiPaths;
import ru.d2k.parkle.dto.RoleCreateDto;
import ru.d2k.parkle.dto.RoleResponseDto;
import ru.d2k.parkle.dto.RoleUpdateDto;
import ru.d2k.parkle.service.rest.RoleService;

import java.util.Set;
import java.util.UUID;

@RestController
@RequestMapping(ApiPaths.ROLE_API)
@RequiredArgsConstructor
public class RoleRestController {
    private final RoleService roleService;

    /**
     * Get all roles from database.
     * @return {@link ResponseEntity} with Set of {@link RoleUpdateDto}.
     * **/
    @GetMapping
    public ResponseEntity<Set<RoleResponseDto>> findAll() {
        Set<RoleResponseDto> dtos = roleService.findAll();

        return ResponseEntity.ok(dtos);
    }

    /**
     * Get role by ID.
     * @param id ID of role.
     * @return {@link ResponseEntity} with {@link RoleUpdateDto}.
     * **/
    @GetMapping("/{id}")
    public ResponseEntity<RoleResponseDto> findById(@PathVariable UUID id) {
        RoleResponseDto dto = roleService.findById(id);

        return ResponseEntity.ok(dto);
    }

    /**
     * Get role by name.
     * @param name name of role.
     * @return {@link ResponseEntity} with {@link RoleUpdateDto}.
     * **/
    @GetMapping("/name/{name}")
    public ResponseEntity<RoleResponseDto> findByName(@PathVariable String name) {
        RoleResponseDto dto = roleService.findByName(name);

        return ResponseEntity.ok(dto);
    }

    /**
     * Create new {@link ru.d2k.parkle.entity.Role}.
     * @param dto DTO of new role.
     * @return {@link ResponseEntity} with {@link RoleUpdateDto}.
     * **/
    @PostMapping("/new")
    public ResponseEntity<RoleResponseDto> create(@Valid @RequestBody RoleCreateDto dto) {
        RoleResponseDto newDto = roleService.create(dto);

        return ResponseEntity.ok(newDto);
    }

    /**
     * Update role by ID.
     * @param id ID of role.
     * @param dto DTO witn new data for role.
     * @return {@link ResponseEntity} with {@link RoleUpdateDto}.
     * **/
    @PatchMapping("/update/{id}")
    public ResponseEntity<RoleResponseDto> updateById(@PathVariable UUID id, @Valid @RequestBody RoleUpdateDto dto) {
        RoleResponseDto newDto = roleService.update(id, dto);

        return ResponseEntity.ok(newDto);
    }

    /**
     * Delete role by ID.
     * @param id ID of role.
     * @return {@link ResponseEntity} with ok() status.
     * **/
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteById(@PathVariable UUID id) {
        boolean result = roleService.delete(id);

        return result
                ? ResponseEntity.ok().build()
                : ResponseEntity
                    .internalServerError()
                    .body("Role was not deleted or not exists!");
    }
}
