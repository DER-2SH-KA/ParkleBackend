package ru.d2k.parkle.dto;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class WebsiteCreateDto {

    @NotNull(message = "WebsiteCreateDto userId can't be null")
    private UUID userId;

    @NotBlank(message = "WebsiteCreateDto hexColorValue cannot be null or blank")
    @Pattern(
            regexp = "^#([a-fA-F0-9]{3}|[a-fA-F0-9]{6})$",
            message = "WebsiteCreateDto hexColor must be type as #fff or #FFFFFF"
    )
    private String hexColor;

    @NotBlank(message = "WebsiteCreateDto title can't be null or blank")
    @Size(max = 40, message = "WebsiteCreateDto title length must be lower than or equal 40 symbols")
    @Pattern(regexp = "^[a-zA-Z0-9]{3,50}$", message = "WebsiteCreateDto title not allowed by regexp pattern")
    private String title;

    @Size(max = 255, message = "WebsiteCreateDto title length must be lower than 255 symbols")
    private String description;

    @NotBlank(message = "WebsiteCreateDto URL can't be null or blank")
    private String url;
}
