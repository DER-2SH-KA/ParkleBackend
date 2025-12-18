package ru.d2k.parkle.dto;

import org.springframework.http.HttpStatus;

public record ErrorResponseDto(
        String messageForClient,
        String messageForDev
) { }
