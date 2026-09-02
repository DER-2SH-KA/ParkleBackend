package ru.d2k.parkle.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import ru.d2k.parkle.utils.generator.UuidGeneratorUtil;

import java.util.UUID;

/** Entity for role. **/
@Entity
@Table(name = "roles")
@Getter
@Setter
@EqualsAndHashCode(of = "id")
@ToString(of = {"id", "name", "priority"})
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class RoleEntity {

    @Id
    @Column(name = "id", nullable = false, updatable = false, columnDefinition = "UUID")
    private UUID id;

    @Column(name = "name", unique = true, nullable = false, length = 32)
    private String name;

    @Column(name = "priority", nullable = false)
    private Integer priority;

    private RoleEntity(String name, Integer priority) {
        this.id = UuidGeneratorUtil.generateNewUuidV7();
        this.name = name;
        this.priority = priority;
    }

    /**
     * Constructor only for tests.
     * @param name name.
     * @param priority priority.
     * **/
    public RoleEntity(UUID id, String name, Integer priority) {
        this.id = id;
        this.name = name;
        this.priority = priority;
    }

    /**
     * Fabric method for create new {@link RoleEntity} with generated ID by UUID generator.
     * @param name name.
     * @param priority priority.
     * @return Created {@link RoleEntity} object.
     * **/
    public static RoleEntity create(String name, Integer priority) { return new RoleEntity(name, priority); }
}