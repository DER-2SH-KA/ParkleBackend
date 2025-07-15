package ru.d2k.parkle.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class HexColorDto {
    private Integer id;

    @NotBlank(message = "HEX value cannot be null or blank.")
    @Pattern(
            regexp = "^#([a-fA-F0-9]{3}|[a-fA-F0-9]{6})$",
            message = "Invalid HEX color format. Must be # followed by 3 or 6 hex characters."
    )
    private String hexValue;
}
