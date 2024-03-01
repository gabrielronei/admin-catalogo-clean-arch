package br.com.gabriels.infraestructure.category;

import br.com.gabriels.domain.category.Category;
import br.com.gabriels.domain.category.CategorySearchQuery;
import br.com.gabriels.domain.pagination.Pagination;
import br.com.gabriels.infraestructure.CleanUpExtensions;
import br.com.gabriels.infraestructure.category.persistence.CategoryEntity;
import br.com.gabriels.infraestructure.category.persistence.CategoryRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.within;

@DataJpaTest
@ExtendWith(CleanUpExtensions.class)
//@ComponentScan(includeFilters = {
//        @ComponentScan.Filter(type = FilterType.REGEX, pattern = "DefaultCategoryGateway")
//}) Só  pra lemrbar depois
class DefaultCategoryGatewayTest {

    @Autowired
    private CategoryRepository categoryRepository;

    private DefaultCategoryGateway defaultCategoryGateway;

    @BeforeEach
    void setUp() {
        this.defaultCategoryGateway = new DefaultCategoryGateway(this.categoryRepository);
    }

    @Test
    void should_persist_the_new_category() {
        assertThat(this.categoryRepository.count()).isEqualTo(0);

        Category category = Category.getInstance("Categoria Nova", "Descricao legal", true);

        Category persistedCategory = this.defaultCategoryGateway.create(category);

        assertThat(this.categoryRepository.count()).isEqualTo(1);

        assertThat(category.getId().getValue()).isEqualTo(persistedCategory.getId().getValue());
        assertThat(category.getName()).isEqualTo(persistedCategory.getName());
        assertThat(category.getDescription()).isEqualTo(persistedCategory.getDescription());
        assertThat(category.getCreatedAt()).isCloseTo(persistedCategory.getCreatedAt(), within(100, ChronoUnit.MILLIS));
        assertThat(category.getUpdatedAt()).isCloseTo(persistedCategory.getUpdatedAt(), within(100, ChronoUnit.MILLIS));
        assertThat(category.getRemovedAt()).isEqualTo(persistedCategory.getRemovedAt());
    }

    @Test
    void should_update_the_category() {
        Category category = Category.getInstance("Categoria Nova", "Descricao legal", true);
        this.defaultCategoryGateway.create(category);

        assertThat(this.categoryRepository.count()).isEqualTo(1);

        final String categoriaAtualizada = "Categoria Atualizada";
        final String descricaoAtualizada = "Descrição atualizada";

        category.update(categoriaAtualizada, descricaoAtualizada, true);
        Category updatedCategory = this.defaultCategoryGateway.update(category);

        assertThat(this.categoryRepository.count()).isEqualTo(1);

        assertThat(updatedCategory.getId().getValue()).isEqualTo(category.getId().getValue());
        assertThat(updatedCategory.getName()).isEqualTo(categoriaAtualizada);
        assertThat(updatedCategory.getDescription()).isEqualTo(descricaoAtualizada);
        assertThat(category.getCreatedAt()).isCloseTo(updatedCategory.getCreatedAt(), within(400, ChronoUnit.MILLIS));
        assertThat(updatedCategory.getUpdatedAt()).isNotEqualTo(category.getUpdatedAt());
    }

    @Test
    void should_delete_the_category() {
        Category category = Category.getInstance("Categoria Nova", "Descricao legal", true);
        this.defaultCategoryGateway.create(category);

        assertThat(this.categoryRepository.count()).isEqualTo(1);

        this.defaultCategoryGateway.deleteById(category.getId());

        assertThat(this.categoryRepository.count()).isEqualTo(0);

        this.defaultCategoryGateway.deleteById(category.getId());

        assertThat(this.categoryRepository.count()).isEqualTo(0);
    }

