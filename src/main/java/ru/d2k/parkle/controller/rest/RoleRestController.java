package ru.d2k.parkle.controller.rest;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.d2k.parkle.controller.ApiRoutes;
import ru.d2k.parkle.dto.RoleCreateDto;
import ru.d2k.parkle.dto.RoleResponseDto;
import ru.d2k.parkle.dto.RoleUpdateDto;
import ru.d2k.parkle.service.rest.RoleService;
import java.util.Set;
import java.util.UUID;

@RequiredArgsConstructor
@RestController
@RequestMapping(ApiRoutes.API + ApiRoutes.ROLE_API)
public class RoleRestController {

    @Autowired
    private final RoleService service;

    @GetMapping
    public ResponseEntity<Set<RoleResponseDto>> findAll() {
        return ResponseEntity.ok(service.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<RoleResponseDto> find(@PathVariable("id") UUID id) {
        return ResponseEntity.ok(service.findById(id));
    }

    @GetMapping("/name/{name}")
    public ResponseEntity<RoleResponseDto> findByName(@PathVariable("name") String name) {
        return ResponseEntity.ok(service.findByName(name));
    }

    @PostMapping("/new")
    public ResponseEntity<RoleResponseDto> create(@Valid @RequestBody RoleCreateDto createRoleDto) {
        return ResponseEntity.ok(service.create(createRoleDto));
    }

    @PatchMapping("/update/{id}")
    public ResponseEntity<RoleResponseDto> update(@PathVariable("id") UUID id,
                                                  @Valid @RequestBody RoleUpdateDto updateRoleDto) {
        return ResponseEntity.ok(service.update(id, updateRoleDto));
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> delete(@PathVariable("id") UUID id) {
        boolean result = service.delete(id);

        return result ? ResponseEntity.ok().build() : ResponseEntity.internalServerError()
                .body("Role was not deleted or not exists!");
    }
}