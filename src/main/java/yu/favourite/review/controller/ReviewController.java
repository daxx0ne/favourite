package yu.favourite.review.controller;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import yu.favourite.review.dto.ReviewDTO;

import yu.favourite.review.service.ReviewService;


@Controller
@RequestMapping("/usr/review")
public class ReviewController {
    private final ReviewService reviewService;

    // 생성자
    public ReviewController(ReviewService reviewService) {
        this.reviewService = reviewService;
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/booklist")
    public String listBookReviews(@RequestParam(defaultValue = "0") int page,
                                  @RequestParam(defaultValue = "4") int size,
                                  Model model) {
        Page<ReviewDTO> reviewPage = reviewService.findReviewsByCategory(1, page, size);
        model.addAttribute("reviews", reviewPage.getContent());
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", reviewPage.getTotalPages());
        return "usr/review/booklist";
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/movielist")
    public String listMovieReviews(@RequestParam(defaultValue = "0") int page,
                                   @RequestParam(defaultValue = "4") int size,
                                   Model model) {
        Page<ReviewDTO> reviewPage = reviewService.findReviewsByCategory(2, page, size);
        model.addAttribute("reviews", reviewPage.getContent());
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", reviewPage.getTotalPages());
        return "usr/review/movielist";
    }

    // 리뷰 작성 폼 페이지
    @PreAuthorize("isAuthenticated()")
    @GetMapping("/write")
    public String showWriteForm() {
        return "usr/review/write";
    }

    // 리뷰 작성
    @PreAuthorize("isAuthenticated()")
    @PostMapping("/write")
    public String write(ReviewDTO reviewDTO, RedirectAttributes redirectAttributes, HttpServletRequest request) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentUsername = authentication.getName(); // 현재 로그인한 사용자의 username
        reviewDTO.setUsername(currentUsername); // ReviewDTO에 username 설정

        reviewService.saveReview(reviewDTO); // 리뷰 저장
        redirectAttributes.addFlashAttribute("message", "리뷰가 성공적으로 작성되었습니다.");
        return "redirect:/";
    }


    // 리뷰 수정 폼 페이지
    @PreAuthorize("isAuthenticated()")
    @GetMapping("/update/{id}")
    public String showUpdateForm(@PathVariable Long id, Model model) {
        ReviewDTO review = reviewService.findById(id);
        model.addAttribute("review", review);
        return "usr/review/update";
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/update/{id}")
    public String update(@PathVariable Long id, ReviewDTO reviewDTO, RedirectAttributes redirectAttributes) {
        try {
            reviewService.updateReview(id, reviewDTO);
            redirectAttributes.addFlashAttribute("message", "리뷰가 성공적으로 수정되었습니다.");
        } catch (AccessDeniedException e) {
            redirectAttributes.addFlashAttribute("error", "수정 권한이 없습니다.");
        }
        return "redirect:/"; // or the appropriate redirect path
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/delete/{id}")
    public String showDeleteForm(@PathVariable Long id, Model model) {
        ReviewDTO review = reviewService.findById(id);
        model.addAttribute("review", review);
        return "usr/review/delete";
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/delete/{id}")
    public String delete(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            reviewService.deleteReview(id);
            redirectAttributes.addFlashAttribute("message", "리뷰가 성공적으로 삭제되었습니다.");
        } catch (AccessDeniedException e) {
            redirectAttributes.addFlashAttribute("error", "삭제 권한이 없습니다.");
        }
        return "redirect:/"; // or the appropriate redirect path
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/recommend/{id}")
    public String recommendReview(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            ReviewDTO updatedReview = reviewService.incrementRecommend(id);
            // 성공 메시지 추가 (선택 사항)
            redirectAttributes.addFlashAttribute("message", "리뷰가 추천되었습니다.");
        } catch (Exception e) {
            // 오류 메시지 추가 (선택 사항)
            redirectAttributes.addFlashAttribute("error", "추천 중 오류가 발생했습니다.");
        }
        return "redirect:/usr/review/booklist";
    }
}
