package ru.d2k.parkle.dto;

public record ErrorResponseDto(
        String messageForClient,
        String messageForDev
) { }
