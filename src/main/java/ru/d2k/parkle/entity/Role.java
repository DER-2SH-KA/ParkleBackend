package ru.d2k.parkle.entity;

import jakarta.persistence.*;
import lombok.Getter;

import java.util.Objects;
import java.util.Optional;

@Entity
@Table(name = "role")
public class Role {

    @Getter
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", unique = true, nullable = false)
    private Integer id;

    @Getter
    @Column(
            name = "name",
            unique = true,
            nullable = false,
            columnDefinition = "varchar(32)"
    )
    private String name;

    @Getter
    @Column(
            name = "priority",
            nullable = false
    )
    private Integer priority;

    /**
     * Set new ID for {@code Role} entity.
     * @param newId new ID for {@code Role} entity.
     * @return this {@code Role}.
     * @throws IllegalArgumentException when {@code newId} is {@code null}
     * @author DER-2SH-KA
     * **/
    public Role setId(Optional<Integer> newId) throws IllegalArgumentException {
        if (newId.isPresent()) {
            this.id = newId.get();
        } else {
            throw new IllegalArgumentException("New ID for Role is NULL");
        }

        return this;
    }

    /**
     * Set new name for {@code Role} entity.
     * @param newName new name for {@code Role} entity.
     * @return this {@code Role}.
     * @throws IllegalArgumentException when {@code newName} is {@code null}
     * @author DER-2SH-KA
     * **/
    public Role setName(Optional<String> newName) throws IllegalArgumentException {
        if (newName.isPresent()) {
            this.name = newName.get();
        } else {
            throw new IllegalArgumentException("New Name for Role is NULL or Empty");
        }

        return this;
    }

    /**
     * Set new priority for {@code Role} entity.
     * @param newPriority new priority for {@code Role} entity.
     * @return this {@code Role}.
     * @throws IllegalArgumentException when {@code newPriority} is {@code null}
     * @author DER-2SH-KA
     * **/
    public Role setPriority(Optional<Integer> newPriority) throws IllegalArgumentException {
        if (newPriority.isPresent()) {
            if (newPriority.get() > 0) {
                this.priority = newPriority.get();
            }
            else {
                throw new IllegalArgumentException("New Priority for Role lower then 1");
            }
        } else {
            throw new IllegalArgumentException("New Priority for Role is NULL or Empty");
        }

        return this;
    }

    @Override
    public String toString() {
        return String.format("ID: %d%n", this.id) +
                String.format("NAME: %s%n", this.name) +
                String.format("PRIORITY: %d%n", this.priority);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (o == null || getClass() != o.getClass()) return false;

        Role oRole = (Role) o;

        return Objects.equals(this.id, oRole.id) &&
                Objects.equals(this.name, oRole.name) &&
                Objects.equals(this.priority, oRole.priority);

    }

    @Override
    public int hashCode() {
        return Objects.hash(
                this.id,
                this.name,
                this.priority
        );
    }
}
