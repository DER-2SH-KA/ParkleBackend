package ru.d2k.parkle.dto;

import lombok.*;
import ru.d2k.parkle.entity.Role;

import java.util.Objects;

/** DTO of {@link Role} **/
@NoArgsConstructor
@ToString(of = {"id", "name", "priority"})
@Getter
public class RoleDto {
    private Integer id;
    private String name;
    private Integer priority;

    public RoleDto(Integer id, String name, Integer priority) {
        this.setId( id );
        this.setName( name );
        this.setPriority( priority );
    }

    /**
     * Set new ID for {@link RoleDto}.
     * @param id New ID.
     * **/
    public void setId(Integer id) {
        if (Objects.nonNull(id)) {
            this.id = id;
        }
        else {
            throw new IllegalArgumentException("RoleDto ID is null!");
        }
    }

    /**
     * Set new name for {@link RoleDto}.
     * @param name New name.
     * **/
    public void setName(String name) {
        if (Objects.nonNull(name) && !name.isBlank()) {
            this.name = name;
        }
        else {
            throw new IllegalArgumentException("RoleDto name is null or blank!");
        }
    }

    /**
     * Set new priority for {@link RoleDto}.
     * @param priority New priority.
     * **/
    public void setPriority(Integer priority) {
        if (Objects.nonNull(priority) && priority > 0) {
            this.priority = priority;
        }
        else {
            throw new IllegalArgumentException("RoleDto priority is null!");
        }
    }

    @Override
    public final boolean equals(Object o) {
        if (this == o) return true;

        if (!(o instanceof RoleDto oRoleDto)) return false;

        return Objects.equals(this.id, oRoleDto.id) &&
                Objects.equals(this.name, oRoleDto.name) &&
                Objects.equals(this.priority, oRoleDto.priority);
    }

    @Override
    public final int hashCode() {
        return Objects.hash(this.id, this.name, this.priority);
    }
}
