package br.com.gabriels.application.category.delete;

import br.com.gabriels.domain.category.CategoryGateway;
import br.com.gabriels.domain.category.CategoryID;

public class DefaultDeleteCategoryUseCase extends DeleteCategoryUseCase {

    private final CategoryGateway categoryGateway;

    public DefaultDeleteCategoryUseCase(CategoryGateway categoryGateway) {
        this.categoryGateway = categoryGateway;
    }

    @Override
    public void execute(CategoryID categoryID) {
        this.categoryGateway.deleteById(categoryID);
    }
}
