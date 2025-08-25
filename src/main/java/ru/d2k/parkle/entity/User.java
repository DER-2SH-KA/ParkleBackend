package ru.d2k.parkle.entity;

import jakarta.persistence.*;
import lombok.*;
import ru.d2k.parkle.utils.generator.Uuid7Generator;

import java.util.*;

/** Entity for user. **/
@Entity
@Table(
        name = "users",
        indexes = { @Index(columnList = "role_id") }
)

@Getter
@Setter
@ToString(of = {"id", "role", "login", "email"})
@EqualsAndHashCode(of = {"id"})
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class User {
    @Id
    @Column(
            name = "id",
            nullable = false,
            updatable = false,
            columnDefinition = "UUID"
    )
    private UUID id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "role_id", nullable = false)
    private Role role;

    @Column(
            name = "login",
            unique = true,
            nullable = false,
            length = 100
    )
    private String login;

    @Column(name = "email", unique = true, length = 320)
    private String email;

    @Column(name = "password", length = 60)
    private String password;

    @OneToMany(
            mappedBy = "user",
            fetch = FetchType.LAZY,
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private List<Website> websites = new ArrayList<>();

    private User(Role role, String login, String email, String password) {
        this.id = Uuid7Generator.generateNewUUID();
        this.role = role;
        this.login = login;
        this.email = email;
        this.password = password;
    }

    /**
     * Constructor only for tests.
     * @param id ID.
     * @param role {@link Role} role.
     * @param login login.
     * @param email email.
     * @param password hashed by BCrypt password.
     * **/
    User(UUID id, Role role, String login, String email, String password) {
        this.id = id;
        this.role = role;
        this.login = login;
        this.email = email;
        this.password = password;
    }

    /**
     * Fabric method for create new {@link User} with generated ID by UUID generator.
     * @param role {@link Role} role.
     * @param login login.
     * @param email email.
     * @param password hashed by BCrypt password.
     * @return Created {@link User} object.
     * **/
    public static User create(Role role, String login, String email, String password) {
        return new User(role, login, email, password);
    }
}
