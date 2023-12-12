package yu.favourite.category.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import yu.favourite.category.entity.Category;

public interface CategoryRepository extends JpaRepository<Category, Integer> {
}

