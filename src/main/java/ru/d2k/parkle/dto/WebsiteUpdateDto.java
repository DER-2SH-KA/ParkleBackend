package ru.d2k.parkle.dto;

import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.net.URI;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class WebsiteUpdateDto {

    @Pattern(
            regexp = "^#([a-fA-F0-9]{3}|[a-fA-F0-9]{6})$",
            message = "WebsiteUpdate hexColor must be type as #fff or #FFFFFF"
    )
    private String hexColor;

    @Size(max = 100, message = "WebsiteUpdate title length must be lower than 100 symbols")
    private String title;

    @Size(max = 255, message = "WebsiteUpdate title length must be lower than 255 symbols")
    private String description;

    private URI url;

}
