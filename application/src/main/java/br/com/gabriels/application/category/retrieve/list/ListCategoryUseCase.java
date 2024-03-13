package br.com.gabriels.application.category.retrieve.list;

import br.com.gabriels.application.UseCase;
import br.com.gabriels.domain.pagination.SearchQuery;
import br.com.gabriels.domain.pagination.Pagination;

public abstract class ListCategoryUseCase extends UseCase<SearchQuery, Pagination<CategoryListOutput>> {
}
