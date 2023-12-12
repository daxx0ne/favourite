package yu.favourite.review.controller;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import yu.favourite.review.dto.ReviewDTO;
import yu.favourite.review.service.ReviewService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/review")
public class ReviewController {
    private final ReviewService reviewService;

    // 생성자
    public ReviewController(ReviewService reviewService) {
        this.reviewService = reviewService;
    }

    @GetMapping("/booklist")
    public String listBookReviews(Model model) {
        List<ReviewDTO> reviews = reviewService.findReviewsByCategory(1); // 책 카테고리는 1로 가정
        model.addAttribute("reviews", reviews);
        return "review/booklist";
    }

    @GetMapping("/movielist")
    public String listMovieReviews(Model model) {
        List<ReviewDTO> reviews = reviewService.findReviewsByCategory(2); // 영화 카테고리는 2로 가정
        model.addAttribute("reviews", reviews);
        return "review/movielist";
    }


    // 리뷰 작성 폼 페이지
    @GetMapping("/write")
    public String showWriteForm() {
        return "review/write";
    }

    // 리뷰 작성
    @PostMapping("/write")
    public String write(ReviewDTO reviewDTO, RedirectAttributes redirectAttributes, HttpServletRequest request) {
        reviewService.saveReview(reviewDTO);
        redirectAttributes.addFlashAttribute("message", "리뷰가 성공적으로 작성되었습니다.");

        // 홈 페이지로 리다이렉트
        return "redirect:/";
    }


    // 리뷰 수정 폼 페이지
    @GetMapping("/update/{id}")
    public String showUpdateForm(@PathVariable Long id, Model model) {
        ReviewDTO review = reviewService.findById(id);
        model.addAttribute("review", review);
        return "review/update";
    }

    // 리뷰 수정
    @PostMapping("/update/{id}")
    public String update(@PathVariable Long id, ReviewDTO reviewDTO, @RequestParam int password, RedirectAttributes redirectAttributes, HttpServletRequest request) {
        try {
            reviewService.updateReview(id, reviewDTO, password);
            redirectAttributes.addFlashAttribute("message", "리뷰가 성공적으로 수정되었습니다.");

            // 홈 페이지로 리다이렉트
            return "redirect:/";
        } catch (IllegalArgumentException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/review/update/{id}";
        }
    }


    // 리뷰 삭제
    @GetMapping("/delete/{id}")
    public String delete(@PathVariable Long id, @RequestParam int password, RedirectAttributes redirectAttributes) {
        try {
            reviewService.deleteReview(id, password);
            redirectAttributes.addFlashAttribute("message", "리뷰가 성공적으로 삭제되었습니다.");
            return "redirect:/";
        } catch (IllegalArgumentException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/";
        }
    }

    @PostMapping("/recommend/{reviewId}")
    public ResponseEntity<?> recommendReview(@PathVariable Long reviewId) {
        try {
            // 해당 리뷰의 추천 수를 데이터베이스에서 가져옵니다.
            int recommendCount = reviewService.incrementRecommendCount(reviewId);

            // 업데이트된 추천 수를 JSON 형태로 응답합니다.
            Map<String, Integer> response = new HashMap<>();
            response.put("recommendCount", recommendCount);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            // 예외 처리
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

}
