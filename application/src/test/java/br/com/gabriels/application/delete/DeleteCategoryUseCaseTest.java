package br.com.gabriels.application.delete;

import br.com.gabriels.application.category.delete.DefaultDeleteCategoryUseCase;
import br.com.gabriels.domain.category.*;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class DeleteCategoryUseCaseTest {

    @InjectMocks
    private DefaultDeleteCategoryUseCase defaultDeleteCategoryUseCase;

    @Mock
    private CategoryGateway categoryGateway;


    @Test
    void should_delete_an_existing_category() {
        final Category category = Category.getInstance("XPTO", "descricaum com typo", true);
        CategoryID categoryId = category.getId();

        doNothing().when(this.categoryGateway).deleteById(categoryId);

        Assertions.assertThatCode(() -> defaultDeleteCategoryUseCase.execute(categoryId)).doesNotThrowAnyException();

        verify(this.categoryGateway, times(1)).deleteById(categoryId);
    }

    @Test
    void should_do_nothing_when_category_does_not_exist() {
        final CategoryID categoryID = CategoryID.from("123");

        doNothing().when(this.categoryGateway).deleteById(categoryID);

        Assertions.assertThatCode(() -> defaultDeleteCategoryUseCase.execute(categoryID)).doesNotThrowAnyException();

        verify(this.categoryGateway, times(1)).deleteById(categoryID);
    }

    @Test
    void should_throw_gateway_error_and_not_delete_category() {
        final CategoryID categoryID = CategoryID.from("123");

        doThrow(new IllegalStateException("Gateway Error")).when(this.categoryGateway).deleteById(categoryID);

        Assertions.assertThatThrownBy(() -> defaultDeleteCategoryUseCase.execute(categoryID))
                .isInstanceOf(IllegalStateException.class);

        verify(this.categoryGateway, times(1)).deleteById(categoryID);
    }
}
