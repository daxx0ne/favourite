package yu.favourite.review.controller;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import yu.favourite.review.dto.ReviewDTO;
import yu.favourite.review.entity.Review;
import yu.favourite.review.repository.ReviewRepository;
import yu.favourite.review.service.ReviewService;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@Controller
@RequestMapping("/review")
public class ReviewController {
    private final ReviewService reviewService;
    private final ReviewRepository reviewRepository;

    // 생성자
    public ReviewController(ReviewService reviewService, ReviewRepository reviewRepository) {
        this.reviewService = reviewService;
        this.reviewRepository = reviewRepository;
    }

    @GetMapping("/booklist")
    public String listBookReviews(@RequestParam(defaultValue = "0") int page,
                                  @RequestParam(defaultValue = "6") int size,
                                  Model model) {
        Page<ReviewDTO> reviewPage = reviewService.findReviewsByCategory(1, page, size);
        model.addAttribute("reviews", reviewPage.getContent());
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", reviewPage.getTotalPages());
        return "review/booklist";
    }

    @GetMapping("/movielist")
    public String listMovieReviews(@RequestParam(defaultValue = "0") int page,
                                   @RequestParam(defaultValue = "6") int size,
                                   Model model) {
        Page<ReviewDTO> reviewPage = reviewService.findReviewsByCategory(2, page, size);
        model.addAttribute("reviews", reviewPage.getContent());
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", reviewPage.getTotalPages());
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

    @PostMapping("/update/{id}")
    public String update(@PathVariable Long id, ReviewDTO reviewDTO, @RequestParam String password, RedirectAttributes redirectAttributes) {
        try {
            reviewService.updateReview(id, reviewDTO, password);
            redirectAttributes.addFlashAttribute("message", "리뷰가 성공적으로 수정되었습니다.");
            return "redirect:/";
        } catch (IllegalArgumentException e) {
            redirectAttributes.addFlashAttribute("error", "비밀번호가 일치하지 않습니다.");
            return "redirect:/review/update/{id}";
        }
    }

    @GetMapping("/delete/{id}")
    public String showDeleteForm(@PathVariable Long id, Model model) {
        ReviewDTO review = reviewService.findById(id);
        model.addAttribute("review", review);
        return "review/delete";
    }

    @PostMapping("/delete/{id}")
    public String delte(@PathVariable Long id, @RequestParam String password, RedirectAttributes redirectAttributes) {
        try {
            reviewService.deleteReview(id, password);
            redirectAttributes.addFlashAttribute("message", "리뷰가 성공적으로 삭제되었습니다.");
            return "redirect:/";
        } catch (IllegalArgumentException e) {
            redirectAttributes.addFlashAttribute("error", "비밀번호가 일치하지 않습니다.");
            return "redirect:/review/delete/{id}";
        }
    }

    @PostMapping("/recommend/{id}")
    public ResponseEntity<ReviewDTO> recommendReview(@PathVariable Long id) {
        try {
            ReviewDTO updatedReview = reviewService.incrementRecommend(id);
            return ResponseEntity.ok(updatedReview);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }
}
