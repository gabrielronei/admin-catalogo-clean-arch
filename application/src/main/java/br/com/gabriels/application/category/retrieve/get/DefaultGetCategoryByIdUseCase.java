package br.com.gabriels.application.category.retrieve.get;

import br.com.gabriels.domain.category.*;
import br.com.gabriels.domain.exceptions.DomainException;
import br.com.gabriels.domain.exceptions.NotFoundException;

public class DefaultGetCategoryByIdUseCase extends GetCategoryByIdUseCase {

    private final CategoryGateway categoryGateway;

    public DefaultGetCategoryByIdUseCase(CategoryGateway categoryGateway) {
        this.categoryGateway = categoryGateway;
    }

    @Override
    public CategoryOutput execute(CategoryID categoryID) {
        return this.categoryGateway.findById(categoryID)
                .map(CategoryOutput::new)
                .orElseThrow(() -> NotFoundException.with(Category.class, categoryID));
    }
}
