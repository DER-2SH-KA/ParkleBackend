package ru.d2k.parkle.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public record ErrorResponseDto(

        @JsonProperty("messageForClient")
        String messageForClient,

        @JsonProperty("messageForDev")
        String messageForDev
) { }