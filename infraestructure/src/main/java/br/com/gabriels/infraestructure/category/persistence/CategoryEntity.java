package br.com.gabriels.infraestructure.category.persistence;

import br.com.gabriels.domain.category.Category;
import br.com.gabriels.domain.category.CategoryID;
import jakarta.persistence.*;

import java.time.Instant;

@Entity
@Table(name = "category")
public class CategoryEntity {

    @Id
    private String id;

    @Column(nullable = false)
    private String name;

    @Column(length = 4000)
    private String description;

    @Column(nullable = false)
    private boolean active;

    @Column(name = "created_at", nullable = false, columnDefinition = "DATETIME(6)")
    private Instant createdAt;

    @Column(name="updated_at", columnDefinition = "DATETIME(6)")
    private Instant updatedAt;

    @Column(name="removed_at", columnDefinition = "DATETIME(6)")
    private Instant removedAt;

    @Deprecated
    public CategoryEntity() {
    }

    public CategoryEntity(Category category) {
        this.id = category.getId().getValue();
        this.name = category.getName();
        this.description = category.getDescription();
        this.active = category.isActive();
        this.createdAt = category.getCreatedAt();
        this.updatedAt = category.getUpdatedAt();
        this.removedAt = category.getRemovedAt();
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

    public Instant getCreatedAt() {
        return createdAt;
    }

    public Instant getUpdatedAt() {
        return updatedAt;
    }

    public Instant getRemovedAt() {
        return removedAt;
    }

    public Category toDomainModel() {
        return new Category(CategoryID.from(this.id), this.name, this.description, this.active);
    }
}
