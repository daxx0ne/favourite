package yu.favourite.review.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import yu.favourite.category.entity.Category;
import yu.favourite.user.entity.SiteUser;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EntityListeners(AuditingEntityListener.class)
public class Review {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category;

    @Column(length = 20, nullable = false)
    private String title;

    @Column(length = 10, nullable = false)
    private String author;

    @Column(columnDefinition = "TEXT", length = 100, nullable = false)
    private String content;

    @Column(nullable = false)
    private int rate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "site_user_id")
    private SiteUser siteuser;

    @Builder
    public Review(Long id, Category category, String author, String title,
                  String content, int rate, SiteUser siteuser) {
        this.id = id;
        this.category = category;
        this.author = author;
        this.title = title;
        this.content = content;
        this.rate = rate;
        this.siteuser = siteuser;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void setRate(int rate) {
        this.rate = rate;
    }

    public void setCategory(Category category) {
        this.category = category;
    }
    public void setSiteuser(SiteUser siteuser) {
        this.siteuser = siteuser;
    }
}
