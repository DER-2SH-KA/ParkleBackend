package ru.d2k.parkle.controller.rest;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.d2k.parkle.controller.ApiPaths;
import ru.d2k.parkle.dto.RoleDto;
import ru.d2k.parkle.service.rest.RoleService;

import java.util.Set;
import java.util.UUID;

@Slf4j
@RestController
@RequestMapping(ApiPaths.ROLE_API)
@RequiredArgsConstructor
public class RoleRestController {

    private final RoleService roleService;

    @GetMapping
    public ResponseEntity<Set<RoleDto>> findRoles() {
        log.info("Given GET request for all roles");

        Set<RoleDto> dtos = roleService.findRoles();

        log.info("GET request was served");
        return ResponseEntity.ok(dtos);
    }

    @GetMapping("/{id}")
    public ResponseEntity<RoleDto> findRoleById(@PathVariable UUID id) {
        log.info("Given GET request for role by ID: {}", id);

        RoleDto dto = roleService.findRoleById(id);

        log.info("GET request was served");
        return ResponseEntity.ok(dto);
    }

    @GetMapping("/{name}")
    public ResponseEntity<RoleDto> findRoleByName(@PathVariable String name) {
        log.info("Given GET request for role by NAME: {}", name);

        RoleDto dto = roleService.findRoleByName(name);

        log.info("GET request was served");
        return ResponseEntity.ok(dto);
    }

    @PostMapping("new")
    public ResponseEntity<RoleDto> createRole(@RequestBody RoleDto dto) {
        log.info("Given POST request fot create role with data: {}", dto);

        RoleDto newDto = roleService.createRole(dto);

        log.info("POST request was served");
        return ResponseEntity.ok(newDto);
    }

    @PutMapping("/{id}")
    public ResponseEntity<RoleDto> updateRole(@PathVariable UUID id, @RequestBody RoleDto dto) {
        log.info("Given PUT request fot update role by ID: {}", id);

        RoleDto newDto = roleService.updateRole(id, dto);

        log.info("PUT request was served");
        return ResponseEntity.ok(newDto);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteRoleById(@PathVariable UUID id) {
        log.info("Given DELETE role request by ID: {}", id);

        roleService.deleteRole(id);

        log.info("DELETE request was served");
        return ResponseEntity.ok().build();
    }
}
