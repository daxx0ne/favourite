package yu.favourite.category.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import yu.favourite.category.entity.Category;

import java.util.Optional;

public interface CategoryRepository extends JpaRepository<Category, Integer> {
    Optional<Object> findById(Long categoryId);
}

