package br.com.gabriels.infraestructure.api;

import br.com.gabriels.domain.pagination.Pagination;
import br.com.gabriels.infraestructure.category.models.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Categories")
public interface CategoryAPI {

    @PostMapping(value = "/categories")
    @Operation(summary = "Create a new category")
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "201", description = "Created successfully"),
                    @ApiResponse(responseCode = "422", description = "Unprocessable error"),
                    @ApiResponse(responseCode = "500", description = "An internal server error")
            }
    )
    ResponseEntity<?> createCategory(@RequestBody @Valid CreateCategoryForm createCategoryForm);

    @GetMapping("/categories")
    @Operation(summary = "List all categories")
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "201", description = "Listed successfully"),
                    @ApiResponse(responseCode = "422", description = "An invalid parameter"),
                    @ApiResponse(responseCode = "500", description = "An internal server error")
            }
    )
    Pagination<?> listCategories(
            @RequestParam(name = "search", required = false, defaultValue = "") final String search,
            @RequestParam(name = "page", required = false, defaultValue = "0") final int page,
            @RequestParam(name = "perPage", required = false, defaultValue = "10") final int perPage,
            @RequestParam(name = "sort", required = false, defaultValue = "name") final String sort,
            @RequestParam(name = "direction", required = false, defaultValue = "asc") final String direction
    );

    @GetMapping(value = "/categories/{id}")
    @Operation(summary = "Get a category by it's identifier")
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200", description = "Category retrieved successfully"),
                    @ApiResponse(responseCode = "404", description = "Category not found"),
                    @ApiResponse(responseCode = "500", description = "An internal server error")
            }
    )
    ResponseEntity<CategoryApiOutput> getCategoryBy(@PathVariable final String id);


    @PutMapping(value = "/categories/{id}")
    @Operation(summary = "Update a category by it's identifier")
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200", description = "Category updated successfully"),
                    @ApiResponse(responseCode = "404", description = "Category not found"),
                    @ApiResponse(responseCode = "500", description = "An internal server error")
            }
    )
    ResponseEntity<?> updateCategoryById(@PathVariable final String id, @RequestBody @Valid final UpdateCategoryForm updateCategoryForm);



    @DeleteMapping(value = "/categories/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Delete a category by it's identifier")
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "204", description = "Category delete successfully"),
                    @ApiResponse(responseCode = "404", description = "Category not found"),
                    @ApiResponse(responseCode = "500", description = "An internal server error")
            }
    )
    void deleteById(@PathVariable final String id);
}
