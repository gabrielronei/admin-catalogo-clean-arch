package br.com.gabriels.infraestructure.endToEnd.category;

import br.com.gabriels.domain.category.CategoryID;
import br.com.gabriels.infraestructure.E2ETest;
import br.com.gabriels.infraestructure.category.models.*;
import br.com.gabriels.infraestructure.category.persistence.CategoryEntity;
import br.com.gabriels.infraestructure.category.persistence.CategoryRepository;
import br.com.gabriels.infraestructure.configuration.json.JSON;
import org.assertj.core.api.Assertions;
import org.assertj.core.api.OptionalAssert;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

import java.util.Optional;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;

@E2ETest
@Testcontainers
public class CategoryE2ETest {
//
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private CategoryRepository categoryRepository;

    @Container
    private static final MySQLContainer MYSQL_CONTAINER = new MySQLContainer(DockerImageName.parse("mysql:5.5"))
            .withPassword("123456")
            .withUsername("root")
            .withDatabaseName("admin_videos");

    @DynamicPropertySource
    public static void setDatasourceProperties(final DynamicPropertyRegistry registry) {
        Integer mappedPort = MYSQL_CONTAINER.getMappedPort(3306);

        System.out.println("============ ".concat(mappedPort.toString()));
        registry.add("mysql.port", () -> mappedPort);
    }
//
//    @Test
//    public void asACatalogAdminIShouldBeAbleToCreateANewCategoryWithValidValues() throws Exception {
//        Assertions.assertThat(MYSQL_CONTAINER.isRunning()).isTrue();
//
//        Assertions.assertThat(categoryRepository.count()).isZero();
//
//        final var expectedName = "Filmes";
//        final var expectedDescription = "A categoria mais assistida";
//        final var expectedIsActive = true;
//
//        final var category = givenACategory(expectedName, expectedDescription, expectedIsActive);
//
//        final var actualCategory = categoryRepository.findById(category.getValue()).get();
//
//        Assertions.assertThat(expectedName).isEqualTo(actualCategory.getName());
//        Assertions.assertThat(expectedDescription).isEqualTo(actualCategory.getDescription());
//        Assertions.assertThat(expectedIsActive).isEqualTo(actualCategory.isActive());
//        Assertions.assertThat(actualCategory.getCreatedAt()).isNotNull();
//        Assertions.assertThat(actualCategory.getUpdatedAt()).isNotNull();
//        Assertions.assertThat(actualCategory.getRemovedAt()).isNull();
//    }
//
//    @Test
//    public void asACatalogAdminIShouldBeAbleToNavigateToAllCategories() throws Exception {
//        Assertions.assertThat(MYSQL_CONTAINER.isRunning()).isTrue();
//        Assertions.assertThat(categoryRepository.count()).isZero();
//
//        givenACategory("Filmes", null, true);
//        givenACategory("Documentários", null, true);
//        givenACategory("Séries", null, true);
//
//        listCategories(0, 1)
//                .andExpect(MockMvcResultMatchers.status().isOk())
//                .andExpect(MockMvcResultMatchers.jsonPath("$.current_page", equalTo(0)))
//                .andExpect(MockMvcResultMatchers.jsonPath("$.per_page", equalTo(1)))
//                .andExpect(MockMvcResultMatchers.jsonPath("$.total", equalTo(3)))
//                .andExpect(MockMvcResultMatchers.jsonPath("$.items", hasSize(1)))
//                .andExpect(MockMvcResultMatchers.jsonPath("$.items[0].name", equalTo("Documentários")));
//
//        listCategories(1, 1)
//                .andExpect(MockMvcResultMatchers.status().isOk())
//                .andExpect(MockMvcResultMatchers.jsonPath("$.current_page", equalTo(1)))
//                .andExpect(MockMvcResultMatchers.jsonPath("$.per_page", equalTo(1)))
//                .andExpect(MockMvcResultMatchers.jsonPath("$.total", equalTo(3)))
//                .andExpect(MockMvcResultMatchers.jsonPath("$.items", hasSize(1)))
//                .andExpect(MockMvcResultMatchers.jsonPath("$.items[0].name", equalTo("Filmes")));
//
//        listCategories(2, 1)
//                .andExpect(MockMvcResultMatchers.status().isOk())
//                .andExpect(MockMvcResultMatchers.jsonPath("$.current_page", equalTo(2)))
//                .andExpect(MockMvcResultMatchers.jsonPath("$.per_page", equalTo(1)))
//                .andExpect(MockMvcResultMatchers.jsonPath("$.total", equalTo(3)))
//                .andExpect(MockMvcResultMatchers.jsonPath("$.items", hasSize(1)))
//                .andExpect(MockMvcResultMatchers.jsonPath("$.items[0].name", equalTo("Séries")));
//
//        listCategories(3, 1)
//                .andExpect(MockMvcResultMatchers.status().isOk())
//                .andExpect(MockMvcResultMatchers.jsonPath("$.current_page", equalTo(3)))
//                .andExpect(MockMvcResultMatchers.jsonPath("$.per_page", equalTo(1)))
//                .andExpect(MockMvcResultMatchers.jsonPath("$.total", equalTo(3)))
//                .andExpect(MockMvcResultMatchers.jsonPath("$.items", hasSize(0)));
//    }

