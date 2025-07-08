package ru.d2k.parkle.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@ToString(of = {"id", "name", "priority"})
@Entity
@Table(name = "roles")
public class Role {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

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
            cascade = { CascadeType.PERSIST, CascadeType.MERGE }
    )
    private Set<User> users = new HashSet<User>();

    public Role(String name, Integer priority) {
        this.setName(name);
        this.setPriority(priority);
    }

    Role(Integer id, String name, Integer priority) {
        this.id = id;
        this.setName(name);
        this.setPriority(priority);
    }

    /**
     * Set new name for {@code Role} entity.
     * @param newName new name for {@code Role} entity.
     * @throws IllegalArgumentException when {@code newName} is {@code null}
     * **/
    public void setName(String newName) throws IllegalArgumentException {
        if (Objects.nonNull(newName) && !newName.isBlank()) {
            this.name = newName;
        } else {
            throw new IllegalArgumentException("New Name for Role is NULL or Empty");
        }
    }

    /**
     * Set new priority for {@code Role} entity.
     * @param newPriority new priority for {@code Role} entity.
     * @throws IllegalArgumentException when {@code newPriority} is {@code null}
     * **/
    public void setPriority(Integer newPriority) throws IllegalArgumentException {
        if (Objects.nonNull(newPriority)) {
            if (newPriority > 0) {
                this.priority = newPriority;
            }
            else {
                throw new IllegalArgumentException("New Priority for Role lower then 1");
            }
        } else {
            throw new IllegalArgumentException("New Priority for Role is NULL");
        }
    }

    @Override
    public final boolean equals(Object o) {
        if (this == o) return true;

        if (!(o instanceof Role oRole)) return false;

        return Objects.nonNull(this.id) &&
                Objects.nonNull(oRole.id) &&
                Objects.equals(this.id, oRole.id);

    }

    @Override
    public final int hashCode() {
        return this.id != null ?
                Objects.hashCode(this.id) : 31;
    }
}
