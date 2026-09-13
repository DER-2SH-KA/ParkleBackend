package ru.d2k.parkle.service.rest.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.d2k.parkle.dao.WebsiteTagDao;
import ru.d2k.parkle.dto.WebsiteTagCreateDto;
import ru.d2k.parkle.dto.WebsiteTagResponseDto;
import ru.d2k.parkle.dto.WebsiteTagUpdateDto;
import ru.d2k.parkle.entity.WebsiteTagEntity;
import ru.d2k.parkle.entity.cache.WebsiteTagCache;
import ru.d2k.parkle.service.rest.WebsiteTagService;
import ru.d2k.parkle.utils.mapper.WebsiteTagMapper;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class WebsiteTagServiceImpl implements WebsiteTagService {

    private final WebsiteTagDao dao;
    private final WebsiteTagMapper mapper;

    @Override
    @Transactional(readOnly = true)
    public List<WebsiteTagResponseDto> findAllByUserLogin(String login) {
        return dao.getAllByUserLogin(login).stream()
                .map(mapper::toResponseDto)
                .toList();
    }

    @Override
    @Transactional
    public WebsiteTagResponseDto create(WebsiteTagCreateDto createDto) {
        WebsiteTagEntity entity = WebsiteTagEntity.create(createDto.userId(), createDto.title(), createDto.hex());

        return mapper.toResponseDto(dao.create(entity));
    }

    @Override
    @Transactional
    public WebsiteTagResponseDto updateById(WebsiteTagUpdateDto updateDto) {
        WebsiteTagCache updatedTagCache = dao.updateById(updateDto.id(), updateDto);

        return mapper.toResponseDto(updatedTagCache);
    }

    @Override
    @Transactional
    public void deleteById(UUID id) {
        dao.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean existsById(UUID id) {
        return dao.existsById(id);
    }
}
