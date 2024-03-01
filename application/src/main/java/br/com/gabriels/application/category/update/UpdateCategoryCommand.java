package br.com.gabriels.application.category.update;

import br.com.gabriels.domain.category.CategoryID;

public record UpdateCategoryCommand(String id, String name, String description, boolean isActive) {

    public CategoryID getCategoryId() {
        return CategoryID.from(id);
    }
}
