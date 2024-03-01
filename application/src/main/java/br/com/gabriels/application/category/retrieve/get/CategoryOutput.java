package br.com.gabriels.application.category.retrieve.get;

import br.com.gabriels.domain.category.Category;
import br.com.gabriels.domain.category.CategoryID;

import java.time.Instant;

public final class CategoryOutput {

    private final CategoryID id;
    private final String name;
    private final String description;
    private final boolean isActive;
    private final Instant createdAt;
    private final Instant updatedAt;
    private final Instant removedAt;

    public CategoryOutput(Category category) {
        this.id = category.getId();
        this.name = category.getName();
        this.description = category.getDescription();
        this.isActive = category.isActive();
        this.createdAt = category.getCreatedAt();
        this.updatedAt = category.getUpdatedAt();
        this.removedAt = category.getRemovedAt();
    }

    public CategoryID getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public boolean isActive() {
        return isActive;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public Instant getUpdatedAt() {
        return updatedAt;
    }

    public Instant getRemovedAt() {
        return removedAt;
    }
}
