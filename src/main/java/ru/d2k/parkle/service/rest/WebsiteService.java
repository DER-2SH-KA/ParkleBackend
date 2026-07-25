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
import ru.d2k.parkle.exception.WebsiteNotFoundException;
import ru.d2k.parkle.model.CustomUserDetails;
import ru.d2k.parkle.utils.mapper.WebsiteMapper;
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
    private final WebsiteDao dao;

    @Autowired
    private final WebsiteMapper mapper;

    @Transactional(readOnly = true)
    public WebsiteResponseDto findById(UUID id) {
        log.info("Getting website by ID: {}...", id);

        WebsiteCache website = dao.getById(id).orElseThrow(() ->
                new WebsiteNotFoundException("Website was not found with ID: " + id));

        log.info("Website with ID '{}' was founded", id);

        return mapper.toResponseDto(website);
    }

    @Transactional(readOnly = true)
    public List<WebsiteResponseDto> findByUserLogin() {
        CustomUserDetails userDetails = (CustomUserDetails) SecurityContextHolder.getContext()
                .getAuthentication().getPrincipal();

        log.info("Getting websites by user login: {}...", userDetails.getUsername());

        List<WebsiteCache> websites = dao.getAllByUserLogin(userDetails.getCache());

        log.info("Websites was found: {}", websites.size());

        return websites.stream().map(mapper::toResponseDto).toList();
    }

    @Transactional
    public WebsiteResponseDto create(WebsiteCreateDto createWebsiteDto) {
        log.info("Creating website: {}...", createWebsiteDto.toString());

        CustomUserDetails userDetails = (CustomUserDetails) SecurityContextHolder.getContext()
                .getAuthentication().getPrincipal();

        Website website = Website.create(userDao.getReferenceById(userDetails.getCache().id()),
                createWebsiteDto.hexColor(), createWebsiteDto.title(), createWebsiteDto.description(),
                createWebsiteDto.url());
        WebsiteCache savedWebsite = dao.create(website);

        log.info("Website was created: {}", savedWebsite);

        return mapper.toResponseDto(savedWebsite);
    }

    @Transactional
    public WebsiteResponseDto updateById(UUID id, WebsiteUpdateDto updateWebsiteDto) {
        log.info("Updating website by ID '{}'...", id);

        if (id == null) {
            return null;
        }

        String userLogin = ((CustomUserDetails) SecurityContextHolder.getContext().getAuthentication()
                .getPrincipal()).getUsername();

        Optional<WebsiteCache> updatedWebsite = dao.updateById(id, updateWebsiteDto, userLogin);

        log.info("Website with ID '{}' was updated", id);

        return mapper.toResponseDto(updatedWebsite.orElseThrow(() ->
                new WebsiteNotFoundException(String.format("Website with ID '%s' not found and not updated!", id))));
    }

    public boolean deleteById(UUID id) {
        log.info("Deleting website by ID '{}'", id);

        if (Objects.nonNull(id)) {
            if (dao.deleteById(id)) {
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
}