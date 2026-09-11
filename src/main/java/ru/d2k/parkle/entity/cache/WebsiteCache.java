package ru.d2k.parkle.entity.cache;

import java.util.Set;
import java.util.UUID;

public record WebsiteCache(UUID id, String title, String description, String url, String hexColor,
                           Set<WebsiteTagCache> tags) { }