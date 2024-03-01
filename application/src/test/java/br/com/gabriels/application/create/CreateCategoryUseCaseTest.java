package br.com.gabriels.application.create;

import br.com.gabriels.application.category.create.*;
import br.com.gabriels.domain.category.CategoryGateway;
import br.com.gabriels.domain.validation.handler.Notification;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Objects;

import static org.mockito.AdditionalAnswers.returnsFirstArg;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CreateCategoryUseCaseTest {

    @Mock
    private CategoryGateway categoryGateway;
    @InjectMocks
    private DefaultCreateCategoryUseCase defaultCreateCategoryUseCase;

    @Test
    void should_create_a_new_category() {

        final String expectedName = "name";

        final var command = new CreateCategoryCommand(expectedName, "description", true);
        when(this.categoryGateway.create(any())).thenAnswer(returnsFirstArg());
        final CreateCategoryOutput output = defaultCreateCategoryUseCase.execute(command).get();

        Assertions.assertThat(output).isNotNull();
        Assertions.assertThat(output.getCategoryID()).isNotNull();

//        when(categoryGateway.create(any())).thenAnswer(returnsFirstArg()); Deixei aqui pra lembrar desse carinha no futuro

        Mockito.verify(categoryGateway, Mockito.times(1)).create(Mockito.argThat(arg -> {
            return Objects.equals(expectedName, arg.getName()) && Objects.isNull(arg.getRemovedAt());
        }));
    }

    @ParameterizedTest
    @NullAndEmptySource
    void should_not_create_an_invalid_category(String name) {
        final var command = new CreateCategoryCommand(name, "description", true);


        Notification left = defaultCreateCategoryUseCase.execute(command).getLeft();

        Assertions.assertThat(left.getErrors()).hasSize(1);
        Assertions.assertThat(left.getErrors().stream().findFirst().get().message()).isEqualTo("Name should not be empty!");

        Mockito.verify(categoryGateway, never()).create(any());
    }

    @Test
    void should_create_an_inactive_category() {

        final String expectedName = "name";

        final var command = new CreateCategoryCommand(expectedName, "description", false);
        when(this.categoryGateway.create(any())).thenAnswer(returnsFirstArg());
        final CreateCategoryOutput output = defaultCreateCategoryUseCase.execute(command).get();

        Assertions.assertThat(output).isNotNull();
        Assertions.assertThat(output.getCategoryID()).isNotNull();

        Mockito.verify(categoryGateway, Mockito.times(1)).create(Mockito.argThat(arg -> {
            return Objects.equals(expectedName, arg.getName()) && Objects.nonNull(arg.getRemovedAt());
        }));
    }

    @Test
    void should_throw_exception_when_gateway_is_offline() {
        final String expectedName = "name";

        final var command = new CreateCategoryCommand(expectedName, "description", true);

        when(categoryGateway.create(any())).thenThrow(new IllegalArgumentException("Erro no gateway bobo!"));

        Notification left = defaultCreateCategoryUseCase.execute(command).getLeft();

        Assertions.assertThat(left.getErrors()).hasSize(1);
        Assertions.assertThat(left.getErrors().stream().findFirst().get().message()).isEqualTo("Erro no gateway bobo!");
    }
}