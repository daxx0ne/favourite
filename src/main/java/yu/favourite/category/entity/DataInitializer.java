package yu.favourite.category.entity;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import yu.favourite.category.repository.CategoryRepository;

@Component
public class DataInitializer implements CommandLineRunner {

    private final CategoryRepository categoryRepository;

    public DataInitializer(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    @Override
    public void run(String... args) throws Exception {
        Category bookCategory = new Category(1, "book");
        Category movieCategory = new Category(2, "movie");

        // 이미 데이터베이스에 해당 id로 데이터가 존재하는지 확인
        if (!categoryRepository.existsById(1)) {
            categoryRepository.save(bookCategory);
        }

        if (!categoryRepository.existsById(2)) {
            categoryRepository.save(movieCategory);
        }
    }

}
