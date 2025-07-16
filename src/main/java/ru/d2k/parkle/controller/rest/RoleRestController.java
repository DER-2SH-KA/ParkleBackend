package ru.d2k.parkle.controller.rest;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.d2k.parkle.controller.ApiPaths;
import ru.d2k.parkle.dto.RoleDto;
import ru.d2k.parkle.service.rest.RoleService;

@RestController
@RequestMapping(ApiPaths.ROLE_API)
@RequiredArgsConstructor
public class RoleRestController {

    private final RoleService roleService;

    @PostMapping("new")
    public ResponseEntity<RoleDto> createRole(@RequestBody RoleDto dto) {
        RoleDto newDto = roleService.createRole(dto);

        return ResponseEntity.ok(newDto);
    }

}
