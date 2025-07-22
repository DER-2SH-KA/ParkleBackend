package ru.d2k.parkle.controller.rest;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.d2k.parkle.controller.ApiPaths;
import ru.d2k.parkle.dto.WebsiteCreateDto;
import ru.d2k.parkle.dto.WebsiteResponseDto;
import ru.d2k.parkle.dto.WebsiteUpdateDto;
import ru.d2k.parkle.service.rest.WebsiteService;

import java.util.List;
import java.util.UUID;

@Slf4j
@RestController
@RequestMapping(value = ApiPaths.WEBSITE_API)
@RequiredArgsConstructor
public class WebsitesRestController {
    private final WebsiteService websiteService;

    //TODO: ONLY FOR TESTS! DELETE AFTER THEM.
    /**
     * Get all websites from database as DTO.
     * @return List of {@link WebsiteResponseDto} objects.
     * **/
    @GetMapping
    public ResponseEntity<List<WebsiteResponseDto>> findWebsites() {
        log.info("Given GET request to return all websites");

        List<WebsiteResponseDto> websiteResponseDtos = websiteService.findWebsites();

        log.info("GET request was served");
        return ResponseEntity.ok( websiteResponseDtos );
    }

    /**
     * Get all websites from database by user ID as DTO.
     * @param userId user's ID.
     * @return List of {@link WebsiteResponseDto} objects.
     * **/
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<WebsiteResponseDto>> findWebsitesByUserId(@PathVariable UUID userId) {
        log.info("Given GET request to return all websites by user ID: {}", userId);

        List<WebsiteResponseDto> websiteResponseDtos = websiteService.findWebsiteByUserId(userId);

        log.info("GET request was served");
        return ResponseEntity.ok( websiteResponseDtos );
    }

    /**
     * Get all websites from database by ID as DTO.
     * @param id website's ID.
     * @return {@link WebsiteResponseDto} object.
     * **/
    @GetMapping("/{id}")
    public ResponseEntity<WebsiteResponseDto> findWebsitesById(@PathVariable UUID id) {
        log.info("Given GET request to return website by ID: {}", id);

        WebsiteResponseDto dto = websiteService.findWebsiteById(id);

        log.info("GET request was served");
        return ResponseEntity.ok( dto );
    }

    /**
     * Create new website by DTO.
     * @param cdto {@link WebsiteCreateDto} object of website to save.
     * @return {@link WebsiteResponseDto} object.
     * **/
    @PostMapping("/new")
    public ResponseEntity<WebsiteResponseDto> createWebsite(@RequestBody WebsiteCreateDto cdto) {
        log.info("Given POST request to create website");

        WebsiteResponseDto dto = websiteService.createWebsite(cdto);

        log.info("POST request was served");
        return ResponseEntity.ok( dto );
    }

    /**
     * Update website by DTO.
     * @param id website's ID.
     * @param udto {@link WebsiteUpdateDto} object for update website.
     * @return updated {@link WebsiteResponseDto} object.
     * **/
    @PutMapping("/{id}")
    public ResponseEntity<WebsiteResponseDto> updateWebsite(@PathVariable UUID id, @RequestBody WebsiteUpdateDto udto) {
        log.info("Given PUT request to update website by ID: {}", id);

        WebsiteResponseDto dto = websiteService.updateWebsite(id, udto);

        log.info("PUT request was served");
        return ResponseEntity.ok( dto );
    }

    /**
     * Delete website by ID.
     * @param id website's ID.
     * @return OK status.
     * **/
    @DeleteMapping("/{id}")
    public ResponseEntity<WebsiteResponseDto> deleteWebsite(@PathVariable UUID id) {
        log.info("Given DELETE request to delete website by ID: {}", id);

        websiteService.deleteWebsite(id);

        log.info("DELETE request was served");
        return ResponseEntity.ok().build();
    }
}
