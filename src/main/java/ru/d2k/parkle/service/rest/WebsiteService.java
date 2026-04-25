package ru.d2k.parkle.service.rest;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.d2k.parkle.dao.UserDao;
import ru.d2k.parkle.dao.WebsiteDao;
import ru.d2k.parkle.dto.WebsiteCreateDto;
import ru.d2k.parkle.dto.WebsiteResponseDto;
import ru.d2k.parkle.dto.WebsiteUpdateDto;
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
@Service
@RequiredArgsConstructor
public class WebsiteService {

    @Autowired
    private final UserDao userDao;

    @Autowired
    private final WebsiteDao websiteDao;

    @Autowired
    private final WebsiteMapper websiteMapper;

    @Autowired
    private final ExtremismUtil extremismUtil;

    @Transactional(readOnly = true)
    public List<WebsiteResponseDto> findWebsites() {
        log.info("Getting all websites...");

        List<WebsiteResponseDto> dtos = websiteDao.getAll().stream()
                .map(websiteMapper::toResponseDto)
                .toList();

        log.info("Websites was founded: {}", dtos.size());

        return dtos;
    }

    @Transactional(readOnly = true)
    public WebsiteResponseDto findWebsiteById(UUID id) {
        log.info("Getting website by ID: {}...", id);

        WebsiteCache website = websiteDao.getById(id).orElseThrow(() ->
                new WebsiteNotFoundException("Website was not found with ID: " + id));

        log.info("Website with ID '{}' was founded", id);

        return websiteMapper.toResponseDto(website);
    }

    @Transactional(readOnly = true)
    public List<WebsiteResponseDto> findWebsiteByUserLogin() {
        CustomUserDetails userDetails = (CustomUserDetails) SecurityContextHolder.getContext()
                .getAuthentication().getPrincipal();

        log.info("Getting websites by user login: {}...", userDetails.getUsername());

        List<WebsiteCache> websites = websiteDao.getAllByUserLogin(userDetails.getCache());

        log.info("Websites was found: {}", websites.size());

        return websites.stream().map(websiteMapper::toResponseDto).toList();
    }

    @Transactional
    public WebsiteResponseDto createWebsite(WebsiteCreateDto dto) {
        log.info("Creating website: {}...", dto.toString());

        this.checkOnExtremism(dto.url());

        CustomUserDetails userDetails = (CustomUserDetails) SecurityContextHolder.getContext()
                .getAuthentication().getPrincipal();

        Website website = Website.create(userDao.getReferenceById(userDetails.getCache().id()), dto.hexColor(),
                dto.title(), dto.description(), dto.url());
        WebsiteCache savedWebsite = websiteDao.create(website, userDetails.getUsername());

        log.info("Website was created: {}", savedWebsite);

        return websiteMapper.toResponseDto(savedWebsite);
    }

    @Transactional
    public WebsiteResponseDto updateWebsite(UUID id, WebsiteUpdateDto udto) {
        log.info("Updating website by ID '{}'...", id);

        if (id == null) {
            return null;
        }

        this.checkOnExtremism(udto.url());

        String userLogin = ((CustomUserDetails) SecurityContextHolder.getContext().getAuthentication()
                .getPrincipal()).getUsername();

        Optional<WebsiteCache> updatedWebsite = websiteDao.update(id, udto, userLogin);

        log.info("Website with ID '{}' was updated", id);

        return websiteMapper.toResponseDto(updatedWebsite.orElseThrow(() ->
                new WebsiteNotFoundException(String.format("Website with ID '%s' not found and not updated!", id))));
    }

    public boolean deleteWebsite(UUID id) {
        log.info("Deleting website by ID '{}'", id);

        CustomUserDetails userDetails = (CustomUserDetails) SecurityContextHolder.getContext()
                .getAuthentication().getPrincipal();

        if (Objects.nonNull(id)) {
            if (websiteDao.deleteById(id, userDetails.getUsername())) {
                log.info("Website with ID  '{}' was deleted", id);

                return true;
            } else {
                log.error("Website with ID '{}' wasn't deleted", id);
            }
        } else {
            log.error("Website's ID equals null");
        }

        return false;
    }

    private void checkOnExtremism(String url) {
        log.info("Checking website with url '{}' on extremism...", url);

        boolean isExtremism = this.isExtremism(url);

        if (isExtremism) {
            log.info("Website URL is extremism!");

            throw new WebsiteIsExtremismSourceException("Website '" + url + "' is extremism!");
        }
    }

    private boolean isExtremism(String websiteUrl) {
        return extremismUtil.checkOnExtremism(websiteUrl);
    }
}