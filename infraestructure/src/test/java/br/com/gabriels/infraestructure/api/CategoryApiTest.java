package br.com.gabriels.infraestructure.api;

import br.com.gabriels.application.category.create.*;
import br.com.gabriels.application.category.delete.DeleteCategoryUseCase;
import br.com.gabriels.application.category.retrieve.get.CategoryOutput;
import br.com.gabriels.application.category.retrieve.get.GetCategoryByIdUseCase;
import br.com.gabriels.application.category.retrieve.list.CategoryListOutput;
import br.com.gabriels.application.category.retrieve.list.ListCategoryUseCase;
import br.com.gabriels.application.category.update.UpdateCategoryOutput;
import br.com.gabriels.application.category.update.UpdateCategoryUseCase;
import br.com.gabriels.domain.category.Category;
import br.com.gabriels.domain.category.CategoryID;
import br.com.gabriels.domain.exceptions.DomainException;
import br.com.gabriels.domain.exceptions.NotFoundException;
import br.com.gabriels.domain.pagination.Pagination;
import br.com.gabriels.domain.validation.Error;
import br.com.gabriels.domain.validation.handler.Notification;
import br.com.gabriels.infraestructure.ControllerTest;
import br.com.gabriels.infraestructure.category.models.CreateCategoryForm;
import br.com.gabriels.infraestructure.category.models.UpdateCategoryForm;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.vavr.control.Either;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.List;
import java.util.Objects;

