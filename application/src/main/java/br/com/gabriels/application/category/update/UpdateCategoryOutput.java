package br.com.gabriels.application.category.update;

import br.com.gabriels.domain.category.Category;
import br.com.gabriels.domain.category.CategoryID;

public class UpdateCategoryOutput {

    private final String categoryID;

    public UpdateCategoryOutput(final Category category) {
        this.categoryID = category.getId().getValue();
    }

    public String getCategoryID() {
        return categoryID;
    }
}
