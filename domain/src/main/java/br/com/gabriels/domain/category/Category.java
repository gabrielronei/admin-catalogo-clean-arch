package br.com.gabriels.domain.category;

import br.com.gabriels.domain.AggregateRoot;
import br.com.gabriels.domain.validation.ValidationHandler;

import java.time.Instant;

public class Category extends AggregateRoot<CategoryID> implements Cloneable {

    private String name;
    private String description;
    private boolean active;
    private Instant createdAt = Instant.now();
    private Instant updatedAt = Instant.now();
    private Instant removedAt;

    public Category(CategoryID id, String name, String description, boolean active) {
        super(id);
        this.name = name;
        this.description = description;
        this.active = active;
        this.removedAt = active ? null : Instant.now();
    }

    public Category update(final String name, final String description, boolean isActive) {
        if (isActive) {
            activate();
        } else {
            deactivate();
        }

        this.name = name;
        this.description = description;
        this.updatedAt = Instant.now();

        return this;
    }

    public String getName() {
        return name;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public Instant getUpdatedAt() {
        return updatedAt;
    }

    public String getDescription() {
        return description;
    }

    public Instant getRemovedAt() {
        return removedAt;
    }

    public boolean isActive() {
        return active;
    }

    public static Category getInstance(final String name, final String description, final boolean isActive) {
        return new Category(CategoryID.unique(), name, description, isActive);
    }

    public void deactivate() {
        this.active = false;
        this.updatedAt = Instant.now();
        this.removedAt = Instant.now();
    }

    public Category activate() {
        this.active = true;
        this.updatedAt = Instant.now();
        this.removedAt = null;
        return this;
    }

    @Override
    public void validate(final ValidationHandler validationHandler) {
        new CategoryValidator(this, validationHandler).validate();
    }

    @Override
    public Category clone() {
        try {
            return (Category) super.clone();
        } catch (CloneNotSupportedException e) {
            throw new RuntimeException(e);
        }
    }
}