    @Test
    void should_findById_the_category() {
        Category newCategory = Category.getInstance("Categoria Nova", "Descricao legal", true);
        Optional<Category> possibleCategory = this.defaultCategoryGateway.findById(newCategory.getId());

        Assertions.assertThat(possibleCategory).isEmpty();

        this.defaultCategoryGateway.create(newCategory);

        possibleCategory = this.defaultCategoryGateway.findById(newCategory.getId());
        Assertions.assertThat(possibleCategory).isPresent();

        Category foundCategory = possibleCategory.get();
        Assertions.assertThat(foundCategory.getId()).isEqualTo(newCategory.getId());
        Assertions.assertThat(foundCategory.getName()).isEqualTo(newCategory.getName());
        Assertions.assertThat(foundCategory.getDescription()).isEqualTo(newCategory.getDescription());
    }

    @Test
    void should_findAll_the_categories() {
        Category documentary = Category.getInstance("Documentario", "Descricao de uma categoria de documentario", true);
        Category horror = Category.getInstance("Terror", "terrores horripilantes", true);
        Category comedy = Category.getInstance("Comedia", "hahaha comedia", true);

        Assertions.assertThat(this.categoryRepository.count()).isEqualTo(0);

        List<CategoryEntity> entities = List.of(new CategoryEntity(documentary), new CategoryEntity(horror), new CategoryEntity(comedy));
        this.categoryRepository.saveAll(entities);

        Assertions.assertThat(this.categoryRepository.count()).isEqualTo(entities.size());

        Pagination<Category> all = this.defaultCategoryGateway.findAll(new CategorySearchQuery(0, entities.size(), "", "name", "asc"));

        Assertions.assertThat(all.items()).extracting(Category::getName).containsExactly(comedy.getName(), documentary.getName(), horror.getName());
    }

    @Test
    void should_return_nothing_on_findAll() {
        Pagination<Category> empty = this.defaultCategoryGateway.findAll(new CategorySearchQuery(0, 1, "", "name", "asc"));

        Assertions.assertThat(empty.items()).isEmpty();
    }

    @Test
    void should_return_result_based_on_search_terms_on_findAll() {
        Category documentary = Category.getInstance("Documentario", "Descricao de uma categoria de documentario", true);
        Category horror = Category.getInstance("Terror", "terrores horripilantes", true);
        Category comedy = Category.getInstance("Comedia", "hahaha comedia", true);

        Assertions.assertThat(this.categoryRepository.count()).isEqualTo(0);

        List<CategoryEntity> entities = List.of(new CategoryEntity(documentary), new CategoryEntity(horror), new CategoryEntity(comedy));
        this.categoryRepository.saveAll(entities);

        Assertions.assertThat(this.categoryRepository.count()).isEqualTo(entities.size());

        Pagination<Category> all = this.defaultCategoryGateway.findAll(new CategorySearchQuery(0, entities.size(), "hah", "name", "asc"));

        Assertions.assertThat(all.items()).hasSize(1);
        Assertions.assertThat(all.items()).extracting(Category::getDescription).containsExactly(comedy.getDescription());
    }

    @Test
    void should_return_all_categories_ordered_by_description_desc_on_findAll() {
        Category documentary = Category.getInstance("Documentario", "Descricao de uma categoria de documentario", true);
        Category horror = Category.getInstance("Terror", "terrores horripilantes", true);
        Category comedy = Category.getInstance("Comedia", "hahaha comedia", true);

        Assertions.assertThat(this.categoryRepository.count()).isEqualTo(0);

        List<CategoryEntity> entities = List.of(new CategoryEntity(documentary), new CategoryEntity(horror), new CategoryEntity(comedy));
        this.categoryRepository.saveAll(entities);

        Assertions.assertThat(this.categoryRepository.count()).isEqualTo(entities.size());

        Pagination<Category> all = this.defaultCategoryGateway.findAll(new CategorySearchQuery(0, entities.size(), "", "description", "DESC"));

        Assertions.assertThat(all.items()).hasSize(entities.size());
        Assertions.assertThat(all.items()).extracting(Category::getDescription).containsExactly(horror.getDescription(), comedy.getDescription(), documentary.getDescription());
    }
}