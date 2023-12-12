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
    private String author;
    private String content;
    private int rate;
    private int password;
    private int recommend;

    public Review toEntity() {
        return Review.builder()
                .id(id).categoryId(categoryId).author(author)
                .title(title).content(content).rate(rate)
                .password(password).recommend(recommend).build();
    }

    @Builder
    public ReviewDTO(Long id, int categoryId, String categoryName, String author,
                     String title, String content, int rate, int password, int recommend) {
        this.id = id;
        this.categoryId = categoryId;
        this.categoryName = categoryName;
        this.author = author;
        this.title = title;
        this.content = content;
        this.rate = rate;
        this.password = password;
        this.recommend = recommend;
    }
}
