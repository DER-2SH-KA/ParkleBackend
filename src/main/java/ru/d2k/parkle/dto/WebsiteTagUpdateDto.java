package ru.d2k.parkle.dto;

import java.util.UUID;

public record WebsiteTagUpdateDto(UUID id, String title, String hex) { }
