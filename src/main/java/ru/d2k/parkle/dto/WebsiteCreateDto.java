package ru.d2k.parkle.dto;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.net.URI;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class WebsiteCreateDto {

    @NotNull(message = "WebisteCreate userId can't be null")
    private UUID userId;

    @NotBlank(message = "WebisteCreate hexColorValue cannot be null or blank")
    @Pattern(
            regexp = "^#([a-fA-F0-9]{3}|[a-fA-F0-9]{6})$",
            message = "WebisteCreate hexColor must be type as #fff or #FFFFFF"
    )
    private String hexColor;

    @NotBlank(message = "WebisteCreate title can't be null or blank")
    @Size(max = 100, message = "WebisteCreate title length must be lower than 100 symbols")
    private String title;

    @Size(max = 255, message = "WebisteCreate title length must be lower than 255 symbols")
    private String description;

    @NotNull(message = "WebisteCreate URL can't be null")
    private URI url;
}
