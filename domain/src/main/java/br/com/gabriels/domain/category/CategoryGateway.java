package br.com.gabriels.domain.category;

import br.com.gabriels.domain.pagination.Pagination;
import br.com.gabriels.domain.pagination.SearchQuery;

import java.util.List;
import java.util.Optional;

public interface CategoryGateway {

    Category create(Category category);
    void deleteById(CategoryID categoryID);
    Optional<Category> findById(CategoryID categoryID);
    Category update(Category category);
    Pagination<Category> findAll(SearchQuery categorySearchQuery);
    List<CategoryID> existsByIds(Iterable<CategoryID> ids);
}
