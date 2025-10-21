package ru.d2k.parkle.controller.rest;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.d2k.parkle.controller.ApiPaths;
import ru.d2k.parkle.dto.RoleDto;
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
     * @return {@link ResponseEntity} with Set of {@link RoleDto}.
     * **/
    @GetMapping
    public ResponseEntity<Set<RoleDto>> findRoles() {
        Set<RoleDto> dtos = roleService.findRoles();

        return ResponseEntity.ok(dtos);
    }

    /**
     * Get role by ID.
     * @param id ID of role.
     * @return {@link ResponseEntity} with {@link RoleDto}.
     * **/
    @GetMapping("/{id}")
    public ResponseEntity<RoleDto> findRoleById(@PathVariable UUID id) {
        RoleDto dto = roleService.findRoleById(id);

        return ResponseEntity.ok(dto);
    }

    /**
     * Get role by name.
     * @param name name of role.
     * @return {@link ResponseEntity} with {@link RoleDto}.
     * **/
    @GetMapping("/name/{name}")
    public ResponseEntity<RoleDto> findRoleByName(@PathVariable String name) {
        RoleDto dto = roleService.findRoleByName(name);

        return ResponseEntity.ok(dto);
    }

    /**
     * Create new {@link ru.d2k.parkle.entity.Role}.
     * @param dto DTO of new role.
     * @return {@link ResponseEntity} with {@link RoleDto}.
     * **/
    @PostMapping("/new")
    public ResponseEntity<RoleDto> createRole(@Valid @RequestBody RoleDto dto) {
        RoleDto newDto = roleService.createRole(dto);

        return ResponseEntity.ok(newDto);
    }

    /**
     * Update role by ID.
     * @param id ID of role.
     * @param dto DTO witn new data for role.
     * @return {@link ResponseEntity} with {@link RoleDto}.
     * **/
    @PutMapping("/update/{id}")
    public ResponseEntity<RoleDto> updateRoleById(@PathVariable UUID id, @Valid @RequestBody RoleDto dto) {
        RoleDto newDto = roleService.updateRole(id, dto);

        return ResponseEntity.ok(newDto);
    }

    /**
     * Delete role by ID.
     * @param id ID of role.
     * @return {@link ResponseEntity} with ok() status.
     * **/
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteRoleById(@PathVariable UUID id) {
        boolean result = roleService.deleteRole(id);

        return result
                ? ResponseEntity.ok().build()
                : ResponseEntity
                    .internalServerError()
                    .body("Role was not deleted or not exists!");
    }
}
