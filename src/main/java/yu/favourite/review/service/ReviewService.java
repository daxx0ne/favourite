package yu.favourite.review.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import yu.favourite.category.repository.CategoryRepository;
import yu.favourite.review.dto.ReviewDTO;
import yu.favourite.review.entity.Review;
import yu.favourite.review.repository.ReviewRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ReviewService {

    private final ReviewRepository reviewRepository;

    public ReviewService(ReviewRepository reviewRepository, CategoryRepository categoryRepository) {
        this.reviewRepository = reviewRepository;
    }

    @Transactional
    public void saveReview(ReviewDTO reviewDTO) {
        reviewRepository.save(reviewDTO.toEntity());
    }


    public void deleteReview(Long id, int inputPassword) {
        Review review = reviewRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("리뷰가 존재하지 않습니다: " + id));

        if (review.getPassword() != inputPassword) {
            throw new IllegalArgumentException("비밀번호가 일치하지 않습니다.");
        }

        reviewRepository.deleteById(id);
    }

    public ReviewDTO findById(Long id) {
        Review review = (Review) reviewRepository.findById(id).orElse(null);
        ReviewDTO build = ReviewDTO.builder()
                .id(review.getId())
                .author(review.getAuthor())
                .title(review.getTitle())
                .content(review.getContent())
                .rate(review.getRate())
                .password(review.getPassword())
                .recommend(review.getRecommend())
                .build();
        return build;
    }

    @Transactional
    public void updateReview(Long id, ReviewDTO reviewDTO, int inputPassword) {
        Review review = reviewRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("리뷰가 존재하지 않습니다: " + id));

        if (review.getPassword() != inputPassword) {
            throw new IllegalArgumentException("비밀번호가 일치하지 않습니다.");
        }
        review.setAuthor(reviewDTO.getAuthor());
        review.setTitle(reviewDTO.getTitle());
        review.setContent(reviewDTO.getContent());
        review.setRate(reviewDTO.getRate());
        review.setRecommend(reviewDTO.getRecommend());
        reviewRepository.save(review);
    }

    public List<ReviewDTO> findReviewsByCategory(int categoryId) {
        List<Review> reviews = reviewRepository.findByCategoryId(categoryId);
        return reviews.stream().map(review -> ReviewDTO.builder()
                        .id(review.getId())
                        .author(review.getAuthor())
                        .title(review.getTitle())
                        .content(review.getContent())
                        .rate(review.getRate())
                        .password(review.getPassword())
                        .recommend(review.getRecommend())
                        .build())
                .collect(Collectors.toList());
    }

    @Transactional
    public int incrementRecommendCount(Long reviewId) {
        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new IllegalArgumentException("리뷰를 찾을 수 없습니다."));

        // 현재 추천 수를 가져와서 1 증가시킵니다.
        int currentRecommendCount = review.getRecommend();
        int updatedRecommendCount = currentRecommendCount + 1;
        review.setRecommend(updatedRecommendCount);

        // 업데이트된 추천 수를 데이터베이스에 저장합니다.
        reviewRepository.save(review);

        return updatedRecommendCount;
    }
}

