package br.com.gabriels.application.category.update;

import br.com.gabriels.application.category.update.*;
import br.com.gabriels.domain.category.*;
import br.com.gabriels.domain.exceptions.DomainException;
import br.com.gabriels.domain.validation.handler.Notification;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Objects;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.AdditionalAnswers.returnsFirstArg;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UpdateCategoryUseCaseTest {

    @Mock
    private CategoryGateway categoryGateway;
    @InjectMocks
    private DefaultUpdateCategoryUseCase defaultUpdateCategoryUseCase;

    @Test
    void should_update_an_existing_category() {
        final Category category = Category.getInstance("XPTO", "descricaum com typo", true);

        final CategoryID id = category.getId();

        final String terror = "Terror";
        final String expectedDescription = "descrição sem typo";

        final UpdateCategoryCommand updateCategoryCommand = new UpdateCategoryCommand(id.getValue(), terror, expectedDescription, true);

        when(this.categoryGateway.findById(id)).thenReturn(Optional.of(category.clone()));

        when(this.categoryGateway.update(any())).thenAnswer(returnsFirstArg());

        final UpdateCategoryOutput output = defaultUpdateCategoryUseCase.execute(updateCategoryCommand).get();

        assertThat(output).isNotNull();
        assertThat(output.getCategoryID()).isNotNull();

        Mockito.verify(categoryGateway, times(1)).findById(eq(id));


        Mockito.verify(categoryGateway, Mockito.times(1)).update(Mockito.argThat(updatedCategory -> {
            return Objects.equals(terror, updatedCategory.getName()) &&
                    Objects.isNull(updatedCategory.getRemovedAt()) &&
                    Objects.equals(expectedDescription, updatedCategory.getDescription()) &&
                    category.getCreatedAt().isBefore(updatedCategory.getUpdatedAt()) &&
                    category.getUpdatedAt().isBefore(updatedCategory.getUpdatedAt());
        }));
    }

    @ParameterizedTest
    @NullAndEmptySource
    void should_not_update_an_invalid_category(String name) {
        final Category category = Category.getInstance("XPTO", "descricaum com typo", true);
        when(this.categoryGateway.findById(any())).thenReturn(Optional.of(category.clone()));

        final var command = new UpdateCategoryCommand(category.getId().getValue(), name, "description", true);


        Notification left = defaultUpdateCategoryUseCase.execute(command).getLeft();

        assertThat(left.getErrors()).hasSize(1);
        assertThat(left.getErrors().stream().findFirst().get().message()).isEqualTo("Name should not be empty!");

        Mockito.verify(categoryGateway, never()).update(any());
    }

    @Test
    void should_update_to_an_inactive_category() {
        final Category category = Category.getInstance("XPTO", "descricaum com typo", true);
        final String expectedName = "name";

        final var command = new UpdateCategoryCommand(category.getId().getValue(), expectedName, "description", false);

        when(this.categoryGateway.findById(any())).thenReturn(Optional.of(category.clone()));
        when(this.categoryGateway.update(any())).thenAnswer(returnsFirstArg());
        final var output = defaultUpdateCategoryUseCase.execute(command).get();

        assertThat(output).isNotNull();
        assertThat(output.getCategoryID()).isNotNull();

        Mockito.verify(categoryGateway, times(1)).update(any());
    }

    @Test
    void should_throw_exception_when_gateway_is_offline() {
        final Category category = Category.getInstance("XPTO", "descricaum com typo", true);

        final CategoryID id = category.getId();

        final String terror = "Terror";
        final String expectedDescription = "descrição sem typo";

        final UpdateCategoryCommand updateCategoryCommand = new UpdateCategoryCommand(id.getValue(), terror, expectedDescription, true);

        when(this.categoryGateway.findById(id)).thenReturn(Optional.empty());

        Assertions.assertThatThrownBy(() -> defaultUpdateCategoryUseCase.execute(updateCategoryCommand).get())
                .isInstanceOf(DomainException.class).hasMessageContaining("Category with id %s not found".formatted(updateCategoryCommand.getCategoryId().getValue()));

        Mockito.verify(categoryGateway, never()).update(any());
    }
}