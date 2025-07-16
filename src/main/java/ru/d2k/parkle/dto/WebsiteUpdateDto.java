package ru.d2k.parkle.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.URL;

import java.net.URI;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class WebsiteUpdateDto {

    @Pattern(
            regexp = "^#([a-fA-F0-9]{3}|[a-fA-F0-9]{6})$",
            message = "WebsiteUpdate hexColor must be type as #fff or #FFFFFF"
    )
    private String hexColor;

    @NotNull(message = "WebsiteUpdate must have userId")
    private UUID userId;

    @Size(max = 100, message = "WebsiteUpdate title length must be lower than 100 symbols")
    private String title;

    @Size(max = 255, message = "WebsiteUpdate description length must be lower than 255 symbols")
    private String description;

    @URL(message = "WebsiteUpdate url is invalid")
    private URI url;

}
