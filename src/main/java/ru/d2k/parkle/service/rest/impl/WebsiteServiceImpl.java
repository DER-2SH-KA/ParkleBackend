package ru.d2k.parkle.service.rest.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.d2k.parkle.dao.UserDao;
import ru.d2k.parkle.dao.WebsiteDao;
import ru.d2k.parkle.dao.WebsiteTagDao;
import ru.d2k.parkle.dto.WebsiteCreateDto;
import ru.d2k.parkle.dto.WebsiteResponseDto;
import ru.d2k.parkle.dto.WebsiteUpdateDto;
import ru.d2k.parkle.entity.UserEntity;
import ru.d2k.parkle.entity.WebsiteEntity;
import ru.d2k.parkle.entity.WebsiteTagEntity;
import ru.d2k.parkle.entity.cache.WebsiteCache;
import ru.d2k.parkle.exception.entity.WebsiteEntityNotFoundException;
import ru.d2k.parkle.model.CustomUserDetails;
import ru.d2k.parkle.service.rest.WebsiteService;
import ru.d2k.parkle.utils.mapper.WebsiteMapper;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class WebsiteServiceImpl implements WebsiteService {

    private final WebsiteDao dao;
    private final WebsiteMapper mapper;
    private final UserDao userDao;
    private final WebsiteTagDao websiteTagDao;

    @Override
    @Transactional(readOnly = true)
    public WebsiteResponseDto findById(UUID id) {
        log.info("Getting website by ID: {}...", id);

        WebsiteCache website = dao.getById(id).orElseThrow(() ->
                new WebsiteEntityNotFoundException("WebsiteEntity was not found with ID: " + id));

        log.info("WebsiteEntity with ID '{}' was founded", id);

        return mapper.toResponseDto(website);
    }

    @Override
    @Transactional(readOnly = true)
    public List<WebsiteResponseDto> findByUserLogin() {
        CustomUserDetails userDetails = (CustomUserDetails) SecurityContextHolder.getContext()
                .getAuthentication().getPrincipal();

        log.info("Getting websites by user login: {}...", userDetails.getUsername());

        List<WebsiteCache> websites = dao.getAllByUserLogin(userDetails.getCache());

        log.info("Websites was found: {}", websites.size());

        return websites.stream().map(mapper::toResponseDto).toList();
    }

    @Override
    @Transactional
    public WebsiteResponseDto create(WebsiteCreateDto createWebsiteDto) {
        log.info("Creating website: {}...", createWebsiteDto.toString());

        CustomUserDetails userDetails = (CustomUserDetails) SecurityContextHolder.getContext()
                .getAuthentication().getPrincipal();

        UserEntity userEntity = userDao.getReferenceById(userDetails.getCache().id());
        Set<WebsiteTagEntity> tagEntities = createWebsiteDto.tags().stream()
                .map(dto -> websiteTagDao.getReferenceById(dto.id()))
                .collect(Collectors.toSet());

        WebsiteEntity website = WebsiteEntity.create(userEntity, createWebsiteDto.hexColor(), createWebsiteDto.title(),
                createWebsiteDto.description(), createWebsiteDto.url(), tagEntities);
        WebsiteCache savedWebsite = dao.create(website);

        log.info("WebsiteEntity was created: {}", savedWebsite);

        return mapper.toResponseDto(savedWebsite);
    }

    @Override
    @Transactional
    public WebsiteResponseDto updateById(WebsiteUpdateDto updateWebsiteDto) {
        log.info("Updating website by ID '{}'...", updateWebsiteDto.id());

        String userLogin = ((CustomUserDetails) SecurityContextHolder.getContext().getAuthentication()
                .getPrincipal()).getUsername();

        WebsiteCache updatedWebsite = dao.updateById(updateWebsiteDto.id(), updateWebsiteDto);

        log.info("WebsiteEntity with ID '{}' was updated", updateWebsiteDto.id());

        return mapper.toResponseDto(updatedWebsite);
    }

    @Override
    @Transactional
    public void deleteById(UUID id) {
        log.info("Deleting website by ID '{}'", id);

        if (Objects.nonNull(id)) {
            dao.deleteById(id);
        } else {
            log.error("WebsiteEntity's ID equals null");
        }
    }

    @Override
    @Transactional(readOnly = true)
    public boolean existsById(UUID id) {
        return dao.existsById(id);
    }
}