package br.com.gabriels.infraestructure.api.controllers;

import br.com.gabriels.application.category.create.CreateCategoryOutput;
import br.com.gabriels.application.category.create.CreateCategoryUseCase;
import br.com.gabriels.application.category.delete.DeleteCategoryUseCase;
import br.com.gabriels.application.category.retrieve.get.GetCategoryByIdUseCase;
import br.com.gabriels.application.category.retrieve.list.ListCategoryUseCase;
import br.com.gabriels.application.category.update.UpdateCategoryOutput;
import br.com.gabriels.application.category.update.UpdateCategoryUseCase;
import br.com.gabriels.domain.category.CategoryID;
import br.com.gabriels.domain.pagination.SearchQuery;
import br.com.gabriels.domain.pagination.Pagination;
import br.com.gabriels.domain.validation.handler.Notification;
import br.com.gabriels.infraestructure.api.CategoryAPI;
import br.com.gabriels.infraestructure.category.models.*;
import io.vavr.control.Either;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;
import java.util.function.Function;

@RestController
public class CategoryController implements CategoryAPI {

    private final CreateCategoryUseCase createCategoryUseCase;
    private final ListCategoryUseCase listCategoryUseCase;
    private final GetCategoryByIdUseCase getCategoryByIdUseCase;
    private final UpdateCategoryUseCase updateCategoryUseCase;
    private final DeleteCategoryUseCase deleteCategoryUseCase;

    public CategoryController(CreateCategoryUseCase createCategoryUseCase,
                              ListCategoryUseCase listCategoryUseCase,
                              GetCategoryByIdUseCase getCategoryByIdUseCase,
                              UpdateCategoryUseCase updateCategoryUseCase,
                              DeleteCategoryUseCase deleteCategoryUseCase) {
        this.createCategoryUseCase = createCategoryUseCase;
        this.listCategoryUseCase = listCategoryUseCase;
        this.getCategoryByIdUseCase = getCategoryByIdUseCase;
        this.updateCategoryUseCase = updateCategoryUseCase;
        this.deleteCategoryUseCase = deleteCategoryUseCase;
    }

    @Override
    public ResponseEntity<?> createCategory(final CreateCategoryForm createCategoryForm) {
        Either<Notification, CreateCategoryOutput> execute = createCategoryUseCase.execute(createCategoryForm.toCommand());

        Function<CreateCategoryOutput, ResponseEntity<?>> onSuccess = output -> ResponseEntity.created(URI.create("/categories/".concat(output.getCategoryID()))).body(output);
        return execute.fold(ResponseEntity.unprocessableEntity()::body, onSuccess);
    }

    @Override
    public Pagination<?> listCategories(final String search, final int page, final int perPage, final String sort, final String direction) {
        return this.listCategoryUseCase.execute(new SearchQuery(page, perPage, search, sort, direction));
    }

    @Override
    public ResponseEntity<CategoryApiOutput> getCategoryBy(final String id) {
        return ResponseEntity.ok(new CategoryApiOutput(this.getCategoryByIdUseCase.execute(CategoryID.from(id))));
    }

    @Override
    public ResponseEntity<?> updateCategoryById(final String id, final UpdateCategoryForm updateCategoryForm) {
        Either<Notification, UpdateCategoryOutput> execute = this.updateCategoryUseCase.execute(updateCategoryForm.toCommand());

        return execute.fold(ResponseEntity.unprocessableEntity()::body, ResponseEntity::ok);
    }

    @Override
    public void deleteById(final String id) {
        this.deleteCategoryUseCase.execute(CategoryID.from(id));
    }
}
