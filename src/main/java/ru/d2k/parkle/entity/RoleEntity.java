package ru.d2k.parkle.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import ru.d2k.parkle.utils.generator.UuidGeneratorUtil;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Entity
@Table(name = "roles")
@Getter
@Setter
@EqualsAndHashCode(of = "id")
@ToString(of = {"id", "name"})
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class RoleEntity {

    @Id
    @Column(name = "id", columnDefinition = "UUID")
    private UUID id;

    @Column(name = "name", unique = true, nullable = false, length = 32)
    private String name;

    @ManyToMany(mappedBy = "roles")
    private Set<UserEntity> users = new HashSet<>();

    private RoleEntity(String name) {
        this.id = UuidGeneratorUtil.generateNewUuidV7();
        this.name = name;
    }

    public RoleEntity(UUID id, String name) {
        this.id = id;
        this.name = name;
    }

    public static RoleEntity create(String name) { return new RoleEntity(name); }
}