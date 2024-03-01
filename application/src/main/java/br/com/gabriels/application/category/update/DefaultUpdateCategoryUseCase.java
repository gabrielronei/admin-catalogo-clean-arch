package br.com.gabriels.application.category.update;

import br.com.gabriels.domain.category.Category;
import br.com.gabriels.domain.category.CategoryGateway;
import br.com.gabriels.domain.exceptions.DomainException;
import br.com.gabriels.domain.validation.Error;
import br.com.gabriels.domain.validation.handler.Notification;
import io.vavr.API;
import io.vavr.control.Either;

public class DefaultUpdateCategoryUseCase extends UpdateCategoryUseCase {

    private final CategoryGateway categoryGateway;

    public DefaultUpdateCategoryUseCase(CategoryGateway categoryGateway) {
        this.categoryGateway = categoryGateway;
    }

    @Override
    public Either<Notification, UpdateCategoryOutput> execute(UpdateCategoryCommand updateCategoryCommand) {
        Category category = this.categoryGateway.findById(updateCategoryCommand.getCategoryId())
                .orElseThrow(() -> DomainException.with(new Error("Category with id %s not found".formatted(updateCategoryCommand.id()))));

        Notification notification = new Notification();

        category.update(updateCategoryCommand.name(), updateCategoryCommand.description(), updateCategoryCommand.isActive())
                .validate(notification);

        return notification.hasErrors() ? API.Left(notification) : getCategoryUpdated(category);
    }

    private Either<Notification, UpdateCategoryOutput> getCategoryUpdated(Category category) {
        return API.Try(() -> this.categoryGateway.update(category))
                .toEither()
                .bimap(Notification::create, UpdateCategoryOutput::new);
    }
}
