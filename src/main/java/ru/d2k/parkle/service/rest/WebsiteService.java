package ru.d2k.parkle.service.rest;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.d2k.parkle.dao.UserDao;
import ru.d2k.parkle.dao.WebsiteDao;
import ru.d2k.parkle.dto.*;
import ru.d2k.parkle.entity.Website;
import ru.d2k.parkle.entity.cache.WebsiteCache;
import ru.d2k.parkle.exception.WebsiteIsExtremismSourceException;
import ru.d2k.parkle.exception.WebsiteNotFoundException;
import ru.d2k.parkle.model.CustomUserDetails;
import ru.d2k.parkle.utils.mapper.WebsiteMapper;
import ru.d2k.parkle.utils.safety.extremism.ExtremismUtil;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

@Slf4j
@RequiredArgsConstructor
@Service
public class WebsiteService {
    private final UserDao userDao;
    private final WebsiteDao websiteDao;
    private final WebsiteMapper websiteMapper;
    // private final ExtremismUtil extremismUtil;

    /**
     * Return all websites by all users from DB as DTO.
     * @return List of {@link WebsiteResponseDto}.
     * **/
    @Transactional(readOnly = true)
    public List<WebsiteResponseDto> findWebsites() {
        log.info("Getting all websites...");

        List<WebsiteResponseDto> dtos = websiteDao.getAll().stream()
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

        WebsiteCache website = websiteDao.getById(id)
                .orElseThrow(() ->
                        new WebsiteNotFoundException("Website was not found with ID: " + id)
                );

        log.info("Website with ID '{}' was founded", id);
        return websiteMapper.toResponseDto(website);
    }

    /**
     * Return websites by user login as DTO.
     * @return {@link WebsiteResponseDto} dto.
     * **/
    @Transactional(readOnly = true)
    public List<WebsiteResponseDto> findWebsiteByUserLogin() {

        CustomUserDetails userDetails = (CustomUserDetails) SecurityContextHolder.getContext()
                .getAuthentication()
                .getPrincipal();

        log.info("Getting websites by user login: {}...", userDetails.getUsername());

        List<WebsiteCache> websites = websiteDao.getAllByUserLogin(
                userDetails.getCache()
        );

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

        /*log.info("Checking website on extremism...");

        boolean isExtremism = this.isExtremism(dto.getUrl());

        if (isExtremism) {
            log.info("Website URL is extremism!");
            throw new WebsiteIsExtremismSourceException("Website '" + dto.getUrl() + "' is extremism!");
        }

        log.info("Website is not extremism");*/

        CustomUserDetails userDetails = (CustomUserDetails) SecurityContextHolder.getContext()
                .getAuthentication()
                .getPrincipal();

        Website website = Website.create(
                userDao.getReferenceById(userDetails.getCache().id()),
                dto.hexColor(),
                dto.title(),
                dto.description(),
                dto.url()
        );
        WebsiteCache savedWebsite = websiteDao.create(website, userDetails.getUsername());

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
        log.info("Updating website by ID '{}'...", id);

        if (id == null) return null;

        /*log.info("Checking website on extremism...");

        boolean isExtremism = this.isExtremism(udto.getUrl());

        if (isExtremism) {
            log.info("Website URL is extremism!");
            throw new WebsiteIsExtremismSourceException("Website '" + udto.getUrl() + "' is extremism!");
        }

        log.info("Website is not extremism");*/

        String userLogin = ((CustomUserDetails)
                SecurityContextHolder
                        .getContext()
                        .getAuthentication()
                        .getPrincipal()
        ).getUsername();

        Optional<WebsiteCache> updatedWebsite = websiteDao.update(id, udto, userLogin);

        log.info("Website with ID '{}' was updated", id);
        return websiteMapper.toResponseDto(
                updatedWebsite.orElseThrow(() ->
                        new WebsiteNotFoundException(String.format("Website with ID '%s' not found and not updated!", id))
                )
        );
    }

    /**
     * Delete website by ID.
     * @param id Website's ID.
     * **/
    public boolean deleteWebsite(UUID id) {
        log.info("Deleting website by ID '{}'", id);

        CustomUserDetails userDetails = (CustomUserDetails) SecurityContextHolder.getContext()
                .getAuthentication()
                .getPrincipal();

        if (Objects.nonNull(id)) {
            if (websiteDao.deleteById(id, userDetails.getUsername())) {
                log.info("Website with ID  '{}' was deleted", id);
                return true;
            }
            else {
                log.info("Website with ID '{}' wasn't deleted", id);
            }
        }
        else { log.info("Website's ID equals null"); }

        return false;
    }

    /*private boolean isExtremism(String websiteUrl) {
        return extremismUtil.checkOnExtremism(websiteUrl);
    }*/
}
