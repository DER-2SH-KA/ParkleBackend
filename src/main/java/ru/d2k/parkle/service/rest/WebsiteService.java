package ru.d2k.parkle.service.rest;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.d2k.parkle.dto.*;
import ru.d2k.parkle.entity.User;
import ru.d2k.parkle.entity.Website;
import ru.d2k.parkle.repository.UserRepository;
import ru.d2k.parkle.repository.WebsiteRepository;
import ru.d2k.parkle.utils.mapper.WebsiteMapper;

import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Slf4j
@RequiredArgsConstructor
@Service
public class WebsiteService {
    private final UserRepository userRepository;
    private final WebsiteRepository websiteRepository;
    private final WebsiteMapper websiteMapper;

    /**
     * Return all websites by all users from DB as DTO.
     * @return List of {@link WebsiteResponseDto}.
     * **/
    @Transactional(readOnly = true)
    public List<WebsiteResponseDto> findWebsites() {
        log.info("Getting all websites...");

        List<WebsiteResponseDto> dtos = websiteRepository.findAll().stream()
                .map(websiteMapper::toResponseDto)
                .toList();

        log.info("Websites was founded: {}", dtos.size());
        return dtos;
    }

    /**
     * Return website by ID as DTO.
     * @param id ID of website.
     * @return {@link WebsiteResponseDto} dto.
     * **/
    @Transactional(readOnly = true)
    public WebsiteResponseDto findWebsiteById(UUID id) {
        log.info("Getting website by ID: {}...", id);

        Website website = websiteRepository.findById(id)
                .orElseThrow(() ->
                        new EntityNotFoundException("Website was not found with ID: " + id)
                );

        log.info("Website with ID = {} was founded", id);
        return websiteMapper.toResponseDto(website);
    }

    /**
     * Return websites by user ID as DTO.
     * @param userId user ID.
     * @return {@link WebsiteResponseDto} dto.
     * **/
    @Transactional(readOnly = true)
    public List<WebsiteResponseDto> findWebsiteByUserId(UUID userId) {
        log.info("Getting websites by user ID: {}...", userId);

        List<Website> websites = websiteRepository.findByUserIdOrderByTitleAsc(userId);

        log.info("Websites was found: {}", websites.size());
        return websites.stream().map(websiteMapper::toResponseDto).toList();
    }

    /**
     * Create website from DTO and return as DTO.
     * @param dto {@link WebsiteCreateDto} of new website.
     * @return {@link WebsiteResponseDto} dto.
     * **/
    @Transactional
    public WebsiteResponseDto createWebsite(WebsiteCreateDto dto) {
        log.info("Creating website: {}...", dto.toString());

        User user = userRepository.findById(dto.getUserId()).orElseThrow(() ->
                new EntityNotFoundException("User was not found with ID: " + dto.getUserId())
        );
        Website website = Website.create(user, dto.getHexColor(), dto.getTitle(), dto.getDescription(), dto.getUrl());
        website = websiteRepository.save(website);

        log.info("Website was created: {}", website);
        return websiteMapper.toResponseDto(website);
    }

    /**
     * Update website from DTO and return as DTO.
     * @param udto {@link WebsiteUpdateDto} dto of website to update.
     * @return {@link WebsiteResponseDto} dto.
     * **/
    @Transactional
    public WebsiteResponseDto updateWebsite(UUID id, WebsiteUpdateDto udto) {
        log.info("Updating website by ID: {}...", id);

        if (id == null) return null;

        Website website = websiteRepository.findById(id)
                .orElseThrow(() ->
                        new EntityNotFoundException("Website was not found with ID: " + id)
                );

        websiteMapper.updateByDto( website, udto );
        website = websiteRepository.save(website);

        log.info("Website with ID = {} was updated", id);
        return websiteMapper.toResponseDto(website);
    }

    /**
     * Delete website by ID.
     * @param id Website's ID.
     * **/
    public boolean deleteWebsite(UUID id) {
        log.info("Deleting website by ID: {}", id);

        if (Objects.nonNull(id)) {

            if (!websiteRepository.existsById(id)) {
                log.info("Website with ID = {} is already not exists", id);
                return false;
            }

            websiteRepository.deleteById(id);

            if (!websiteRepository.existsById(id)) {
                log.info("Website with ID = {} was deleted", id);
                return true;
            }
            else {
                log.info("Website with ID = {} wasn't deleted", id);
            }
        }
        else { log.info("Website's ID equals null and now was deleted"); }

        return false;
    }
}
