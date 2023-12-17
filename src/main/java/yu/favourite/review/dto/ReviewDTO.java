package yu.favourite.review.dto;

import lombok.*;
import yu.favourite.review.entity.Review;


@Getter
@Setter
@ToString
@NoArgsConstructor
public class ReviewDTO {

    private Long id;
    private int categoryId;
    private String categoryName;
    private String title;
    private String author; // 작품의 감독명 or 저자명
    private String content;
    private int rate;
    private int password;
    private int recommend;
    private String username; // member 테이블의 username

    public Review toEntity() {
        return Review.builder()
                .id(id).categoryId(categoryId).author(author)
                .title(title).content(content).rate(rate)
                .recommend(recommend).build();
    }

    @Builder
    public ReviewDTO(Long id, int categoryId, String author,
                     String title, String content, int rate, int recommend, String username) {
        this.id = id;
        this.categoryId = categoryId;
        this.author = author;
        this.title = title;
        this.content = content;
        this.rate = rate;
        this.recommend = recommend;
        this.username = username;
    }
}
