package ru.d2k.parkle.dto;

import java.util.UUID;

public record WebsiteResponseDto(
        UUID id,
        UUID userId,
        String hexColor,
        String title,
        String description,
        String url
) { }
