package yu.favourite.review.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import yu.favourite.category.entity.Category;
import yu.favourite.review.entity.Review;

import java.util.Calendar;

public interface ReviewRepository extends JpaRepository<Review, Long> {
    Page<Review> findByCategory(Category category, Pageable pageable);
}