    @Test
    public void asACatalogAdminIShouldBeAbleToSearchBetweenAllCategories() throws Exception {
        Assertions.assertThat(MYSQL_CONTAINER.isRunning()).isTrue();
        Assertions.assertThat(categoryRepository.count()).isZero();

        givenACategory("Filmes", null, true);
        givenACategory("Documentários", null, true);
        givenACategory("Séries", null, true);

        listCategories(0, 1, "fil", "", "asc")
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.current_page", equalTo(0)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.per_page", equalTo(1)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.total", equalTo(1)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.items", hasSize(1)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.items[0].name", equalTo("Filmes")));
    }

    @Test
    public void asACatalogAdminIShouldBeAbleToSortAllCategoriesByDescriptionDesc() throws Exception {
        Assertions.assertThat(MYSQL_CONTAINER.isRunning()).isTrue();
        Assertions.assertThat(categoryRepository.count()).isZero();

        givenACategory("Filmes", "C", true);
        givenACategory("Documentários", "Z", true);
        givenACategory("Séries", "A", true);

        listCategories(0, 3, "", "description", "desc")
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.current_page", equalTo(0)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.per_page", equalTo(3)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.total", equalTo(3)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.items", hasSize(3)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.items[0].name", equalTo("Documentários")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.items[1].name", equalTo("Filmes")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.items[2].name", equalTo("Séries")));
    }

    @Test
    public void asACatalogAdminIShouldBeAbleToGetACategoryByItsIdentifier() throws Exception {
        Assertions.assertThat(MYSQL_CONTAINER.isRunning()).isTrue();
        Assertions.assertThat(categoryRepository.count()).isZero();

        final var expectedName = "Filmes";
        final var expectedDescription = "A categoria mais assistida";
        final var expectedIsActive = true;

        final var actualId = givenACategory(expectedName, expectedDescription, expectedIsActive);

        final var actualCategory = getCategoryBy(actualId.getValue());

        Assertions.assertThat(expectedName).isEqualTo(actualCategory.getName());
        Assertions.assertThat(expectedDescription).isEqualTo(actualCategory.getDescription());
        Assertions.assertThat(expectedIsActive).isEqualTo(actualCategory.isActive());
        Assertions.assertThat(actualCategory.getCreatedAt()).isNotNull();
        Assertions.assertThat(actualCategory.getUpdatedAt()).isNotNull();
        Assertions.assertThat(actualCategory.getRemovedAt()).isEmpty();
    }

    @Test
    public void asACatalogAdminIShouldBeAbleToSeeATreatedErrorByGettingANotFoundCategoryByItsIdentifier() throws Exception {
        Assertions.assertThat(MYSQL_CONTAINER.isRunning()).isTrue();
        Assertions.assertThat(categoryRepository.count()).isZero();


        final String id = CategoryID.unique().getValue();
        final MockHttpServletRequestBuilder request = MockMvcRequestBuilders.get("/categories/".concat(id))
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON);

