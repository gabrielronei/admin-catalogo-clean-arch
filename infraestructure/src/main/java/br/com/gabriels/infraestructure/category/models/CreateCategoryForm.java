package br.com.gabriels.infraestructure.category.models;

import br.com.gabriels.application.category.create.CreateCategoryCommand;
import jakarta.validation.constraints.NotBlank;

public record CreateCategoryForm(@NotBlank String name,
                                 @NotBlank String description,
                                 boolean active) {

    public CreateCategoryCommand toCommand() {
        return new CreateCategoryCommand(this.name, this.description, this.active);
    }
}
