package ru.d2k.parkle.service.rest;

import ru.d2k.parkle.dto.WebsiteCreateDto;
import ru.d2k.parkle.dto.WebsiteResponseDto;
import ru.d2k.parkle.dto.WebsiteUpdateDto;
import java.util.List;
import java.util.UUID;

public interface WebsiteService {

    WebsiteResponseDto findById(UUID id);
    List<WebsiteResponseDto> findByUserLogin();
    WebsiteResponseDto create(WebsiteCreateDto createWebsiteDto);
    WebsiteResponseDto updateById(WebsiteUpdateDto updateWebsiteDto);
    void deleteById(UUID id);
    boolean existsById(UUID id);
}