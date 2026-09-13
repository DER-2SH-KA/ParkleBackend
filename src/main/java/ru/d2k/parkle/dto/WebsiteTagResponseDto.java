package ru.d2k.parkle.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.UUID;

public record WebsiteTagResponseDto(

        @JsonProperty("id")
        UUID id,

        @JsonProperty("title")
        String title,

        @JsonProperty("hex")
        String hex
) { }
