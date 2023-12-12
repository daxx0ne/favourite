package yu.favourite.category.dto;

import lombok.Builder;
import yu.favourite.category.entity.Category;


public class CategoryDTO {

    private int id;
    private String name;

    public Category toEntity() {
        Category build = Category.builder()
                .id(id).name(name).build();
        return build;
    }

    @Builder
    public CategoryDTO(int id, String name) {
        this.id = id;
        this.name = name;
    }
}
