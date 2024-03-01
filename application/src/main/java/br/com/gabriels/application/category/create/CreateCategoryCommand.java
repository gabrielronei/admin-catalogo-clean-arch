package br.com.gabriels.application.category.create;

import br.com.gabriels.domain.category.Category;

public record CreateCategoryCommand(String name,
                                    String description,
                                    boolean isActive) {


    public Category buildCategory() {
        return Category.getInstance(this.name, this.description, isActive);
    }
}
