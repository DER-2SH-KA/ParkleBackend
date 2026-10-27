package ru.d2k.parkle.service.rest;

import ru.d2k.parkle.dto.WebsiteTagCreateDto;
import ru.d2k.parkle.dto.WebsiteTagResponseDto;
import ru.d2k.parkle.dto.WebsiteTagUpdateDto;
import java.util.List;
import java.util.UUID;

public interface WebsiteTagService {

    List<WebsiteTagResponseDto> findAllByUserLogin();
    WebsiteTagResponseDto create(WebsiteTagCreateDto createDto);
    WebsiteTagResponseDto updateById(WebsiteTagUpdateDto updateDto);
    void deleteById(UUID id);
    boolean existsById(UUID id);
}
