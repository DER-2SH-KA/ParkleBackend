package ru.d2k.parkle.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.Objects;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@ToString(of = {"id", "hexValue"})
@Entity
@Table(name="hex_color")
public class HexColor {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Column(
            name = "hex_value",
            unique = true,
            nullable = false,
            length = 7
    )
    private String hexValue;

    public HexColor(String hexValue) {
        this.setHexValue(hexValue);
    }

    HexColor(Integer id, String hexValue) {
        this.id = id;
        this.setHexValue(hexValue);
    }

    /**
     * Set new HEX value for {@code HexColor} entity.
     * @param newHexValue new HEX value for {@code HexColor} entity.
     * @throws IllegalArgumentException when {@code newHexValue} is {@code null}
     * **/
    public void setHexValue(String newHexValue) throws IllegalArgumentException {
        if (Objects.nonNull(newHexValue) && !newHexValue.isBlank()) {
            this.hexValue = newHexValue;
        } else {
            throw new IllegalArgumentException("New HEX value for HexColor is NULL or Empty");
        }
    }

    @Override
    public final boolean equals(Object o) {
        if (this == o) return true;

        if (o == null || getClass() != o.getClass()) return false;

        HexColor oHexColor = (HexColor) o;

        return Objects.nonNull(this.id) &&
                Objects.nonNull(oHexColor.id) &&
                Objects.equals(this.id, oHexColor.id);

    }

    @Override
    public final int hashCode() {
        return this.id != null ? Objects.hashCode(this.id) : 31;
    }
}
