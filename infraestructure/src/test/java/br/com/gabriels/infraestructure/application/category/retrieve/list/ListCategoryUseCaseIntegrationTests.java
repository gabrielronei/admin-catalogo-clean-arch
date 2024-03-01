package br.com.gabriels.infraestructure.application.category.retrieve.list;

import br.com.gabriels.application.category.retrieve.list.ListCategoryUseCase;
import br.com.gabriels.domain.category.*;
import br.com.gabriels.infraestructure.CleanUpExtensions;
import br.com.gabriels.infraestructure.category.persistence.CategoryEntity;
import br.com.gabriels.infraestructure.category.persistence.CategoryRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@ExtendWith(CleanUpExtensions.class)
public class ListCategoryUseCaseIntegrationTests {

    @Autowired
    private ListCategoryUseCase listCategoryUseCase;

    @Autowired
    private CategoryRepository categoryRepository;

    @BeforeEach
    void mockUp() {
        final var categories = Stream.of(
                        new Category(CategoryID.unique(), "Filmes", null, true),
                        new Category(CategoryID.unique(), "Netflix Originals", "Títulos de autoria da Netflix", true),
                        new Category(CategoryID.unique(), "Amazon Originals", "Títulos de autoria da Amazon Prime", true),
                        new Category(CategoryID.unique(), "Documentários", null, true),
                        new Category(CategoryID.unique(), "Sports", null, true),
                        new Category(CategoryID.unique(), "Kids", "Categoria para crianças", true),
                        new Category(CategoryID.unique(), "Series", null, true)
                )
                .map(CategoryEntity::new)
                .toList();

        categoryRepository.saveAllAndFlush(categories);
    }

    @Test
    public void givenAValidTerm_whenTermDoesntMatchsPrePersisted_shouldReturnEmptyPage() {
        final var expectedPage = 0;
        final var expectedPerPage = 10;
        final var expectedTerms = "ji1j3i 1j3i1oj";
        final var expectedSort = "name";
        final var expectedDirection = "asc";
        final var expectedItemsCount = 0;
        final var expectedTotal = 0;

        final var aQuery = new CategorySearchQuery(expectedPage, expectedPerPage, expectedTerms, expectedSort, expectedDirection);

        final var actualResult = listCategoryUseCase.execute(aQuery);

        assertThat(expectedItemsCount).isEqualTo(actualResult.items().size());
        assertThat(expectedPage).isEqualTo(actualResult.currentPage());
        assertThat(expectedPerPage).isEqualTo(actualResult.perPage());
        assertThat(expectedTotal).isEqualTo(actualResult.total());
    }

    @ParameterizedTest
    @CsvSource({
            "fil,0,10,1,1,Filmes",
            "net,0,10,1,1,Netflix Originals",
            "ZON,0,10,1,1,Amazon Originals",
            "KI,0,10,1,1,Kids",
            "crianças,0,10,1,1,Kids",
            "da Amazon,0,10,1,1,Amazon Originals",
    })
    public void givenAValidTerm_whenCallsListCategories_shouldReturnCategoriesFiltered(final String expectedTerms,
                                                                                       final int expectedPage, final int expectedPerPage, final int expectedItemsCount, final long expectedTotal, final String expectedCategoryName) {
        final var expectedSort = "name";
        final var expectedDirection = "asc";

        final var aQuery = new CategorySearchQuery(expectedPage, expectedPerPage, expectedTerms, expectedSort, expectedDirection);

        final var actualResult = listCategoryUseCase.execute(aQuery);

        assertThat(expectedItemsCount).isEqualTo(actualResult.items().size());
        assertThat(expectedPage).isEqualTo(actualResult.currentPage());
        assertThat(expectedPerPage).isEqualTo(actualResult.perPage());
        assertThat(expectedTotal).isEqualTo(actualResult.total());
        assertThat(expectedCategoryName).isEqualTo(actualResult.items().get(0).getName());
    }

    @ParameterizedTest
    @CsvSource({
            "name,asc,0,10,7,7,Amazon Originals",
            "name,desc,0,10,7,7,Sports",
            "createdAt,asc,0,10,7,7,Filmes",
            "createdAt,desc,0,10,7,7,Series",
    })
    public void givenAValidSortAndDirection_whenCallsListCategories_thenShouldReturnCategoriesOrdered(final String expectedSort, final String expectedDirection,
                                                                                                      final int expectedPage, final int expectedPerPage,
                                                                                                      final int expectedItemsCount, final long expectedTotal,
                                                                                                      final String expectedCategoryName) {
        final var expectedTerms = "";

        final var aQuery = new CategorySearchQuery(expectedPage, expectedPerPage, expectedTerms, expectedSort, expectedDirection);

        final var actualResult = listCategoryUseCase.execute(aQuery);

        Assertions.assertThat(expectedItemsCount).isEqualTo(actualResult.items().size());
        Assertions.assertThat(expectedPage).isEqualTo(actualResult.currentPage());
        Assertions.assertThat(expectedPerPage).isEqualTo(actualResult.perPage());
        Assertions.assertThat(expectedTotal).isEqualTo(actualResult.total());
        Assertions.assertThat(expectedCategoryName).isEqualTo(actualResult.items().get(0).getName());
    }

    @ParameterizedTest
    @CsvSource({
            "0,2,2,7,Amazon Originals;Documentários",
            "1,2,2,7,Filmes;Kids",
            "2,2,2,7,Netflix Originals;Series",
            "3,2,1,7,Sports",
    })
    public void givenAValidPage_whenCallsListCategories_shouldReturnCategoriesPaginated(final int expectedPage, final int expectedPerPage,
            final int expectedItemsCount, final long expectedTotal, final String expectedCategoriesName) {

        final var expectedSort = "name";
        final var expectedDirection = "asc";
        final var expectedTerms = "";

        final var aQuery = new CategorySearchQuery(expectedPage, expectedPerPage, expectedTerms, expectedSort, expectedDirection);

        final var actualResult = listCategoryUseCase.execute(aQuery);

        Assertions.assertThat(expectedItemsCount).isEqualTo(actualResult.items().size());
        Assertions.assertThat(expectedPage).isEqualTo(actualResult.currentPage());
        Assertions.assertThat(expectedPerPage).isEqualTo(actualResult.perPage());
        Assertions.assertThat(expectedTotal).isEqualTo(actualResult.total());

        int index = 0;
        for (final String expectedName : expectedCategoriesName.split(";")) {
            final String actualName = actualResult.items().get(index).getName();
            Assertions.assertThat(expectedName).isEqualTo(actualName);
            index++;
        }
    }
}
