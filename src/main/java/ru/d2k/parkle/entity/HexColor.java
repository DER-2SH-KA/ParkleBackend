package ru.d2k.parkle.entity;

import jakarta.persistence.*;
import lombok.*;
import ru.d2k.parkle.utils.generator.Uuid7Generator;

import java.util.Set;
import java.util.HashSet;
import java.util.Objects;
import java.util.UUID;

/** Entity for HEX color. **/
@Entity
@Table(name="hex_colors")

@Getter
@Setter
@ToString(of = {"id", "hexValue"})
@EqualsAndHashCode(of = {"id"})
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class HexColor {
    @Id
    @Column(
            name = "id",
            nullable = false,
            updatable = false,
            columnDefinition = "UUID"
    )
    private UUID id;

    @Column(
            name = "hex_value",
            unique = true,
            nullable = false,
            length = 7
    )
    private String hexValue;

    @OneToMany(mappedBy = "hexColor")
    private Set<Website> websites = new HashSet<>();

    private HexColor(String hexValue) {
        this.id = Uuid7Generator.generateNewUUID();
        this.hexValue = hexValue;
    }

    HexColor(UUID id, String hexValue) {
        this.id = id;
        this.hexValue = hexValue;
    }

    /**
     * Fabric method for create new {@link HexColor} with generated ID by UUID generator.
     * @param hexValue HEX value.
     * @return Created {@link HexColor} object.
     * **/
    public static HexColor create(String hexValue) { return new HexColor(hexValue); }
}
