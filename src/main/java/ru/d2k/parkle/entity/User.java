package ru.d2k.parkle.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;
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
    @GeneratedValue(generator = "uuid-v7-user-generator")
    @GenericGenerator(name = "uuid-v7-user-generator", type = Uuid7Generator.class)
    @Column(
            name = "id",
            nullable = false,
            updatable = false,
            columnDefinition = "UUID"
    )
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
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

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Website> websites = new ArrayList<>();

    public User(Role role, String login, String email, String password) {
        this.role = role;
        this.login = login;
        this.email = email;
        this.password = password;
    }

    User(UUID id, Role role, String login, String email, String password) {
        this.id = id;
        this.role = role;
        this.login = login;
        this.email = email;
        this.password = password;
    }
}
