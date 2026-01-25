package ru.d2k.parkle.entity.cache;

import java.util.UUID;

public record WebsiteCache(
    UUID id,
    UUID userId,
    String hexColor,
    String title,
    String description,
    String url
) { }
