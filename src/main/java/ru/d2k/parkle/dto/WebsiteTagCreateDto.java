package ru.d2k.parkle.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.UUID;

public record WebsiteTagCreateDto(

        @JsonProperty("userId")
        UUID userId,

        @JsonProperty("title")
        String title,

        @JsonProperty("hex")
        String hex
) { }
