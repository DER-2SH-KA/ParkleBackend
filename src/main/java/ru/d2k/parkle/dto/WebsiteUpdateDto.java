package ru.d2k.parkle.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

import java.util.UUID;

public record WebsiteUpdateDto(
        @NotNull(message = "WebsiteUpdateDto id can't be null!")
        UUID id,

        @NotBlank(message = "WebsiteUpdateDto userLogin can't be null or blank!")
        String userLogin,

        @NotBlank(message = "WebsiteUpdateDto hexColorValue cannot be null or blank")
        @Pattern(
                regexp = "^#([a-fA-F0-9]{3}|[a-fA-F0-9]{6})$",
                message = "WebsiteUpdateDto hexColor must be type as #fff or #FFFFFF"
        )
        String hexColor,

        @NotBlank(message = "WebsiteUpdateDto title can't be null or blank")
        @Size(max = 40, message = "WebsiteUpdateDto title length must be lower than or equal 40 symbols")
        String title,

        @Size(max = 255, message = "WebsiteUpdateDto title length must be lower than 255 symbols")
        String description,

        @NotBlank(message = "WebsiteUpdateDto URL can't be null or blank")
        String url
) { }
