package ru.d2k.parkle.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import ru.d2k.parkle.utils.generator.UuidGeneratorUtil;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "website_tags")
@Getter
@Setter
@ToString
@EqualsAndHashCode(of = {"id"})
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class WebsiteTagEntity {

    @Id
    @Column(name = "id", columnDefinition = "UUID")
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private UserEntity user;

    @Column(name = "title", nullable = false, length = 50)
    private String title;

    @Column(name = "hex", nullable = false, length = 7)
    private String hex;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "website_and_tags", joinColumns = @JoinColumn(name = "website_tag_id"),
            inverseJoinColumns = @JoinColumn(name = "website_id"))
    private List<WebsiteEntity> websites = new ArrayList<>();

    private WebsiteTagEntity(UserEntity user, String title, String hex, List<WebsiteEntity> websites) {
        this.id = UuidGeneratorUtil.generateNewUuidV7();
        this.user = user;
        this.title = title;
        this.hex = hex;
        this.websites = websites;
    }

    WebsiteTagEntity(UUID id, UserEntity user, String title, String hex, List<WebsiteEntity> websites) {
        this.id = id;
        this.user = user;
        this.title = title;
        this.hex = hex;
        this.websites = websites;
    }

    public static WebsiteTagEntity create(UserEntity user, String title, String hex) {
        return new WebsiteTagEntity(user, title, hex, new ArrayList<>());
    }
}
