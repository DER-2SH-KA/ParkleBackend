package ru.d2k.parkle.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class WebsiteUpdateDto {

    @Pattern(
            regexp = "^#([a-fA-F0-9]{3}|[a-fA-F0-9]{6})$",
            message = "WebsiteUpdateDto hexColor must be type as #fff or #FFFFFF"
    )
    private String hexColor;

    private UUID userId;

    @Size(
            max = 40,
            message = "WebsiteUpdateDto title length must be lower than or equal 40 symbols"
    )
    @Pattern(regexp = "^[a-zA-Z0-9]{3,40}$", message = "WebsiteUpdateDto title not allowed by regexp pattern")
    private String title;

    @Size(max = 255, message = "WebsiteUpdateDto description length must be lower than 255 symbols")
    private String description;

    private String url;

}
