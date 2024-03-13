package br.com.gabriels.domain.genre;

import br.com.gabriels.domain.category.CategoryID;
import br.com.gabriels.domain.exceptions.NotificationException;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.NullAndEmptySource;

import java.util.List;

public class GenreTest {

    @Test
    public void givenValidParams_whenCallsNewGenre_shouldInstantiateAGenre() {
        final var expectedName = "Ação";
        final var expectedActive = true;
        final var expectedCategories = 0;

        Genre genre = new Genre(expectedName, expectedActive);

        Assertions.assertThat(genre.getId()).isNotNull();
        Assertions.assertThat(genre.getName()).isEqualTo(expectedName);
        Assertions.assertThat(genre.isActive()).isEqualTo(expectedActive);
        Assertions.assertThat(genre.getCreatedAt()).isNotNull();
        Assertions.assertThat(genre.getUpdatedAt()).isNotNull();
        Assertions.assertThat(genre.getRemovedAt()).isNull();
        Assertions.assertThat(genre.getCategories()).hasSize(expectedCategories);
    }

    @ParameterizedTest
    @NullAndEmptySource
    public void givenInvalidNullAndEmptyName_whenCallsNewGenreAndValidate_shouldReturnAnError(String nullAndEmptyName) {
        Assertions.assertThatThrownBy(() -> new Genre(nullAndEmptyName, true))
                .isInstanceOf(NotificationException.class);
    }

    @ParameterizedTest
    @CsvSource({"12", "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa"})
    public void givenInvalidLengthName_whenCallsNewGenreAndValidate_shouldReturnAnError(String name) {
        Assertions.assertThatThrownBy(() -> new Genre(name, true))
                .isInstanceOf(NotificationException.class);
    }

    @Test
    public void givenAnActiveGenre_whenCallInactivate_shouldReceiveOk() {
        Genre genre = new Genre("Genre legal", true);

        Assertions.assertThat(genre.isActive()).isTrue();
        Assertions.assertThat(genre.getRemovedAt()).isNull();

        genre.inactivate();

        Assertions.assertThat(genre.isActive()).isFalse();
        Assertions.assertThat(genre.getRemovedAt()).isNotNull();
    }

    @Test
    public void givenAnInactiveGenre_whenCallActivate_shouldReceiveOk() {
        Genre genre = new Genre("Genre legal", false);

        Assertions.assertThat(genre.isActive()).isFalse();
        Assertions.assertThat(genre.getRemovedAt()).isNotNull();

        genre.activate();

        Assertions.assertThat(genre.isActive()).isTrue();
        Assertions.assertThat(genre.getRemovedAt()).isNull();
    }

    @Test
    public void givenAValidGenre_whenCallsUpdate_shouldReceiveGenreUpdated() {
        final var expectedName = "Ação";
        final var expectedActive = true;
        final var expectedCategories = List.of(CategoryID.from("123"));

        Genre genre = new Genre("acao", false);

        Assertions.assertThat(genre.getId()).isNotNull();
        Assertions.assertThat(genre.getName()).isEqualTo("acao");
        Assertions.assertThat(genre.isActive()).isFalse();
        Assertions.assertThat(genre.getCreatedAt()).isNotNull();
        Assertions.assertThat(genre.getUpdatedAt()).isNotNull();
        Assertions.assertThat(genre.getRemovedAt()).isNotNull();
        Assertions.assertThat(genre.getCategories()).hasSize(0);

        genre.update(expectedName, expectedActive, expectedCategories);

        Assertions.assertThat(genre.getId()).isNotNull();
        Assertions.assertThat(genre.getName()).isEqualTo(expectedName);
        Assertions.assertThat(genre.isActive()).isEqualTo(expectedActive);
        Assertions.assertThat(genre.getCreatedAt()).isNotNull();
        Assertions.assertThat(genre.getUpdatedAt()).isNotNull();
        Assertions.assertThat(genre.getRemovedAt()).isNull();
        Assertions.assertThat(genre.getCategories()).hasSize(1);
    }


    @ParameterizedTest
    @NullAndEmptySource
    public void givenAnInvalidGenre_whenCallsUpdate_shouldReceiveNotificationException(String nullAndEmptyName) {
        Genre genre = new Genre("acao", false);

        Assertions.assertThat(genre.getId()).isNotNull();
        Assertions.assertThat(genre.getName()).isEqualTo("acao");
        Assertions.assertThat(genre.isActive()).isFalse();
        Assertions.assertThat(genre.getCreatedAt()).isNotNull();
        Assertions.assertThat(genre.getUpdatedAt()).isNotNull();
        Assertions.assertThat(genre.getRemovedAt()).isNotNull();
        Assertions.assertThat(genre.getCategories()).hasSize(0);

        Assertions.assertThatThrownBy(() -> new Genre(nullAndEmptyName, true))
                .isInstanceOf(NotificationException.class);
    }

    @Test
    public void givenAValidGenre_whenCallsUpdateWithNullCategories_shouldReceiveOk() {
        Genre genre = new Genre("acao", false);

        Assertions.assertThat(genre.getId()).isNotNull();
        Assertions.assertThat(genre.getName()).isEqualTo("acao");
        Assertions.assertThat(genre.isActive()).isFalse();
        Assertions.assertThat(genre.getCreatedAt()).isNotNull();
        Assertions.assertThat(genre.getUpdatedAt()).isNotNull();
        Assertions.assertThat(genre.getRemovedAt()).isNotNull();
        Assertions.assertThat(genre.getCategories()).hasSize(0);

        Assertions.assertThatCode(() -> genre.update("acao", true, null))
                .doesNotThrowAnyException();
    }

    @Test
    public void givenAValidEmptyCategoriesGenre_whenCallAddCategory_shouldReceiveOk() {
        final CategoryID seriesId = CategoryID.from("123");
        final CategoryID moviesId = CategoryID.from("321");

        Genre genre = new Genre("acao", true);
        Assertions.assertThat(genre.getCategories()).hasSize(0);

        genre.addCategory(seriesId);
        Assertions.assertThat(genre.getCategories()).hasSize(1);

        genre.addCategory(moviesId);
        Assertions.assertThat(genre.getCategories()).hasSize(2);
    }

    @Test
    public void givenAValidGenreWithTwoCategories_whenCallRemoveCategory_shouldReceiveOk() {
        final CategoryID seriesId = CategoryID.from("123");
        final CategoryID moviesId = CategoryID.from("321");

        Genre genre = new Genre("acao", true);
        genre.update("acao", true, List.of(seriesId, moviesId));
        Assertions.assertThat(genre.getCategories()).hasSize(2);

        genre.removeCategory(seriesId);
        Assertions.assertThat(genre.getCategories()).hasSize(1);

        genre.removeCategory(seriesId);
        Assertions.assertThat(genre.getCategories()).hasSize(1);

        genre.removeCategory(moviesId);
        Assertions.assertThat(genre.getCategories()).hasSize(0);

        genre.removeCategory(null);
        Assertions.assertThat(genre.getCategories()).hasSize(0);
    }
}
