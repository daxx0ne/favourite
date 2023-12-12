package yu.favourite.review.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import yu.favourite.review.entity.Review;

import java.util.List;

public interface ReviewRepository extends JpaRepository<Review, Long> {
    List<Review> findByCategoryId(int categoryId);
}

