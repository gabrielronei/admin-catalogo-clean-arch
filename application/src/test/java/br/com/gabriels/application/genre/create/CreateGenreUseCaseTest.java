package br.com.gabriels.application.genre.create;

import br.com.gabriels.domain.category.*;
import br.com.gabriels.domain.exceptions.NotificationException;
import br.com.gabriels.domain.genre.GenreGateway;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.*;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.argThat;

@ExtendWith(MockitoExtension.class)
public class CreateGenreUseCaseTest {

    @InjectMocks
    private DefaultCreateGenreUseCase defaultCreateGenreUseCase;

    @Mock
    private CategoryGateway categoryGateway;

    @Mock
    private GenreGateway genreGateway;


    @Test
    void givenAValidCOmmand_whenCallsCreateGenre_shouldReturnGenreId() {
        final var expectedName = "Ação";
        final var expectedIsActive = true;
        final List<Category> expectedCategories = Collections.emptyList();

        CreateGenreCommand command = new CreateGenreCommand(expectedName, expectedIsActive, asString(expectedCategories));

        Mockito.when(genreGateway.create(Mockito.any())).thenAnswer(AdditionalAnswers.returnsFirstArg());

        final var actualOutput = defaultCreateGenreUseCase.execute(command);
        Assertions.assertThat(actualOutput).isNotNull();
        Assertions.assertThat(actualOutput.get().getGenreId()).isNotNull();


        Mockito.verify(genreGateway, Mockito.times(1)).create(argThat(aGenre -> Objects.equals(expectedName, aGenre.getName()) &&
                Objects.equals(expectedIsActive, aGenre.isActive()) &&
                Objects.equals(expectedCategories, aGenre.getCategories()) &&
                Objects.nonNull(aGenre.getId()) &&
                Objects.nonNull(aGenre.getCreatedAt()) &&
                Objects.nonNull(aGenre.getUpdatedAt()) &&
                Objects.isNull(aGenre.getRemovedAt())
        ));
    }

    @Test
    void givenAValidCommandWithCategories_whenCallsCreateGenre_shouldReturnGenreId() {
        final var expectedName = "Ação";
        final var expectedIsActive = true;
        final List<CategoryID> expectedCategories = List.of(CategoryID.from("123"), CategoryID.from("456"));

        CreateGenreCommand command = new CreateGenreCommand(expectedName, expectedIsActive, expectedCategories.stream().map(CategoryID::getValue).toList());

        Mockito.when(categoryGateway.existsByIds(any())).thenReturn(expectedCategories);
        Mockito.when(genreGateway.create(Mockito.any())).thenAnswer(AdditionalAnswers.returnsFirstArg());

        final var actualOutput = defaultCreateGenreUseCase.execute(command);

        Mockito.verify(categoryGateway, Mockito.times(1)).existsByIds(expectedCategories);

        Assertions.assertThat(actualOutput).isNotNull();
        Assertions.assertThat(actualOutput.get().getGenreId()).isNotNull();


        Mockito.verify(genreGateway, Mockito.times(1)).create(argThat(aGenre -> Objects.equals(expectedName, aGenre.getName()) &&
                Objects.equals(expectedIsActive, aGenre.isActive()) &&
                Objects.equals(expectedCategories, aGenre.getCategories()) &&
                Objects.nonNull(aGenre.getId()) &&
                Objects.nonNull(aGenre.getCreatedAt()) &&
                Objects.nonNull(aGenre.getUpdatedAt()) &&
                Objects.isNull(aGenre.getRemovedAt())
        ));
    }

    @ParameterizedTest
    @NullAndEmptySource
    void givenAInvalidEmptyName_whenCallsCreateGenre_shouldReturnDomainException(String name) {
        final var expectedName = name;
        final var expectedIsActive = true;
        final List<Category> expectedCategories = Collections.emptyList();

        CreateGenreCommand command = new CreateGenreCommand(expectedName, expectedIsActive, asString(expectedCategories));

        Assertions.assertThatCode(() -> defaultCreateGenreUseCase.execute(command))
                .isInstanceOf(NotificationException.class);
    }

    @Test
    void givenAValidCommandWithCategories_whenCallsCreateGenreSomeCategoriesDoesNotExists_shouldReturnGenreId() {
        final CategoryID movies = CategoryID.from("123");
        final CategoryID series = CategoryID.from("321");
        final CategoryID documentaries = CategoryID.from("456");

        final var expectedName = "Ação";
        final var expectedIsActive = true;
        final List<CategoryID> expectedCategories = List.of(movies, series, documentaries);

        CreateGenreCommand command = new CreateGenreCommand(expectedName, expectedIsActive, expectedCategories.stream().map(CategoryID::getValue).toList());

        Mockito.when(categoryGateway.existsByIds(any())).thenReturn(List.of(series));

        Assertions.assertThatCode(() -> defaultCreateGenreUseCase.execute(command))
                .isInstanceOf(NotificationException.class);
    }

    private List<String> asString(final List<Category> categories) {
        return categories.stream().map(it -> it.getId().getValue()).toList();
    }
}
