package br.com.gabriels.application.retrieve.list;

import br.com.gabriels.application.category.retrieve.list.CategoryListOutput;
import br.com.gabriels.application.category.retrieve.list.DefaultListCategoryUseCase;
import br.com.gabriels.domain.category.*;
import br.com.gabriels.domain.pagination.Pagination;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;

@ExtendWith(MockitoExtension.class)
public class ListCategoryUseCaseTest {

    @InjectMocks
    private DefaultListCategoryUseCase defaultListCategoryUseCase;

    @Mock
    private CategoryGateway categoryGateway;

    @Test
    void should_return_list_of_categories() {
        final CategorySearchQuery searchQuery = new CategorySearchQuery(0, 2, "", "createdAt", "asc");

        final List<Category> categories = List.of(
                Category.getInstance("category 1", "descricao legal", true),
                Category.getInstance("category 2", "descricao legal", true),
                Category.getInstance("category 3", "descricao legal", true)
        );

        final Pagination<Category> expectedPagination = new Pagination<>(0, 2, 2, categories);

        Mockito.when(this.categoryGateway.findAll(searchQuery)).thenReturn(expectedPagination);
        final Pagination<CategoryListOutput> all = this.defaultListCategoryUseCase.execute(searchQuery);

        Assertions.assertThat(all.total()).isEqualTo(2);
        Assertions.assertThat(all.currentPage()).isEqualTo(0);
        Assertions.assertThat(all.perPage()).isEqualTo(2);
        Assertions.assertThat(all.items()).extracting(CategoryListOutput::getName).contains("category 1", "category 2");
    }

    @Test
    void should_return_an_empty_list() {
        final CategorySearchQuery searchQuery = new CategorySearchQuery(0, 2, "", "createdAt", "asc");

        final List<Category> categories = Collections.emptyList();

        final Pagination<Category> expectedPagination = new Pagination<>(0, 2, 2, categories);

        Mockito.when(this.categoryGateway.findAll(searchQuery)).thenReturn(expectedPagination);
        final Pagination<CategoryListOutput> all = this.defaultListCategoryUseCase.execute(searchQuery);

        Assertions.assertThat(all.total()).isEqualTo(2);
        Assertions.assertThat(all.currentPage()).isEqualTo(0);
        Assertions.assertThat(all.perPage()).isEqualTo(2);
        Assertions.assertThat(all.items()).hasSize(0);

    }

    @Test
    void should_throw_gateway_error() {
        final CategorySearchQuery searchQuery = new CategorySearchQuery(0, 2, "", "createdAt", "asc");


        Mockito.doThrow(new IllegalStateException("Gateway Error!")).when(this.categoryGateway).findAll(searchQuery);

        Assertions.assertThatThrownBy(() -> this.defaultListCategoryUseCase.execute(searchQuery))
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("Gateway Error!");
    }
}
