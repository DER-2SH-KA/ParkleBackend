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
            length = 50
    )
    private String login;

    @Column(name = "email", unique = true, nullable = false, length = 320)
    private String email;

    @Column(name = "password", nullable = false, length = 60)
    private String password;

    @Column(name = "is_bocked", nullable = false)
    private Boolean isBocked;

    @OneToMany(
            mappedBy = "user",
            fetch = FetchType.LAZY,
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private List<Website> websites = new ArrayList<>();

    private User(Role role, String login, String email, String password, boolean isBlocked) {
        this.id = Uuid7Generator.generateNewUUID();
        this.role = role;
        this.login = login;
        this.email = email;
        this.password = password;
        this.isBocked = isBlocked;
    }

    /**
     * Constructor only for tests ({@code isBlocked = false}.
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
        this.isBocked = false;
    }

    /**
     * Constructor only for tests.
     * @param id ID.
     * @param role {@link Role} role.
     * @param login login.
     * @param email email.
     * @param password hashed by BCrypt password.
     * @param isBlocked is user account blocked.
     * **/
    User(UUID id, Role role, String login, String email, String password, boolean isBlocked) {
        this.id = id;
        this.role = role;
        this.login = login;
        this.email = email;
        this.password = password;
        this.isBocked = isBlocked;
    }

    /**
     * Fabric method for create new {@link User} with generated ID by UUID generator {@code isBlocked = false}.
     * @param role {@link Role} role.
     * @param login login.
     * @param email email.
     * @param password hashed by BCrypt password.
     * @return Created {@link User} object.
     * **/
    public static User create(Role role, String login, String email, String password) {
        return new User(role, login, email, password, false);
    }

    /**
     * Fabric method for create new {@link User} with generated ID by UUID generator.
     * @param role {@link Role} role.
     * @param login login.
     * @param email email.
     * @param password hashed by BCrypt password.
     * @param isBocked is user account blocked.
     * @return Created {@link User} object.
     * **/
    public static User create(Role role, String login, String email, String password, boolean isBocked) {
        return new User(role, login, email, password, isBocked);
    }
}
