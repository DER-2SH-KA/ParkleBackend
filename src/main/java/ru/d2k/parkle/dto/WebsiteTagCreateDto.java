package ru.d2k.parkle.dto;

import java.util.UUID;

public record WebsiteTagCreateDto(UUID userId, String title, String hex) { }
