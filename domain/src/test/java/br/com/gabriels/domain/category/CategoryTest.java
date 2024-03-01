package br.com.gabriels.domain.category;

import br.com.gabriels.domain.exceptions.DomainException;
import br.com.gabriels.domain.validation.handler.ThrowsValidationHandler;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;

import java.time.Instant;

import static org.assertj.core.api.Assertions.assertThat;

class CategoryTest {

    @ParameterizedTest
    @NullAndEmptySource
    void category__should_throws_validation_exception_if_name_is_null_or_empty(String title) {
        Assertions.assertThatThrownBy(() -> new Category(CategoryID.unique(), title, "descrição legal", true)
                        .validate(new ThrowsValidationHandler()))
                .isInstanceOf(DomainException.class)
                .hasMessageContaining("Name should not be empty!");
    }

    @Test
    void category__should_build_category_correctly() {
        Assertions.assertThatCode(() -> new Category(CategoryID.unique(), "charlie brown", "descrição legal", true)
                        .validate(new ThrowsValidationHandler()))
                .doesNotThrowAnyException();
    }

    @Test
    void category__name_should_not_have_length_less_than_minimum_characters() {
        Assertions.assertThatThrownBy(() -> new Category(CategoryID.unique(), "r ", "descrição legal", true)
                        .validate(new ThrowsValidationHandler()))
                .isInstanceOf(DomainException.class)
                .hasMessageContaining("Name should be between 3 and 255 characters!");

        Assertions.assertThatThrownBy(() -> new Category(CategoryID.unique(), "re ", "descrição legal", true)
                        .validate(new ThrowsValidationHandler()))
                .isInstanceOf(DomainException.class)
                .hasMessageContaining("Name should be between 3 and 255 characters!");

        Assertions.assertThatCode(() -> new Category(CategoryID.unique(), "red", "descrição legal", true)
                        .validate(new ThrowsValidationHandler()))
                .doesNotThrowAnyException();
    }

    @Test
    void category__name_should_not_have_length_greater_than_minimum_characters() {
        String twoHundredAndFiftySevenCharacters = """
                Lorem ipsum dolor sit amet, consectetur adipiscing elit. Maecenas commodoss leo at placerat facilisis. Sed semper eu massa hendrerit vestibulum. Cras dapibus tincidunt elit tempor finibus. Suspendisse potenti. Duis sit amet tellus nunc. Class aptent ligula.
                """;

        Assertions.assertThatThrownBy(() -> new Category(CategoryID.unique(), twoHundredAndFiftySevenCharacters, "descrição legal", true)
                        .validate(new ThrowsValidationHandler()))
                .isInstanceOf(DomainException.class)
                .hasMessageContaining("Name should be between 3 and 255 characters!");


        Assertions.assertThatThrownBy(() -> new Category(CategoryID.unique(), twoHundredAndFiftySevenCharacters.substring(0, 256), "descrição legal", true)
                        .validate(new ThrowsValidationHandler()))
                .isInstanceOf(DomainException.class)
                .hasMessageContaining("Name should be between 3 and 255 characters!");

        Assertions.assertThatCode(() -> new Category(CategoryID.unique(), twoHundredAndFiftySevenCharacters.substring(0, 255), "descrição legal", true)
                        .validate(new ThrowsValidationHandler()))
                .doesNotThrowAnyException();
    }

    @Test
    void should_create_a_category_active_or_inactive_corretly() {
        Category category = new Category(CategoryID.unique(), "charlie brown", "descrição legal", true);
        assertThat(category.getRemovedAt()).isNull();
        assertThat(category.isActive()).isTrue();


        category = new Category(CategoryID.unique(), "charlie brown", "descrição legal", false);
        assertThat(category.getRemovedAt()).isNotNull();
        assertThat(category.isActive()).isFalse();
    }

    @Test
    void deactivate__should_disable_category_correctly() {
        Category category = new Category(CategoryID.unique(), "charlie brown", "descrição legal", true);
        Instant firstUpdatedAt = category.getUpdatedAt();

        assertThat(category.getRemovedAt()).isNull();
        assertThat(category.isActive()).isTrue();

        category.deactivate();
        assertThat(category.getRemovedAt()).isNotNull();
        assertThat(category.getUpdatedAt()).isNotEqualTo(firstUpdatedAt);
        assertThat(category.isActive()).isFalse();
    }
}