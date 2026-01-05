package ru.d2k.parkle.entity;

import jakarta.persistence.*;
import lombok.*;
import ru.d2k.parkle.utils.generator.Uuid7Generator;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

/** Entity for role. **/
@Entity
@Table(name = "roles")

@Getter
@Setter
@EqualsAndHashCode(of = "id")
@ToString(of = {"id", "name", "priority"})
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Role {
    @Id
    @Column(
            name = "id",
            nullable = false,
            updatable = false,
            columnDefinition = "UUID"
    )
    private UUID id;

    @Column(
            name = "name",
            unique = true,
            nullable = false,
            length = 32
    )
    private String name;

    @Column(
            name = "priority",
            nullable = false
    )
    private Integer priority;

    @OneToMany(
            mappedBy = "role",
            fetch = FetchType.LAZY
    )
    private Set<User> users = new HashSet<User>();

    private Role(String name, Integer priority) {
        this.id = Uuid7Generator.generateNewUUID();
        this.name = name;
        this.priority = priority;
    }

    /**
     * Constructor only for tests.
     * @param name name.
     * @param priority priority.
     * **/
    public Role(UUID id, String name, Integer priority) {
        this.id = id;
        this.name = name;
        this.priority = priority;
    }

    /**
     * Fabric method for create new {@link Role} with generated ID by UUID generator.
     * @param name name.
     * @param priority priority.
     * @return Created {@link Role} object.
     * **/
    public static Role create(String name, Integer priority) { return new Role(name, priority); }
}
