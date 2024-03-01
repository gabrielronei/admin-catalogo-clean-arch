package br.com.gabriels.infraestructure.category.models;

import br.com.gabriels.application.category.retrieve.get.CategoryOutput;

public final class CategoryApiOutput {

    private String id;
    private String name;
    private String description;
    private boolean active;
    private String createdAt;
    private String removedAt;
    private String updatedAt;

    @Deprecated
    public CategoryApiOutput() {}

    public CategoryApiOutput(CategoryOutput category) {
        this.id = category.getId().getValue();
        this.name = category.getName();
        this.description = category.getDescription();
        this.active = category.isActive();
        this.createdAt = category.getCreatedAt().toString();
        this.updatedAt = category.getUpdatedAt().toString();
        this.removedAt = category.getRemovedAt() != null ? category.getRemovedAt().toString() : "";
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public boolean isActive() {
        return active;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public String getRemovedAt() {
        return removedAt;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }
}
