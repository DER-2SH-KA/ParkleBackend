package ru.d2k.parkle.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;
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
    @GeneratedValue(generator = "uuid-v7-generator")
    @GenericGenerator(name = "uuid-v7-role-generator", type = Uuid7Generator.class)
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

    @OneToMany(mappedBy = "role")
    private Set<User> users = new HashSet<User>();

    public Role(String name, Integer priority) {
        this.name = name;
        this.priority = priority;
    }

    Role(UUID id, String name, Integer priority) {
        this.id = id;
        this.name = name;
        this.priority = priority;
    }
}
