package ru.d2k.parkle.utils;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import lombok.extern.slf4j.Slf4j;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Objects;

@Slf4j
@Converter(autoApply = true)
public class UriConverter implements AttributeConverter<URI, String> {
    @Override
    public String convertToDatabaseColumn(URI uri) {
        return Objects.nonNull(uri) ? uri.toString() : null;
    }

    @Override
    public URI convertToEntityAttribute(String uriAsString) throws IllegalStateException {
        if (Objects.nonNull(uriAsString) && !uriAsString.isBlank()) {
            try {
                return new URI(uriAsString);
            }
            catch (URISyntaxException use) {
                log.error("Exception Convert from String to URL. String: {}", uriAsString, use);
                throw new IllegalStateException("Invalid URI format in database: " + uriAsString, use);
            }
        }

        return null;
    }
}
