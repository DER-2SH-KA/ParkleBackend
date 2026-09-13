package ru.d2k.parkle.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Set;
import java.util.UUID;

public record WebsiteResponseDto(

        @JsonProperty("id")
        UUID id,

        @JsonProperty("hexColor")
        String hexColor,

        @JsonProperty("title")
        String title,

        @JsonProperty("description")
        String description,

        @JsonProperty("url")
        String url,

        @JsonProperty("tags")
        Set<WebsiteTagResponseDto> tags
) { }