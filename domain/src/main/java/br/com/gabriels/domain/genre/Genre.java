package br.com.gabriels.domain.genre;

import br.com.gabriels.domain.AggregateRoot;
import br.com.gabriels.domain.category.CategoryID;
import br.com.gabriels.domain.exceptions.NotificationException;
import br.com.gabriels.domain.validation.ValidationHandler;
import br.com.gabriels.domain.validation.handler.Notification;

import java.time.Instant;
import java.util.*;

public class Genre extends AggregateRoot<GenreId> {

    private String name;
    private boolean active;
    private List<CategoryID> categories = new ArrayList<>();
    private Instant createdAt;
    private Instant updatedAt;
    private Instant removedAt;

    public Genre(GenreId genreId, String name, boolean active) {
        super(genreId);
        this.name = name;
        this.active = active;
        this.createdAt = Instant.now();
        this.updatedAt = Instant.now();
        this.removedAt = active ? null : Instant.now();

        validate();
    }

    public Genre(String name, boolean active) {
        this(GenreId.unique(), name, active);
    }

    public String getName() {
        return name;
    }

    public List<CategoryID> getCategories() {
        return Collections.unmodifiableList(categories);
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

    public void addCategory(CategoryID categoryID) {
        if (categoryID == null) return;
        this.updatedAt = Instant.now();
        this.categories.add(categoryID);
    }

    public void addCategories(List<CategoryID> categories) {
        if (categories == null || categories.isEmpty()) return;
        this.categories.addAll(categories);
    }

    public void removeCategory(CategoryID categoryID) {
        if (categoryID == null) return;
        this.updatedAt = Instant.now();
        this.categories.remove(categoryID);
    }

    public void inactivate() {
        this.active = false;
        this.removedAt = Instant.now();
        this.updatedAt = Instant.now();
    }

    public void activate() {
        this.active = true;
        this.removedAt = null;
        this.updatedAt = Instant.now();
    }


    public void update(String expectedName, boolean expectedActive, List<CategoryID> categories) {
        if (expectedActive) {
            activate();
        } else {
            inactivate();
        }

        this.name = expectedName;
        this.categories = categories != null ? new ArrayList<>(categories) : Collections.emptyList();
        this.updatedAt = Instant.now();

        validate();
    }

    @Override
    public void validate(final ValidationHandler validationHandler) {
        new GenreValidator(this, validationHandler).validate();
    }

    private void validate() {
        final var notification = new Notification();
        validate(notification);

        if (notification.hasErrors()) {
            throw new NotificationException(notification);
        }
    }
}
