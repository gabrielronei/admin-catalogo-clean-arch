package br.com.gabriels.infraestructure.application.category.retrieve.get;

import br.com.gabriels.application.category.retrieve.get.CategoryOutput;
import br.com.gabriels.application.category.retrieve.get.GetCategoryByIdUseCase;
import br.com.gabriels.domain.category.*;
import br.com.gabriels.domain.exceptions.DomainException;
import br.com.gabriels.domain.exceptions.NotFoundException;
import br.com.gabriels.infraestructure.CleanUpExtensions;
import br.com.gabriels.infraestructure.category.persistence.CategoryEntity;
import br.com.gabriels.infraestructure.category.persistence.CategoryRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.SpyBean;

import java.time.temporal.ChronoUnit;

import static org.assertj.core.api.Assertions.within;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doThrow;

@SpringBootTest
@ExtendWith(CleanUpExtensions.class)
public class GetCategoryUseCaseIntegrationTests {

    @Autowired
    private GetCategoryByIdUseCase getCategoryByIdUseCase;

    @Autowired
    private CategoryRepository categoryRepository;

    @SpyBean
    private CategoryGateway categoryGateway;

    @Test
    public void givenAValidId_whenCallsGetCategory_shouldReturnCategory() {
        final var expectedName = "Filmes";
        final var expectedDescription = "A categoria mais assistida";
        final var expectedIsActive = true;

        final var aCategory = new Category(CategoryID.unique(), expectedName, expectedDescription, expectedIsActive);

        final var expectedId = aCategory.getId();

        categoryRepository.save(new CategoryEntity(aCategory));

        CategoryOutput actualCategory = getCategoryByIdUseCase.execute(expectedId);

        Assertions.assertThat(actualCategory.getName()).isEqualTo(expectedName);
        Assertions.assertThat(actualCategory.getDescription()).isEqualTo(expectedDescription);
        Assertions.assertThat(actualCategory.getCreatedAt()).isCloseTo(aCategory.getCreatedAt(), within(100, ChronoUnit.SECONDS));
        Assertions.assertThat(actualCategory.getUpdatedAt()).isCloseTo(aCategory.getUpdatedAt(), within(100, ChronoUnit.SECONDS));
        Assertions.assertThat(actualCategory.getRemovedAt()).isEqualTo(aCategory.getRemovedAt());
    }

    @Test
    public void givenAInvalidId_whenCallsGetCategory_shouldReturnNotFound() {
        final var expectedErrorMessage = "Category with ID 123 not found!";
        final var expectedId = CategoryID.from("123");

        Assertions.assertThatCode(() -> getCategoryByIdUseCase.execute(expectedId))
                .isInstanceOf(NotFoundException.class)
                .hasMessageContaining(expectedErrorMessage);
    }

    @Test
    public void givenAValidId_whenGatewayThrowsException_shouldReturnException() {
        final var expectedErrorMessage = "Gateway error";
        final var expectedId = CategoryID.from("123");

        doThrow(new IllegalStateException(expectedErrorMessage))
                .when(categoryGateway).findById(eq(expectedId));

        Assertions.assertThatCode(() -> getCategoryByIdUseCase.execute(expectedId))
                .isInstanceOf(IllegalStateException.class).hasMessageContaining("Gateway error");
    }
}
