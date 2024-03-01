package br.com.gabriels.application.retrieve.get;

import br.com.gabriels.application.category.retrieve.get.CategoryOutput;
import br.com.gabriels.application.category.retrieve.get.DefaultGetCategoryByIdUseCase;
import br.com.gabriels.domain.category.Category;
import br.com.gabriels.domain.category.CategoryGateway;
import br.com.gabriels.domain.exceptions.DomainException;
import br.com.gabriels.domain.exceptions.NotFoundException;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

@ExtendWith(MockitoExtension.class)
public class GetCategoryByIdUseCaseTest {

    @InjectMocks
    private DefaultGetCategoryByIdUseCase defaultGetCategoryByIdUseCase;

    @Mock
    private CategoryGateway categoryGateway;


    @Test
    void should_return_category_if_exists() {
        Category category = Category.getInstance("Categoria", "descrição legal", true);

        Mockito.when(categoryGateway.findById(category.getId())).thenReturn(Optional.of(category.clone()));

        final CategoryOutput foundCategory = defaultGetCategoryByIdUseCase.execute(category.getId());

        Assertions.assertThat(foundCategory).isNotNull();
    }

    @Test
    void should_return_not_found_if_category_non_exists() {
        Category category = Category.getInstance("Categoria", "descrição legal", true);

        Mockito.when(categoryGateway.findById(category.getId())).thenReturn(Optional.empty());

        Assertions.assertThatCode(() -> defaultGetCategoryByIdUseCase.execute(category.getId()))
                .isInstanceOf(NotFoundException.class)
                .hasMessageContaining("Category with ID %s not found!".formatted(category.getId().getValue()));
    }

    @Test
    void should_return_gateway_error() {
        Category category = Category.getInstance("Categoria", "descrição legal", true);

        Mockito.doThrow(new IllegalStateException("Gateway Error!")).when(this.categoryGateway).findById(category.getId());

        Assertions.assertThatThrownBy(() -> defaultGetCategoryByIdUseCase.execute(category.getId()))
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("Gateway Error!");
    }
}