import static org.mockito.ArgumentMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ControllerTest(controllers = CategoryAPI.class)
public class CategoryApiTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private CreateCategoryUseCase createCategoryUseCase;

    @MockBean
    private GetCategoryByIdUseCase getCategoryByIdUseCase;

    @MockBean
    private UpdateCategoryUseCase updateCategoryUseCase;

    @MockBean
    private DeleteCategoryUseCase deleteCategoryUseCase;

    @MockBean
    private ListCategoryUseCase listCategoryUseCase;

    @Test
    void givenAValidCommand_whenCallsCreateCategory_shouldReturnCategoryId() throws Exception {
        final var expectedName = "filmes";
        final var expectedDescription = "a categoria mais assistida";
        final var expectedIsActive = true;
        final var categoryID = CategoryID.unique();


        final CreateCategoryForm createCategoryForm = new CreateCategoryForm(expectedName, expectedDescription, expectedIsActive);

        Category category = new Category(categoryID, expectedName, expectedDescription, expectedIsActive);

        Mockito.when(createCategoryUseCase.execute(Mockito.any())).thenReturn(Either.right(new CreateCategoryOutput(category)));

        this.mockMvc.perform(MockMvcRequestBuilders.post("/categories")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createCategoryForm)))
                .andDo(print())
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.header().string("Location", "/categories/".concat(categoryID.getValue())))
                .andExpect(MockMvcResultMatchers.header().string("Content-Type", MediaType.APPLICATION_JSON_VALUE))
                .andExpect(MockMvcResultMatchers.jsonPath("$.category_id", Matchers.equalTo(categoryID.getValue())));


        Mockito.verify(createCategoryUseCase, Mockito.times(1)).execute(argThat(cmd ->
                Objects.equals(expectedName, cmd.name()) &&
                        Objects.equals(expectedDescription, cmd.description()) &&
                        Objects.equals(expectedIsActive, cmd.isActive())
        ));
    }

    @Test
    void givenAnInvalidCommand_whenCallsCreateCategory_shouldReturnErrorMessage() throws Exception {
        final String expectedName = null;
        final var expectedDescription = "a categoria mais assistida";
        final var expectedIsActive = true;


        final CreateCategoryForm createCategoryForm = new CreateCategoryForm(expectedName, expectedDescription, expectedIsActive);

        Mockito.when(createCategoryUseCase.execute(Mockito.any())).thenReturn(Either.left(Notification.create(new Error("Name should not be empty!"))));

        this.mockMvc.perform(MockMvcRequestBuilders.post("/categories").contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createCategoryForm)))
                .andDo(print())
                .andExpect(status().isUnprocessableEntity())
                .andExpect(MockMvcResultMatchers.header().string("Location", Matchers.nullValue()))
                .andExpect(MockMvcResultMatchers.header().string("Content-Type", MediaType.APPLICATION_JSON_VALUE));

        Mockito.verify(createCategoryUseCase, Mockito.times(1)).execute(argThat(cmd ->
                Objects.equals(expectedName, cmd.name()) &&
                        Objects.equals(expectedDescription, cmd.description()) &&
                        Objects.equals(expectedIsActive, cmd.isActive())
        ));
    }

    @Test
    void givenAnInvalidCommand_whenCallsCreateCategory_shouldThrowAnException() throws Exception {
        final String expectedName = null;
        final var expectedDescription = "a categoria mais assistida";
        final var expectedIsActive = true;

        final CreateCategoryForm createCategoryForm = new CreateCategoryForm(expectedName, expectedDescription, expectedIsActive);

        Mockito.when(createCategoryUseCase.execute(Mockito.any())).thenThrow(DomainException.with(new Error("Name should not be empty!")));

        this.mockMvc.perform(MockMvcRequestBuilders.post("/categories").contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createCategoryForm)))
                .andDo(print())
                .andExpect(status().isUnprocessableEntity())
                .andExpect(MockMvcResultMatchers.header().string("Location", Matchers.nullValue()))
                .andExpect(MockMvcResultMatchers.header().string("Content-Type", MediaType.APPLICATION_JSON_VALUE))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message", Matchers.equalTo("Name should not be empty!")));
    }

    @Test
    void givenAValidId_whenCallsGetCategory_shouldReturnTheCategory() throws Exception {
        final String expectedName = "XPTO";
        final var expectedDescription = "a categoria mais assistida";
        final var expectedIsActive = true;

        final CreateCategoryCommand createCategoryForm = new CreateCategoryForm(expectedName, expectedDescription, expectedIsActive).toCommand();

        final Category category = createCategoryForm.buildCategory();
        final var categoryId = category.getId().getValue();

        Mockito.when(getCategoryByIdUseCase.execute(Mockito.any())).thenReturn(new CategoryOutput(category));

        this.mockMvc.perform(MockMvcRequestBuilders.get("/categories/{id}", categoryId))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", Matchers.equalTo(categoryId)))
                .andExpect(jsonPath("$.name", Matchers.equalTo(expectedName)))
                .andExpect(jsonPath("$.description", Matchers.equalTo(expectedDescription)))
                .andExpect(jsonPath("$.active", Matchers.equalTo(expectedIsActive)))
                .andExpect(jsonPath("$.created_at", Matchers.equalTo(category.getCreatedAt().toString())))
                .andExpect(jsonPath("$.updated_at", Matchers.equalTo(category.getUpdatedAt().toString())))
                .andExpect(jsonPath("$.removed_at", Matchers.equalTo("")));
    }

    @Test
    void givenAnInvalidId_whenCallsGetCategory_shouldReturnNotFound() throws Exception {
        final var categoryId = CategoryID.from("1234");

        Mockito.when(getCategoryByIdUseCase.execute(Mockito.any()))
                .thenThrow(NotFoundException.with(Category.class, categoryId));

        this.mockMvc.perform(MockMvcRequestBuilders.get("/categories/{id}", categoryId))
                .andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message", Matchers.equalTo("Category with ID %s not found!".formatted(categoryId.getValue()))));
    }

    @Test
    void givenAValidCategoryToUpdate_whenCallsUpdateCategory_shouldUpdateCategory() throws Exception {
        final var expectedName = "filmes";
        final var expectedDescription = "a categoria mais assistida";
        final var expectedIsActive = true;
        final var categoryID = CategoryID.unique();


        final Category category = new Category(categoryID, "xpto", "xpto", false);

        Mockito.when(updateCategoryUseCase.execute(Mockito.any())).thenReturn(Either.right(new UpdateCategoryOutput(category)));

        final UpdateCategoryForm updateCategoryForm = new UpdateCategoryForm(categoryID.getValue(), expectedName, expectedDescription, expectedIsActive);

        this.mockMvc.perform(MockMvcRequestBuilders.put("/categories/{id}", categoryID.getValue())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateCategoryForm)))
                .andDo(print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(jsonPath("$.category_id", Matchers.equalTo(categoryID.getValue())));


        Mockito.verify(updateCategoryUseCase, Mockito.times(1)).execute(argThat(cmd ->
                Objects.equals(expectedName, cmd.name()) && Objects.equals(expectedDescription, cmd.description())
                        && Objects.equals(expectedIsActive, cmd.isActive())
        ));
    }

    @Test
    void givenAnInvalidCategoryToUpdate_whenCallsUpdateCategory_shouldReturnErrorMessage() throws Exception {
        final var expectedName = "filmes";
        final var expectedDescription = "a categoria mais assistida";
        final var expectedIsActive = true;
        final var categoryID = CategoryID.unique();

        Mockito.when(updateCategoryUseCase.execute(Mockito.any())).thenReturn(Either.left(Notification.create(new Error("Category not found!"))));

        final UpdateCategoryForm updateCategoryForm = new UpdateCategoryForm(categoryID.getValue(), expectedName, expectedDescription, expectedIsActive);

        this.mockMvc.perform(MockMvcRequestBuilders.put("/categories/{id}", categoryID.getValue())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateCategoryForm)))
                .andDo(print())
                .andExpect(MockMvcResultMatchers.status().isUnprocessableEntity())
                .andExpect(jsonPath("$.errors[0].message", Matchers.equalTo("Category not found!")));


        Mockito.verify(updateCategoryUseCase, Mockito.times(1)).execute(argThat(cmd ->
                Objects.equals(expectedName, cmd.name()) && Objects.equals(expectedDescription, cmd.description())
                        && Objects.equals(expectedIsActive, cmd.isActive())
        ));
    }

    @Test
    void givenAnInvalidCategoryToUpdate_whenCallsUpdateCategory_shouldReturnNotFound() throws Exception {
        final var expectedName = "filmes";
        final var expectedDescription = "a categoria mais assistida";
        final var expectedIsActive = true;
        final var categoryID = CategoryID.unique();

        Mockito.when(updateCategoryUseCase.execute(Mockito.any())).thenThrow(NotFoundException.with(Category.class, categoryID));

        final UpdateCategoryForm updateCategoryForm = new UpdateCategoryForm(categoryID.getValue(), expectedName, expectedDescription, expectedIsActive);

        this.mockMvc.perform(MockMvcRequestBuilders.put("/categories/{id}", categoryID.getValue())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateCategoryForm)))
                .andDo(print())
                .andExpect(MockMvcResultMatchers.status().isNotFound())
                .andExpect(jsonPath("$.message", Matchers.equalTo("Category with ID %s not found!".formatted(categoryID.getValue()))));


        Mockito.verify(updateCategoryUseCase, Mockito.times(1)).execute(argThat(cmd ->
                Objects.equals(expectedName, cmd.name()) && Objects.equals(expectedDescription, cmd.description())
                        && Objects.equals(expectedIsActive, cmd.isActive())
        ));
    }

    @Test
    void givenAValidId_whenCallsDeleteCategory_shouldBeOK() throws Exception {
        final var expectedId = CategoryID.from("123");

        Mockito.doNothing().when(deleteCategoryUseCase).execute(Mockito.any());

        this.mockMvc.perform(MockMvcRequestBuilders.delete("/categories/{id}", expectedId.getValue())
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isNoContent());

        Mockito.verify(deleteCategoryUseCase, Mockito.times(1)).execute(eq(expectedId));
    }

    @Test
    void givenValidParams_whenCallsListCategories_shouldReturnCategoriesFiltered() throws Exception {
        final Category category = new Category(CategoryID.unique(), "nome", "descricao", true);
        final Category anotherCategory = new Category(CategoryID.unique(), "nome2", "descricao2", true);
        final var expectedPage = 0;
        final var expectedPerPage = 10;
        final var expectedTerms = "movies";
        final var expectedSort = "description";
        final var expectedDirection = "desc";
        final List<Category> expectedItems = List.of(category, anotherCategory);
        final var expectedItemsCount = expectedItems.size();
        final var expectedTotal = expectedItems.size();

        Mockito.when(listCategoryUseCase.execute(any())).thenReturn(new Pagination<>(expectedPage, expectedPerPage, expectedTotal, expectedItems.stream().map(CategoryListOutput::new).toList()));


        this.mockMvc.perform(MockMvcRequestBuilders.get("/categories")
                        .queryParam("page", String.valueOf(expectedPage))
                        .queryParam("perPage", String.valueOf(expectedPerPage))
                        .queryParam("sort", expectedSort)
                        .queryParam("direction", expectedDirection)
                        .queryParam("search", expectedTerms)
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(jsonPath("$.current_page", Matchers.equalTo(expectedPage)))
                .andExpect(jsonPath("$.per_page", Matchers.equalTo(expectedPerPage)))
                .andExpect(jsonPath("$.total", Matchers.equalTo(expectedTotal)))
                .andExpect(jsonPath("$.items", Matchers.hasSize(expectedItemsCount)))
                .andExpect(jsonPath("$.items[0].name", Matchers.equalTo(category.getName())))
                .andExpect(jsonPath("$.items[1].name", Matchers.equalTo(anotherCategory.getName())));

        Mockito.verify(listCategoryUseCase, Mockito.times(1)).execute(argThat(query ->
                Objects.equals(expectedPage, query.page())
                        && Objects.equals(expectedPerPage, query.perPage())
                        && Objects.equals(expectedTerms, query.terms())
                        && Objects.equals(expectedSort, query.sort())
                        && Objects.equals(expectedDirection, query.direction())
        ));
    }
}
