package ru.d2k.parkle.dto;

import java.util.Set;
import java.util.UUID;

public record WebsiteResponseDto(UUID id, String hexColor, String title, String description,
                                 String url, Set<WebsiteTagResponseDto> tags) { }