        this.mockMvc.perform(request)
                .andExpect(MockMvcResultMatchers.status().isNotFound())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message", equalTo("Category with ID %s not found!".formatted(id))));
    }

    @Test
    public void asACatalogAdminIShouldBeAbleToGetOldCategoryWithoutUpdatingTheOther() throws Exception {
        Assertions.assertThat(MYSQL_CONTAINER.isRunning()).isTrue();
        Assertions.assertThat(categoryRepository.count()).isZero();

        final String originalName = "Movies";
        final var actualId = givenACategory(originalName, null, true);

        final var expectedName = "Filmes";
        final var expectedDescription = "A categoria mais assistida";
        final var expectedIsActive = true;

        final String anotherRandomId = CategoryID.unique().getValue();
        final var aRequestBody = new UpdateCategoryForm(anotherRandomId, expectedName, expectedDescription, expectedIsActive);

        final var aRequest = MockMvcRequestBuilders.put("/categories/".concat(anotherRandomId))
                .contentType(MediaType.APPLICATION_JSON)
                .content(JSON.writeValueAsString(aRequestBody));

        this.mockMvc.perform(aRequest)
                .andExpect(MockMvcResultMatchers.status().isUnprocessableEntity());

        Optional<CategoryEntity> categoryEntityOptionalAssert = categoryRepository.findById(actualId.getValue());

        Assertions.assertThat(categoryEntityOptionalAssert).isPresent();
        Assertions.assertThat(categoryEntityOptionalAssert.get().getName()).isEqualTo(originalName);

    }

    @Test
    public void asACatalogAdminIShouldBeAbleToUpdateACategoryByItsIdentifier() throws Exception {
        Assertions.assertThat(MYSQL_CONTAINER.isRunning()).isTrue();
        Assertions.assertThat(categoryRepository.count()).isZero();

        final var actualId = givenACategory("Movies", null, true);

        final var expectedName = "Filmes";
        final var expectedDescription = "A categoria mais assistida";
        final var expectedIsActive = true;

        final var aRequestBody = new UpdateCategoryForm(actualId.getValue(), expectedName, expectedDescription, expectedIsActive);

        final var aRequest = MockMvcRequestBuilders.put("/categories/".concat(actualId.getValue()))
                .contentType(MediaType.APPLICATION_JSON)
                .content(JSON.writeValueAsString(aRequestBody));

        this.mockMvc.perform(aRequest)
                .andExpect(MockMvcResultMatchers.status().isOk());

        final var actualCategory = categoryRepository.findById(actualId.getValue()).get();

        Assertions.assertThat(categoryRepository.count()).isEqualTo(1);

        Assertions.assertThat(expectedName).isEqualTo(actualCategory.getName());
        Assertions.assertThat(expectedDescription).isEqualTo(actualCategory.getDescription());
        Assertions.assertThat(expectedIsActive).isEqualTo(actualCategory.isActive());
        Assertions.assertThat(actualCategory.getCreatedAt()).isNotNull();
        Assertions.assertThat(actualCategory.getUpdatedAt()).isNotNull();
        Assertions.assertThat(actualCategory.getRemovedAt()).isNull();
    }

    @Test
    public void asACatalogAdminIShouldBeAbleToDeleteACategoryByItsIdentifier() throws Exception {
        Assertions.assertThat(MYSQL_CONTAINER.isRunning()).isTrue();
        Assertions.assertThat(categoryRepository.count()).isZero();

        final var actualId = givenACategory("Filmes", null, true);
        Assertions.assertThat(categoryRepository.count()).isEqualTo(1);

        this.mockMvc.perform(MockMvcRequestBuilders.delete("/categories/".concat(actualId.getValue())))
                .andExpect(MockMvcResultMatchers.status().isNoContent());
        Assertions.assertThat(categoryRepository.count()).isZero();
    }


    private ResultActions listCategories(final int page, final int perPage, final String search) throws Exception {
        return this.listCategories(page, perPage, search, "", "");
    }



    private ResultActions listCategories(final int page, final int perPage) throws Exception {
        return this.listCategories(page, perPage, "", "", "");
    }

    private ResultActions listCategories(final int page, final int perPage, final String search, final String sort, final String direction) throws Exception {
        final var aRequest = MockMvcRequestBuilders.get("/categories")
                .queryParam("page", String.valueOf(page))
                .queryParam("perPage", String.valueOf(perPage))
                .queryParam("search", search)
                .queryParam("sort", sort)
                .queryParam("direction", direction)
                .contentType(MediaType.APPLICATION_JSON);

        return this.mockMvc.perform(aRequest).andExpect(MockMvcResultMatchers.status().isOk());
    }


    private CategoryID givenACategory(final String name, final String description, final boolean isActive) throws Exception {
        final var requestBody = new CreateCategoryForm(name, description, isActive);


        final var aRequest = MockMvcRequestBuilders.post("/categories")
                .contentType(MediaType.APPLICATION_JSON)
                .content(JSON.writeValueAsString(requestBody));

        return CategoryID.from(this.mockMvc.perform(aRequest).andExpect(MockMvcResultMatchers.status().isCreated()).andReturn()
                .getResponse()
                .getHeader("Location")
                .replace("/categories/", ""));
    }

    private CategoryApiOutput getCategoryBy(final String anId) throws Exception {
        final var aRequest = MockMvcRequestBuilders.get("/categories/".concat(anId));

        final var json = this.mockMvc.perform(aRequest).andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn().getResponse().getContentAsString();


        return JSON.readValue(json, CategoryApiOutput.class);
    }
}
