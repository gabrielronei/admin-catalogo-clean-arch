package br.com.gabriels.application.category.retrieve.list;

import br.com.gabriels.domain.category.CategoryGateway;
import br.com.gabriels.domain.category.CategorySearchQuery;
import br.com.gabriels.domain.pagination.Pagination;

public class DefaultListCategoryUseCase extends ListCategoryUseCase {

    private final CategoryGateway categoryGateway;

    public DefaultListCategoryUseCase(CategoryGateway categoryGateway) {
        this.categoryGateway = categoryGateway;
    }

    @Override
    public Pagination<CategoryListOutput> execute(CategorySearchQuery categorySearchQuery) {
        return this.categoryGateway.findAll(categorySearchQuery).map(CategoryListOutput::new);
    }
}
