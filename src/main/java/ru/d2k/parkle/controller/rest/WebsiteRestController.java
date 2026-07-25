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
import ru.d2k.parkle.dto.WebsiteCreateDto;
import ru.d2k.parkle.dto.WebsiteResponseDto;
import ru.d2k.parkle.dto.WebsiteUpdateDto;
import ru.d2k.parkle.service.rest.WebsiteService;
import java.util.List;
import java.util.UUID;

@RequiredArgsConstructor
@RestController
@RequestMapping(ApiRoutes.API + ApiRoutes.WEBSITE_API)
public class WebsiteRestController {

    @Autowired
    private final WebsiteService service;

    @GetMapping("/me")
    public ResponseEntity<List<WebsiteResponseDto>> findAllByUserLogin() {
        return ResponseEntity.ok(service.findByUserLogin());
    }

    @GetMapping("/{id}")
    public ResponseEntity<WebsiteResponseDto> findById(@PathVariable(name = "id") UUID id) {
        return ResponseEntity.ok(service.findById(id));
    }

    @PostMapping("/new")
    public ResponseEntity<WebsiteResponseDto> create(@Valid @RequestBody WebsiteCreateDto createWebsiteDto) {
        return ResponseEntity.ok(service.create(createWebsiteDto));
    }

    @PatchMapping("/update/{id}")
    public ResponseEntity<WebsiteResponseDto> updateById(@PathVariable("id") UUID id,
                                                         @Valid @RequestBody WebsiteUpdateDto updateWebsiteDto) {
        return ResponseEntity.ok(service.updateById(id, updateWebsiteDto));
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteById(@PathVariable("id") UUID id) {
        boolean result = service.deleteById(id);

        return result ? ResponseEntity.ok().build() : ResponseEntity.internalServerError()
                .body("Website was not deleted or not exists!");
    }
}