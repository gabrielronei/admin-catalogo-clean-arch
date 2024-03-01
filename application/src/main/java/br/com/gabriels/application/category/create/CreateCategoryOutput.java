package br.com.gabriels.application.category.create;

import br.com.gabriels.domain.category.Category;
import br.com.gabriels.domain.category.CategoryID;

public final class CreateCategoryOutput {

    private final String categoryID;

    public CreateCategoryOutput(final Category category) {
        this.categoryID = category.getId().getValue();
    }

    public String getCategoryID() {
        return categoryID;
    }
}
