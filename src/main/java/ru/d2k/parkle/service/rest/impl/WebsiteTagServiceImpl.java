package ru.d2k.parkle.service.rest.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.d2k.parkle.dao.UserDao;
import ru.d2k.parkle.dao.WebsiteTagDao;
import ru.d2k.parkle.dto.WebsiteTagCreateDto;
import ru.d2k.parkle.dto.WebsiteTagResponseDto;
import ru.d2k.parkle.dto.WebsiteTagUpdateDto;
import ru.d2k.parkle.entity.UserEntity;
import ru.d2k.parkle.entity.WebsiteTagEntity;
import ru.d2k.parkle.model.domain.WebsiteTag;
import ru.d2k.parkle.model.security.CustomUserDetails;
import ru.d2k.parkle.service.rest.WebsiteTagService;
import ru.d2k.parkle.utils.mapper.WebsiteTagMapper;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class WebsiteTagServiceImpl implements WebsiteTagService {

    private final WebsiteTagDao dao;
    private final WebsiteTagMapper mapper;
    private final UserDao userDao;

    @Override
    @Transactional(readOnly = true)
    public List<WebsiteTagResponseDto> findAllByUserLogin() {
        CustomUserDetails userDetails = (CustomUserDetails) SecurityContextHolder.getContext()
                .getAuthentication().getPrincipal();

        return dao.getAllByUserLogin(userDetails.getUsername()).stream()
                .map(mapper::toResponseDto)
                .toList();
    }

    @Override
    @Transactional
    public WebsiteTagResponseDto create(WebsiteTagCreateDto createDto) {
        UserEntity userEntity = userDao.getReferenceById(createDto.userId());

        WebsiteTagEntity entity = WebsiteTagEntity.create(userEntity, createDto.title(), createDto.hex());

        return mapper.toResponseDto(dao.create(entity));
    }

    @Override
    @Transactional
    public WebsiteTagResponseDto updateById(WebsiteTagUpdateDto updateDto) {
        WebsiteTag updatedTagCache = dao.updateById(updateDto.id(), updateDto);

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
