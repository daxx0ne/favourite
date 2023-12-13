package yu.favourite.review.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import yu.favourite.category.repository.CategoryRepository;
import yu.favourite.review.dto.ReviewDTO;
import yu.favourite.review.entity.Review;
import yu.favourite.review.repository.ReviewRepository;

import java.util.Objects;

@Service
public class ReviewService {

    private final ReviewRepository reviewRepository;

    public ReviewService(ReviewRepository reviewRepository, CategoryRepository categoryRepository) {
        this.reviewRepository = reviewRepository;
    }

    @Transactional
    public void saveReview(ReviewDTO reviewDTO) {
        Review review = reviewDTO.toEntity();
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
    public void updateReview(Long id, ReviewDTO reviewDTO, String inputPassword) {
        Review review = reviewRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("리뷰가 존재하지 않습니다: " + id));

        int inputPasswordInt = Integer.parseInt(inputPassword);
        if (review.getPassword() != inputPasswordInt) {
            throw new IllegalArgumentException("비밀번호가 일치하지 않습니다.");
        }

        review.setAuthor(reviewDTO.getAuthor());
        review.setTitle(reviewDTO.getTitle());
        review.setContent(reviewDTO.getContent());
        review.setRate(reviewDTO.getRate());
        review.setRecommend(reviewDTO.getRecommend());
        reviewRepository.save(review);
    }


    @Transactional(readOnly = true)
    public Page<ReviewDTO> findReviewsByCategory(int categoryId, int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "id"));
        Page<Review> reviews = reviewRepository.findByCategoryId(categoryId, pageable);

        return reviews.map(review -> new ReviewDTO(
                review.getId(),
                review.getCategoryId(),
                review.getAuthor(),
                review.getTitle(),
                review.getContent(),
                review.getRate(),
                review.getPassword(),
                review.getRecommend()
        ));
    }

    @Transactional
    public ReviewDTO incrementRecommend(Long id) {
        Review review = reviewRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("리뷰가 존재하지 않습니다."));
        review.setRecommend(review.getRecommend() + 1);
        review = reviewRepository.save(review);
        return convertToDTO(review);
    }

    private ReviewDTO convertToDTO(Review review) {
        if (review == null) {
            return null;
        }
        return ReviewDTO.builder()
                .id(review.getId())
                .author(review.getAuthor())
                .title(review.getTitle())
                .content(review.getContent())
                .rate(review.getRate())
                .password(review.getPassword())
                .recommend(review.getRecommend())
                .build();
    }

    public void deleteReview(Long id, String password) {
        Review review = reviewRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("리뷰가 존재하지 않습니다: " + id));

        int inputPasswordInt = Integer.parseInt(password);
        if (review.getPassword() != inputPasswordInt) {
            throw new IllegalArgumentException("비밀번호가 일치하지 않습니다.");
        }

        reviewRepository.deleteById(id);
    }
}

