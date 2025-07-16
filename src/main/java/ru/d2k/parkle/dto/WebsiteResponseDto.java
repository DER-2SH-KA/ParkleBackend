package ru.d2k.parkle.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class WebsiteResponseDto {
    private UUID id;
    private UUID userId;
    private String hexColor;
    private String title;
    private String description;
    private String url;
}
