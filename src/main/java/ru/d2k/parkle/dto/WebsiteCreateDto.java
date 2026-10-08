package ru.d2k.parkle.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import java.util.List;

public record WebsiteCreateDto(

        @JsonProperty("userLogin")
        @NotBlank(message = "WebsiteCreateDto userLogin can't be null or blank!")
        String userLogin,

        @JsonProperty("hexColor")
        @NotBlank(message = "WebsiteCreateDto hexColorValue cannot be null or blank")
        @Pattern(regexp = "^#([a-fA-F0-9]{3}|[a-fA-F0-9]{6})$",
                message = "WebsiteCreateDto hexColor must be type as #fff or #FFFFFF")
        String hexColor,

        @JsonProperty("title")
        @NotBlank(message = "WebsiteCreateDto title can't be null or blank")
        @Size(max = 40, message = "WebsiteCreateDto title length must be lower than or equal 40 symbols")
        String title,

        @JsonProperty("description")
        @Size(max = 255, message = "WebsiteCreateDto title length must be lower than 255 symbols")
        String description,

        @JsonProperty("url")
        @NotBlank(message = "WebsiteCreateDto URL can't be null or blank")
        String url,

        @JsonProperty("tags")
        @NotNull(message = "WebsiteCreateDto tags cannot be null")
        List<WebsiteTagResponseDto> tags
) { }