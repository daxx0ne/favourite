package yu.favourite.review.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import yu.favourite.member.entity.Member;

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

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member; // member 테이블의 유저네임을 가져오고 싶어서 참조

    @Builder
    public Review(Long id, Member member, int categoryId, String author, String title,
                  String content, int rate, int recommend) {
        this.id = id;
        this.member = member;
        this.categoryId = categoryId;
        this.author = author;
        this.title = title;
        this.content = content;
        this.rate = rate;
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

    public void setMember(Member member) {
        this.member = member;
    }
}
