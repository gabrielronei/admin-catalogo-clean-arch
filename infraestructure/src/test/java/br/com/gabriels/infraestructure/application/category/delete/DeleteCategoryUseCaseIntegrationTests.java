package br.com.gabriels.infraestructure.application.category.delete;

import br.com.gabriels.application.category.delete.DeleteCategoryUseCase;
import br.com.gabriels.domain.category.*;
import br.com.gabriels.infraestructure.CleanUpExtensions;
import br.com.gabriels.infraestructure.category.persistence.CategoryEntity;
import br.com.gabriels.infraestructure.category.persistence.CategoryRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.SpyBean;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.times;

@SpringBootTest
@ExtendWith(CleanUpExtensions.class)
public class DeleteCategoryUseCaseIntegrationTests {

    @Autowired
    private DeleteCategoryUseCase deleteCategoryUseCase;

    @Autowired
    private CategoryRepository categoryRepository;

    @SpyBean
    private CategoryGateway categoryGateway;


    @Test
    public void givenAValidId_whenCallsDeleteCategory_shouldBeOK() {
        final var aCategory = new Category(CategoryID.unique(), "Filmes", "A categoria mais assistida", true);
        final var expectedId = aCategory.getId();

        categoryRepository.save(new CategoryEntity(aCategory));

        assertThat(categoryRepository.count()).isEqualTo(1);

        assertThatCode(() -> deleteCategoryUseCase.execute(expectedId)).doesNotThrowAnyException();

        assertThat(categoryRepository.count()).isEqualTo(0);
    }

    @Test
    public void givenAInvalidId_whenCallsDeleteCategory_shouldBeOK() {
        final var expectedId = CategoryID.from("123");

        Assertions.assertThat(categoryRepository.count()).isEqualTo(0);

        Assertions.assertThatCode(() -> deleteCategoryUseCase.execute(expectedId)).doesNotThrowAnyException();

        Assertions.assertThat(categoryRepository.count()).isEqualTo(0);
    }

    @Test
    public void givenAValidId_whenGatewayThrowsException_shouldReturnException() {
        final var aCategory = new Category(CategoryID.unique(),"Filmes", "A categoria mais assistida", true);
        final var expectedId = aCategory.getId();

        doThrow(new IllegalStateException("Gateway error"))
                .when(categoryGateway).deleteById(eq(expectedId));

        Assertions.assertThatCode(() -> deleteCategoryUseCase.execute(expectedId)).isInstanceOf(IllegalStateException.class);

        Mockito.verify(categoryGateway, times(1)).deleteById(eq(expectedId));
    }

}
