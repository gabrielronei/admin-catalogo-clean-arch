package br.com.gabriels.domain.category;

import br.com.gabriels.domain.validation.Error;
import br.com.gabriels.domain.validation.*;

public class CategoryValidator extends Validator {

    private final Category category;

    public CategoryValidator(final Category category, ValidationHandler handler) {
        super(handler);
        this.category = category;
    }

    @Override
    public void validate() {
        checkName();
    }

    private void checkName() {
        final var categoryName = this.category.getName();
        if (categoryName == null || categoryName.isEmpty()) {
            this.validationHandler().append(new Error("Name should not be empty!"));
            return;
        }

        var trimmedName = categoryName.trim();
        if (trimmedName.length() < 3 || trimmedName.length() > 255) {
            this.validationHandler().append(new Error("Name should be between 3 and 255 characters!"));
        }
    }
}
