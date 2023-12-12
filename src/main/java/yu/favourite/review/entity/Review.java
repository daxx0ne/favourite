package yu.favourite.review.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EntityListeners(AuditingEntityListener.class)
public class Review {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private int categoryId;

    @Column(length = 20, nullable = false)
    private String title;

    @Column(length = 10, nullable = false)
    private String author;

    @Column(columnDefinition = "TEXT", length = 100, nullable = false)
    private String content;

    @Column(nullable = false)
    private int rate;

    @Column(nullable = false)
    private int password;

    private int recommend;

    @Builder
    public Review(Long id, int categoryId, String author, String title,
                  String content, int rate, int password, int recommend) {
        this.id = id;
        this.categoryId = categoryId;
        this.author = author;
        this.title = title;
        this.content = content;
        this.rate = rate;
        this.password = password;
        this.recommend = recommend;
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

    public void setRecommend(int recommend) {
        this.recommend = recommend;
    }
}
