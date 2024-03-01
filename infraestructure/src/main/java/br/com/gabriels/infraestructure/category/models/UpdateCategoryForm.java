package br.com.gabriels.infraestructure.category.models;

import br.com.gabriels.application.category.update.UpdateCategoryCommand;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record UpdateCategoryForm(@NotNull String id,
                                 @NotBlank String name,
                                 @NotBlank String description,
                                 boolean active) {

    public UpdateCategoryCommand toCommand() {
        return new UpdateCategoryCommand(this.id, this.name, this.description, this.active);
    }
}
