package ru.d2k.parkle.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import java.util.Set;
import java.util.UUID;

public record WebsiteUpdateDto(

        @JsonProperty("id")
        @NotNull(message = "WebsiteUpdateDto id can't be null!")
        UUID id,

        @JsonProperty("hexColor")
        @NotBlank(message = "WebsiteUpdateDto hexColorValue cannot be null or blank")
        @Pattern(regexp = "^#([a-fA-F0-9]{3}|[a-fA-F0-9]{6})$",
                message = "WebsiteUpdateDto hexColor must be type as #fff or #FFFFFF")
        String hexColor,

        @JsonProperty("title")
        @NotBlank(message = "WebsiteUpdateDto title can't be null or blank")
        @Size(max = 40, message = "WebsiteUpdateDto title length must be lower than or equal 40 symbols")
        String title,

        @JsonProperty("description")
        @Size(max = 255, message = "WebsiteUpdateDto title length must be lower than 255 symbols")
        String description,

        @JsonProperty("url")
        @NotBlank(message = "WebsiteUpdateDto URL can't be null or blank")
        String url,

        @JsonProperty("tags")
        @NotNull(message = "WebsiteUpdateDto tags cannot be null")
        Set<WebsiteTagResponseDto> tags
) { }