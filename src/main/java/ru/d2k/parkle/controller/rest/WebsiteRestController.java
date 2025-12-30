package ru.d2k.parkle.controller.rest;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.d2k.parkle.controller.ApiPaths;
import ru.d2k.parkle.dto.WebsiteCreateDto;
import ru.d2k.parkle.dto.WebsiteResponseDto;
import ru.d2k.parkle.dto.WebsiteUpdateDto;
import ru.d2k.parkle.service.rest.WebsiteService;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping(value = ApiPaths.WEBSITE_API)
@RequiredArgsConstructor
public class WebsiteRestController {
    private final WebsiteService websiteService;

    //TODO: ONLY FOR TESTS! DELETE AFTER THEM.
    /**
     * Get all websites from database as DTO.
     * @return List of {@link WebsiteResponseDto} objects.
     * **/
    @GetMapping
    public ResponseEntity<List<WebsiteResponseDto>> findWebsites() {
        List<WebsiteResponseDto> websiteResponseDtos = websiteService.findWebsites();

        return ResponseEntity.ok( websiteResponseDtos );
    }

    /**
     * Get all websites from database by user login ib JWT as DTO.
     * @return List of {@link WebsiteResponseDto} objects.
     * **/
    @GetMapping("/me")
    public ResponseEntity<List<WebsiteResponseDto>> findWebsitesByUserLogin() {
        List<WebsiteResponseDto> websiteResponseDtos = websiteService.findWebsiteByUserLogin();

        return ResponseEntity.ok( websiteResponseDtos );
    }

    /**
     * Get all websites from database by ID as DTO.
     * @param id website's ID.
     * @return {@link WebsiteResponseDto} object.
     * **/
    @GetMapping("/{websiteId}")
    public ResponseEntity<WebsiteResponseDto> findWebsitesById(@PathVariable(name = "websiteId") UUID id) {
        WebsiteResponseDto dto = websiteService.findWebsiteById(id);

        return ResponseEntity.ok( dto );
    }

    /**
     * Create new website by DTO.
     * @param cdto {@link WebsiteCreateDto} object of website to save.
     * @return {@link WebsiteResponseDto} object.
     * **/
    @PostMapping("/new")
    public ResponseEntity<WebsiteResponseDto> createWebsite(@Valid @RequestBody WebsiteCreateDto cdto) {
        WebsiteResponseDto dto = websiteService.createWebsite(cdto);

        return ResponseEntity.ok( dto );
    }

    /**
     * Update website by DTO.
     * @param id website's ID.
     * @param udto {@link WebsiteUpdateDto} object for update website.
     * @return updated {@link WebsiteResponseDto} object.
     * **/
    @PatchMapping("/update/{id}")
    public ResponseEntity<WebsiteResponseDto> updateWebsite(
            @PathVariable UUID id,
            @Valid @RequestBody WebsiteUpdateDto udto
    ) {
        WebsiteResponseDto dto = websiteService.updateWebsite(id, udto);

        return ResponseEntity.ok( dto );
    }

    /**
     * Delete website by ID.
     * @param id website's ID.
     * @return OK status.
     * **/
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteWebsite(@PathVariable UUID id) {
        boolean result = websiteService.deleteWebsite(id);

        return result
                ? ResponseEntity.ok().build()
                : ResponseEntity
                    .internalServerError()
                    .body("Website was not deleted or not exists!");
    }
}
