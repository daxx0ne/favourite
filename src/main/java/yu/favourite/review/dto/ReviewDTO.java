package yu.favourite.review.dto;

import lombok.*;
import yu.favourite.category.entity.Category;
import yu.favourite.review.entity.Review;
import yu.favourite.user.SiteUser;

import java.util.Set;


@Getter
@Setter
@ToString
@NoArgsConstructor
public class ReviewDTO {

    private Long id;
    private int category;
    private String title;
    private String author;
    private String content;
    private int rate;
    private String username;

    public Review toEntity() {
        return Review.builder()
                .id(id).author(author)
                .title(title).content(content).rate(rate)
                .build();
    }

    @Builder
    public ReviewDTO(Long id, int category, String author,
                     String title, String content, int rate, String username) {
        this.id = id;
        this.category = category;
        this.author = author;
        this.title = title;
        this.content = content;
        this.rate = rate;
        this.username = username;
    }

}
