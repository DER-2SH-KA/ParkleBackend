package ru.d2k.parkle.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;
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
    @GeneratedValue(generator = "uuid-v7-hex-color-generator")
    @GenericGenerator(name = "uuid-v7-hex-color-generator", type = Uuid7Generator.class)
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

    public HexColor(String hexValue) {
        this.hexValue = hexValue;
    }

    HexColor(UUID id, String hexValue) {
        this.id = id;
        this.hexValue = hexValue;
    }
}
