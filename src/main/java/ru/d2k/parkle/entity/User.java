package ru.d2k.parkle.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.*;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@ToString(of = {"id", "roleId", "login", "email"})
@Entity
@Table(
        name = "users",
        indexes = {
                @Index(columnList = "login"),
                @Index(columnList = "role_id")
        }
)
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "role_id", nullable = false)
    private Role role;

    @Column(
            name = "login",
            unique = true,
            nullable = false,
            length = 100
    )
    private String login;

    @Column(
            name = "email",
            unique = true,
            length = 320
    )
    private String email;

    @Column(
            name = "password",
            length = 60
    )
    private String password;

    @OneToMany(
            mappedBy = "user",
            cascade = {CascadeType.PERSIST, CascadeType.MERGE}
    )
    private List<Website> websites = new ArrayList<>();

    public User(Role role, String login, String email, String password) {
        this.setRole(role);
        this.setLogin(login);
        this.setEmail(email);
        this.setPassword(password);
    }

    User(Long id, Role role, String login, String email, String password) {
        this.id = id;
        this.setRole(role);
        this.setLogin(login);
        this.setEmail(email);
        this.setPassword(password);
    }

    /**
     * Set new {@code Role} for {@code User} entity.
     * @param newRole new {@code Role} for {@code User} entity.
     * @throws IllegalArgumentException when {@code newRoleId} is {@code null}
     * **/
    public void setRole(Role newRole) throws IllegalArgumentException {
        if (Objects.nonNull(newRole)) {
            this.role = newRole;
        }
        else {
            throw new IllegalArgumentException("Role for User is NULL");
        }
    }

    /**
     * Set new login for {@code User} entity.
     * @param newLogin new login for {@code User} entity.
     * @throws IllegalArgumentException when {@code newLogin} is {@code null}
     * **/
    public void setLogin(String newLogin) throws IllegalArgumentException {
        if (Objects.nonNull(newLogin) && !newLogin.isBlank()) {
            this.login = newLogin;
        }
        else {
            throw new IllegalArgumentException("New login is NULL or Blank");
        }
    }

    /**
     * Set new email for {@code User} entity.
     * @param newEmail new email for {@code User} entity.
     * @throws IllegalArgumentException when {@code newEmail} is {@code null}
     * **/
    public void setEmail(String newEmail) throws IllegalArgumentException {
        if (Objects.nonNull(newEmail) && !newEmail.isBlank()) {
            this.email = newEmail;
        }
        else {
            throw new IllegalArgumentException("New email is NULL or Blank");
        }
    }

    /**
     * Set new password for {@code User} entity.
     * @param newPassword new password for {@code User} entity.
     * @throws IllegalArgumentException when {@code newPassword} is {@code null}
     * **/
    public void setPassword(String newPassword) throws IllegalArgumentException {
        if (Objects.nonNull(newPassword) && !newPassword.isBlank()) {
            this.password = newPassword;
        }
        else {
            throw new IllegalArgumentException("New password is NULL or Blank");
        }
    }

    @Override
    public final boolean equals(Object o) {
        if (o == null) return false;

        if (this == o) return true;

        if (!(o instanceof User oUser)) return false;

        return Objects.nonNull(this.id) &&
                Objects.nonNull(oUser.id) &&
                Objects.equals(this.id, oUser.id);
    }

    @Override
    public final int hashCode() {
        return this.id != null ?
                Objects.hashCode(this.id) : 31;
    }
}
