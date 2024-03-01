package br.com.gabriels.infraestructure.application.category.update;

import br.com.gabriels.application.category.update.UpdateCategoryCommand;
import br.com.gabriels.application.category.update.UpdateCategoryUseCase;
import br.com.gabriels.domain.category.*;
import br.com.gabriels.domain.exceptions.DomainException;
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

import java.time.temporal.ChronoUnit;

import static org.assertj.core.api.Assertions.within;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.never;

@SpringBootTest
@ExtendWith(CleanUpExtensions.class)
public class UpdateCategoryUseCaseIntegrationTests {

    @Autowired
    private UpdateCategoryUseCase updateCategoryUseCase;

    @Autowired
    private CategoryRepository categoryRepository;

    @SpyBean
    private CategoryGateway categoryGateway;

    @Test
    public void givenAValidCommand_whenCallsUpdateCategory_shouldReturnCategoryId() {
        final var aCategory = new Category(CategoryID.unique(), "Film", null, true);

        categoryRepository.save(new CategoryEntity(aCategory));

        final var expectedName = "Filmes";
        final var expectedDescription = "A categoria mais assistida";
        final var expectedIsActive = true;
        final var expectedId = aCategory.getId();

        final var aCommand = new UpdateCategoryCommand(expectedId.getValue(), expectedName, expectedDescription, expectedIsActive);

        Assertions.assertThat(categoryRepository.count()).isEqualTo(1);

        final var actualOutput = updateCategoryUseCase.execute(aCommand).get();

        Assertions.assertThat(actualOutput).isNotNull();
        Assertions.assertThat(actualOutput.getCategoryID()).isNotNull();

        final var actualCategory =
                categoryRepository.findById(expectedId.getValue()).get();

        Assertions.assertThat(expectedName).isEqualTo(actualCategory.getName());
        Assertions.assertThat(expectedDescription).isEqualTo(actualCategory.getDescription());
        Assertions.assertThat(expectedIsActive).isEqualTo(actualCategory.isActive());
        Assertions.assertThat(aCategory.getCreatedAt()).isCloseTo(actualCategory.getCreatedAt(), within(1, ChronoUnit.SECONDS));
        Assertions.assertThat(aCategory.getUpdatedAt().isBefore(actualCategory.getUpdatedAt())).isTrue();
        Assertions.assertThat(actualCategory.getRemovedAt()).isNull();
    }

    @Test
    public void givenAInvalidName_whenCallsUpdateCategory_thenShouldReturnDomainException() {
        final var aCategory = new Category(CategoryID.unique(), "Film", null, true);

        categoryRepository.save(new CategoryEntity(aCategory));

        final String expectedName = null;
        final var expectedDescription = "A categoria mais assistida";
        final var expectedIsActive = true;
        final var expectedId = aCategory.getId();

        final var expectedErrorMessage = "Name should not be empty!";
        final var expectedErrorCount = 1;

        final var aCommand = new UpdateCategoryCommand(expectedId.getValue(), expectedName, expectedDescription, expectedIsActive);

        final var notification = updateCategoryUseCase.execute(aCommand).getLeft();

        Assertions.assertThat(expectedErrorCount).isEqualTo(notification.getErrors().size());
        Assertions.assertThat(expectedErrorMessage).isEqualTo(notification.getErrors().getFirst().message());

        Mockito.verify(categoryGateway, never()).update(any());
    }

    @Test
    public void givenAValidInactivateCommand_whenCallsUpdateCategory_shouldReturnInactiveCategoryId() {
        final var aCategory = new Category(CategoryID.unique(), "Film", null, true);

        categoryRepository.save(new CategoryEntity(aCategory));

        final var expectedName = "Filmes";
        final var expectedDescription = "A categoria mais assistida";
        final var expectedIsActive = false;
        final var expectedId = aCategory.getId();

        final var aCommand = new UpdateCategoryCommand(expectedId.getValue(), expectedName, expectedDescription, expectedIsActive);

        Assertions.assertThat(aCategory.isActive()).isTrue();
        Assertions.assertThat(aCategory.getRemovedAt()).isNull();

        final var actualOutput = updateCategoryUseCase.execute(aCommand).get();

        Assertions.assertThat(actualOutput).isNotNull();
        Assertions.assertThat(actualOutput.getCategoryID()).isNotNull();

        final var actualCategory = categoryRepository.findById(expectedId.getValue()).get();

        Assertions.assertThat(expectedName).isEqualTo(actualCategory.getName());
        Assertions.assertThat(expectedDescription).isEqualTo(actualCategory.getDescription());
        Assertions.assertThat(expectedIsActive).isEqualTo(actualCategory.isActive());
        Assertions.assertThat(aCategory.getCreatedAt()).isCloseTo(actualCategory.getCreatedAt(), within(1, ChronoUnit.SECONDS));
        Assertions.assertThat(aCategory.getUpdatedAt().isBefore(actualCategory.getUpdatedAt())).isTrue();
        Assertions.assertThat(actualCategory.getRemovedAt()).isNotNull();
    }

    @Test
    public void givenAValidCommand_whenGatewayThrowsRandomException_shouldReturnAException() {
        final var aCategory = new Category(CategoryID.unique(), "Film", null, true);

        categoryRepository.save(new CategoryEntity(aCategory));

        final var expectedName = "Filmes";
        final var expectedDescription = "A categoria mais assistida";
        final var expectedIsActive = true;
        final var expectedId = aCategory.getId();
        final var expectedErrorCount = 1;
        final var expectedErrorMessage = "Gateway error";

        final var aCommand = new UpdateCategoryCommand(expectedId.getValue(), expectedName, expectedDescription, expectedIsActive);

        doThrow(new IllegalStateException(expectedErrorMessage))
                .when(categoryGateway).update(any());

        final var notification = updateCategoryUseCase.execute(aCommand).getLeft();

        Assertions.assertThat(expectedErrorCount).isEqualTo(notification.getErrors().size());
        Assertions.assertThat(expectedErrorMessage).isEqualTo(notification.getErrors().getFirst().message());

        final var actualCategory = categoryRepository.findById(expectedId.getValue()).get();

        Assertions.assertThat(aCategory.getName()).isEqualTo(actualCategory.getName());
        Assertions.assertThat(aCategory.getDescription()).isEqualTo(actualCategory.getDescription());
        Assertions.assertThat(aCategory.isActive()).isEqualTo(actualCategory.isActive());
        Assertions.assertThat(aCategory.getCreatedAt()).isCloseTo(actualCategory.getCreatedAt(), within(1, ChronoUnit.MILLIS));
        Assertions.assertThat(aCategory.getUpdatedAt()).isCloseTo(actualCategory.getUpdatedAt(), within(1, ChronoUnit.MILLIS));
        Assertions.assertThat(aCategory.getRemovedAt()).isEqualTo(actualCategory.getRemovedAt());
    }

    @Test
    public void givenACommandWithInvalidID_whenCallsUpdateCategory_shouldReturnNotFoundException() {
        final var expectedName = "Filmes";
        final var expectedDescription = "A categoria mais assistida";
        final var expectedIsActive = false;
        final var expectedId = "123";
        final var expectedErrorMessage = "Category with id 123 not found";

        final var aCommand = new UpdateCategoryCommand(expectedId, expectedName, expectedDescription, expectedIsActive);

        Assertions.assertThatCode(() -> updateCategoryUseCase.execute(aCommand))
                .isInstanceOf(DomainException.class)
                .hasMessageContaining(expectedErrorMessage);
    }
}
