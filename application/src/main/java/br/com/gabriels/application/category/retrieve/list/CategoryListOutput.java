package br.com.gabriels.application.category.retrieve.list;

import br.com.gabriels.domain.category.Category;

public final class CategoryListOutput {

    private final String name;
    private final String description;

    public CategoryListOutput(Category category) {
        this.name = category.getName();
        this.description = category.getDescription();
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }
}
