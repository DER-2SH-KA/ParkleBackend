package ru.d2k.parkle.entity;

import jakarta.persistence.*;
import lombok.Getter;

import java.util.Objects;
import java.util.Optional;

@Entity
@Table(name="hex_color")
public class HexColor {
    @Getter
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", unique = true, nullable = false)
    private Integer id;

    @Getter
    @Column(
            name = "hex_value",
            unique = true,
            nullable = false,
            columnDefinition = "varchar(7)"
    )
    private String hexValue;

    /**
     * Set new ID for {@code HexColor} entity.
     * @param newId new ID for {@code HexColor} entity.
     * @return this {@code HexColor}.
     * @throws IllegalArgumentException when {@code newId} is {@code null}
     * @author DER-2SH-KA
     * **/
    public HexColor setId(Optional<Integer> newId) throws IllegalArgumentException {
        if (newId.isPresent()) {
            this.id = newId.get();
        } else {
            throw new IllegalArgumentException("New ID for HexColor is NULL");
        }

        return this;
    }

    /**
     * Set new name for {@code HexColor} entity.
     * @param newHexValue new name for {@code HexColor} entity.
     * @return this {@code HexColor}.
     * @throws IllegalArgumentException when {@code newHexValue} is {@code null}
     * @author DER-2SH-KA
     * **/
    public HexColor setHexValue(Optional<String> newHexValue) throws IllegalArgumentException {
        if (newHexValue.isPresent()) {
            this.hexValue = newHexValue.get();
        } else {
            throw new IllegalArgumentException("New Name for HexColor is NULL or Empty");
        }

        return this;
    }

    @Override
    public String toString() {
        return String.format("ID: %d%n", this.id) +
                String.format("HEX VALUE: %s%n", this.hexValue);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (o == null || getClass() != o.getClass()) return false;

        HexColor oHexColor = (HexColor) o;

        return Objects.equals(this.id, oHexColor.id) &&
                Objects.equals(this.hexValue, oHexColor.hexValue);

    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id, this.hexValue);
    }
}
