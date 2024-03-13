package br.com.gabriels.infraestructure.category;

import br.com.gabriels.domain.category.*;
import br.com.gabriels.domain.pagination.SearchQuery;
import br.com.gabriels.domain.pagination.Pagination;
import br.com.gabriels.infraestructure.category.persistence.CategoryEntity;
import br.com.gabriels.infraestructure.category.persistence.CategoryRepository;
import br.com.gabriels.infraestructure.utils.SpecificationUtils;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.Optional;

import static br.com.gabriels.infraestructure.utils.SpecificationUtils.like;

@Service
public class DefaultCategoryGateway implements CategoryGateway {

    private final CategoryRepository categoryRepository;

    public DefaultCategoryGateway(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    @Override
    public Category create(Category category) {
        return save(category);
    }

    @Override
    public void deleteById(CategoryID categoryID) {
        final String idValue = categoryID.getValue();
        if (this.categoryRepository.existsById(idValue)) {
            this.categoryRepository.deleteById(idValue);
        }
    }

    @Override
    public Optional<Category> findById(CategoryID categoryID) {
        return this.categoryRepository.findById(categoryID.getValue()).map(CategoryEntity::toDomainModel);
    }

    @Override
    public Category update(Category category) {
        return save(category);
    }

    @Override
    public Pagination<Category> findAll(SearchQuery categorySearchQuery) {
        PageRequest pageRequest = PageRequest.of(categorySearchQuery.page(), categorySearchQuery.perPage(), Sort.by(Sort.Direction.fromString(categorySearchQuery.direction()), categorySearchQuery.sort()));

        Specification<CategoryEntity> specification = Optional.ofNullable(categorySearchQuery.terms()).filter(term -> !term.isBlank())
                .map(it -> SpecificationUtils.<CategoryEntity>like("name", it).or(like("description", it))).orElse(null);

        Page<CategoryEntity> pagedResult = this.categoryRepository.findAll(specification, pageRequest);

        return new Pagination<>(pagedResult.getNumber(), pagedResult.getSize(), pagedResult.getTotalElements(), pagedResult.map(CategoryEntity::toDomainModel).toList());
    }

    private Category save(Category category) {
        return this.categoryRepository.save(new CategoryEntity(category)).toDomainModel();
    }
}
