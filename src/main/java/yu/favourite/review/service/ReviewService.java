package yu.favourite.review.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import yu.favourite.member.entity.Member;
import yu.favourite.member.repository.MemberRepository;
import yu.favourite.review.dto.ReviewDTO;
import yu.favourite.review.entity.Review;
import yu.favourite.review.repository.ReviewRepository;

@Service
public class ReviewService {

    private final ReviewRepository reviewRepository;
    private final MemberRepository memberRepository;

    public ReviewService(ReviewRepository reviewRepository, MemberRepository memberRepository) {
        this.reviewRepository = reviewRepository;
        this.memberRepository = memberRepository;
    }

    @Transactional
    public void saveReview(ReviewDTO reviewDTO) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentUsername = authentication.getName();
        Member member = memberRepository.findByUsername(currentUsername)  // MemberRepository를 사용하여 Member 검색
                .orElseThrow(() -> new IllegalStateException("회원을 찾을 수 없습니다."));

        Review review = reviewDTO.toEntity();
        review.setMember(member);  // Review 객체에 Member 설정
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
                .recommend(review.getRecommend())
                .build();
        return build;
    }

    @Transactional
    public void updateReview(Long id, ReviewDTO reviewDTO) {
        Review review = reviewRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("리뷰가 존재하지 않습니다: " + id));

        // 현재 로그인한 사용자 검증
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentUsername = authentication.getName();
        if (!review.getMember().getUsername().equals(currentUsername)) {
            throw new AccessDeniedException("수정 권한이 없습니다.");
        }

        // 리뷰 업데이트 로직
        review.setTitle(reviewDTO.getTitle());
        review.setContent(reviewDTO.getContent());
        review.setRate(reviewDTO.getRate());
        review.setAuthor(reviewDTO.getAuthor());

        reviewRepository.save(review);
    }


    @Transactional(readOnly = true)
    public Page<ReviewDTO> findReviewsByCategory(int categoryId, int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "id"));
        Page<Review> reviews = reviewRepository.findByCategoryId(categoryId, pageable);

        return reviews.map(review -> ReviewDTO.builder()
                .id(review.getId())
                .categoryId(review.getCategoryId())
                .author(review.getAuthor())
                .title(review.getTitle())
                .content(review.getContent())
                .rate(review.getRate())
                .recommend(review.getRecommend())
                .username(review.getMember().getUsername())
                .build());
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
                .recommend(review.getRecommend())
                .username(review.getMember().getUsername())
                .build();
    }

    @Transactional
    public void deleteReview(Long id) {
        Review review = reviewRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("리뷰가 존재하지 않습니다: " + id));

        // 현재 로그인한 사용자 검증
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentUsername = authentication.getName();
        if (!review.getMember().getUsername().equals(currentUsername)) {
            throw new AccessDeniedException("삭제 권한이 없습니다.");
        }

        // 리뷰 삭제
        reviewRepository.deleteById(id);
    }

}

