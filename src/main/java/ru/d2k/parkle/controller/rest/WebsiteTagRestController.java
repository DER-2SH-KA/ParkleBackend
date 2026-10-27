package ru.d2k.parkle.controller.rest;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
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
import ru.d2k.parkle.dto.WebsiteTagCreateDto;
import ru.d2k.parkle.dto.WebsiteTagUpdateDto;
import ru.d2k.parkle.service.rest.WebsiteTagService;
import java.util.UUID;

@RequiredArgsConstructor
@RestController
@RequestMapping(ApiRoutes.API + ApiRoutes.WEBSITE_TAG_API)
public class WebsiteTagRestController {

    private final WebsiteTagService service;

    @GetMapping("/me")
    public ResponseEntity<?> findAllByUserLogin() {
        return ResponseEntity.ok(service.findAllByUserLogin());
    }

    @PostMapping("/new")
    public ResponseEntity<?> create(@Valid @RequestBody WebsiteTagCreateDto createDto) {
        return ResponseEntity.ok(service.create(createDto));
    }

    @PatchMapping("/update")
    public ResponseEntity<?> updateById(@Valid @RequestBody WebsiteTagUpdateDto updateDto) {
        return ResponseEntity.ok(service.updateById(updateDto));
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteById(@PathVariable("id") UUID id) {
        service.deleteById(id);

        return !service.existsById(id) ? ResponseEntity.ok().build() : ResponseEntity.internalServerError()
                .body("Website tag was not deleted or not exists!");
    }
}
