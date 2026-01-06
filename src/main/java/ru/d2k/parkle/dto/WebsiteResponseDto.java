package ru.d2k.parkle.dto;

import java.util.UUID;

public record WebsiteResponseDto(
        UUID id,
        String userLogin,
        String hexColor,
        String title,
        String description,
        String url
) { }
