package yu.favourite.review.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import yu.favourite.category.entity.Category;
import yu.favourite.category.repository.CategoryRepository;
import yu.favourite.review.dto.ReviewDTO;
import yu.favourite.review.entity.Review;
import yu.favourite.review.repository.ReviewRepository;
import yu.favourite.user.SiteUser;
import yu.favourite.user.UserRepository;


@Service
public class ReviewService {

    private final ReviewRepository reviewRepository;
    private final CategoryRepository categoryRepository;
    private final UserRepository userRepository;

    public ReviewService(ReviewRepository reviewRepository, CategoryRepository categoryRepository, UserRepository userRepository) {
        this.reviewRepository = reviewRepository;
        this.categoryRepository = categoryRepository;
        this.userRepository = userRepository;
    }

    @Transactional
    public void saveReview(ReviewDTO reviewDTO) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentUsername = authentication.getName();
        SiteUser siteUser = userRepository.findByUsername(currentUsername)
                .orElseThrow(() -> new IllegalStateException("회원을 찾을 수 없습니다."));

        Review review = reviewDTO.toEntity();
        review.setSiteuser(siteUser);
        reviewRepository.save(review);
    }

    public ReviewDTO findById(Long id) {
        Review review = (Review) reviewRepository.findById(id).orElse(null);
        ReviewDTO build = ReviewDTO.builder()
                .id(review.getId())
                .author(review.getAuthor())
                .title(review.getTitle())
                .content(review.getContent())
                .rate(review.getRate())
                .build();
        return build;
    }

    @Transactional
    public void updateReview(Long id, ReviewDTO reviewDTO) {
        if (id == null) {
            throw new IllegalArgumentException("리뷰 ID가 null입니다.");
        }
        Review review = reviewRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("리뷰가 존재하지 않습니다: " + id));

        // 현재 로그인한 사용자 검증
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentUsername = authentication.getName();
        if (!review.getSiteuser().getUsername().equals(currentUsername)) {
            throw new AccessDeniedException("수정 권한이 없습니다.");
        }
        Category category = categoryRepository.findById(reviewDTO.getCategory())
                .orElseThrow(() -> new IllegalArgumentException("유효하지 않은 카테고리 ID: " + reviewDTO.getCategory()));

        review.setCategory(category); // 카테고리 설정
        review.setAuthor(reviewDTO.getAuthor());
        review.setAuthor(reviewDTO.getAuthor());
        review.setTitle(reviewDTO.getTitle());
        review.setContent(reviewDTO.getContent());
        review.setRate(reviewDTO.getRate());

        reviewRepository.save(review);
    }


    @Transactional(readOnly = true)
    public Page<ReviewDTO> findReviewsByCategory(int categoryId, int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "id"));
        Category category = categoryRepository.findById(categoryId).orElseThrow(
                () -> new IllegalArgumentException("Invalid category ID: " + categoryId)
        );
        Page<Review> reviews = reviewRepository.findByCategory(category, pageable);

        return reviews.map(this::convertToDTO);
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
                .username(review.getSiteuser().getUsername())
                .build();
    }

    @Transactional
    public void deleteReview(Long id) {
        Review review = reviewRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("리뷰가 존재하지 않습니다: " + id));


        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentUsername = authentication.getName();
        if (!review.getSiteuser().getUsername().equals(currentUsername)) {
            throw new AccessDeniedException("삭제 권한이 없습니다.");
        }

        reviewRepository.deleteById(id);
    }
}

