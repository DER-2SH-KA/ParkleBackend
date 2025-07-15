package ru.d2k.parkle.dto;

import lombok.*;

import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@NoArgsConstructor
@AllArgsConstructor
@ToString(of = {"id", "hexValue"})
@Getter
public class HexColorDto {
    private static final Pattern PATTERN = Pattern.compile("^#([a-fA-F0-9]{3}|[a-fA-F0-9]{6})$");

    @Setter
    private Integer id;
    private String hexValue;

    /**
     * Set new HEX value.
     * @param hexValue new HEX value.
     * @throws IllegalArgumentException if null, blank or was not found pattern matches.
     * **/
    public void setHexValue(String hexValue) {
        if (Objects.nonNull(hexValue) && !hexValue.isBlank()) {
            Matcher matcher = PATTERN.matcher(hexValue);

            if (matcher.hasMatch()) {
                this.hexValue = hexValue.substring(matcher.start(), matcher.end());
            }
            else {
                throw new IllegalArgumentException("Wasn't founded matches by pattern for HEX.");
            }
        }
        else {
            throw new IllegalArgumentException("hexValue is null or blank.");
        }
    }

    @Override
    public final int hashCode() {
        return Objects.hash(this.id, this.hexValue);
    }

    @Override
    public final boolean equals(Object o) {
        if (this == o) return true;

        if (!(o instanceof HexColorDto oHexColorDto)) return false;

        return Objects.equals(this.id, oHexColorDto.id) &&
                Objects.equals(this.hexValue, oHexColorDto.hexValue);
    }
}
