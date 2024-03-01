package br.com.gabriels.infraestructure.application.category.create;

import br.com.gabriels.application.category.create.CreateCategoryCommand;
import br.com.gabriels.application.category.create.CreateCategoryUseCase;
import br.com.gabriels.domain.category.CategoryGateway;
import br.com.gabriels.infraestructure.CleanUpExtensions;
import br.com.gabriels.infraestructure.category.persistence.CategoryEntity;
import br.com.gabriels.infraestructure.category.persistence.CategoryRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.SpyBean;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;

@SpringBootTest
@ExtendWith(CleanUpExtensions.class)
public class CreateCategoryUseCaseIntregrationTests {

    @Autowired
    private CreateCategoryUseCase createCategoryUseCase;

    @Autowired
    private CategoryRepository categoryRepository;

    @SpyBean
    private CategoryGateway categoryGateway;

    @Test
    public void givenAValidCommand_whenCallsCreateCategory_shouldReturnCategoryId() {
        final var expectedName = "Filmes";
        final var expectedDescription = "A categoria mais assistida";
        final var expectedIsActive = true;

        assertThat(categoryRepository.count()).isEqualTo(0);

        final var aCommand = new CreateCategoryCommand(expectedName, expectedDescription, expectedIsActive);

        final var actualOutput = createCategoryUseCase.execute(aCommand).get();

        assertThat(actualOutput).isNotNull();
        assertThat(actualOutput.getCategoryID()).isNotNull();

        assertThat(categoryRepository.count()).isEqualTo(1);

        CategoryEntity actualCategory = categoryRepository.findById(actualOutput.getCategoryID()).get();

        assertThat(expectedName).isEqualTo(actualCategory.getName());
        assertThat(expectedDescription).isEqualTo(actualCategory.getDescription());
        assertThat(expectedIsActive).isEqualTo(actualCategory.isActive());
        assertThat(actualCategory.getCreatedAt()).isNotNull();
        assertThat(actualCategory.getUpdatedAt()).isNotNull();
        assertThat(actualCategory.getRemovedAt()).isNull();
    }

    @Test
    public void givenAInvalidName_whenCallsCreateCategory_thenShouldReturnDomainException() {
        final String expectedName = null;
        final var expectedDescription = "A categoria mais assistida";
        final var expectedIsActive = true;
        final var expectedErrorMessage = "Name should not be empty!";
        final var expectedErrorCount = 1;

        assertThat(categoryRepository.count()).isEqualTo(0);

        final var aCommand = new CreateCategoryCommand(expectedName, expectedDescription, expectedIsActive);

        final var notification = createCategoryUseCase.execute(aCommand).getLeft();

        assertThat(expectedErrorCount).isEqualTo(notification.getErrors().size());
        assertThat(expectedErrorMessage).isEqualTo(notification.getErrors().getFirst().message());

        assertThat(categoryRepository.count()).isEqualTo(0);

        Mockito.verify(categoryGateway, Mockito.never()).create(any());
    }

    @Test
    public void givenAValidCommandWithInactiveCategory_whenCallsCreateCategory_shouldReturnInactiveCategoryId() {
        final var expectedName = "Filmes";
        final var expectedDescription = "A categoria mais assistida";
        final var expectedIsActive = false;

        assertThat(categoryRepository.count()).isEqualTo(0);

        final var aCommand = new CreateCategoryCommand(expectedName, expectedDescription, expectedIsActive);

        final var actualOutput = createCategoryUseCase.execute(aCommand).get();

        assertThat(actualOutput).isNotNull();
        assertThat(actualOutput.getCategoryID()).isNotNull();

        assertThat(categoryRepository.count()).isEqualTo(1);

        final var actualCategory = categoryRepository.findById(actualOutput.getCategoryID()).get();

        assertThat(expectedName).isEqualTo(actualCategory.getName());
        assertThat(expectedDescription).isEqualTo(actualCategory.getDescription());
        assertThat(expectedIsActive).isEqualTo(actualCategory.isActive());
        assertThat(actualCategory.getCreatedAt()).isNotNull();
        assertThat(actualCategory.getUpdatedAt()).isNotNull();
        assertThat(actualCategory.getRemovedAt()).isNotNull();
    }

    @Test
    public void givenAValidCommand_whenGatewayThrowsRandomException_shouldReturnAException() {
        final var expectedName = "Filmes";
        final var expectedDescription = "A categoria mais assistida";
        final var expectedIsActive = true;
        final var expectedErrorCount = 1;
        final var expectedErrorMessage = "Gateway error";

        final var aCommand = new CreateCategoryCommand(expectedName, expectedDescription, expectedIsActive);

        doThrow(new IllegalStateException(expectedErrorMessage))
                .when(categoryGateway).create(any());

        final var notification = createCategoryUseCase.execute(aCommand).getLeft();

        assertThat(expectedErrorCount).isEqualTo(notification.getErrors().size());
        assertThat(expectedErrorMessage).isEqualTo(notification.getErrors().getFirst().message());
    }
}
