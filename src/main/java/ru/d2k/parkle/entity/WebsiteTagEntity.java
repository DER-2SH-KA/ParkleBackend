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
import java.util.Set;
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
    private UUID userId;

    @Column(name = "title", nullable = false, length = 50)
    private String title;

    @Column(name = "hex", nullable = false, length = 7)
    private String hex;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "website_and_tags", joinColumns = @JoinColumn(name = "website_tag_id"),
            inverseJoinColumns = @JoinColumn(name = "website_id"))
    private Set<WebsiteEntity> websites;

    private WebsiteTagEntity(UUID userId, String title, String hex, Set<WebsiteEntity> websites) {
        this.id = UuidGeneratorUtil.generateNewUuidV7();
        this.userId = userId;
        this.title = title;
        this.hex = hex;
        this.websites = websites;
    }

    WebsiteTagEntity(UUID id, UUID userId, String title, String hex, Set<WebsiteEntity> websites) {
        this.id = id;
        this.userId = userId;
        this.title = title;
        this.hex = hex;
        this.websites = websites;
    }

    public static WebsiteTagEntity create(UUID userId, String title, String hex, Set<WebsiteEntity> websites) {
        return new WebsiteTagEntity(userId, title, hex, websites);
    }
}
