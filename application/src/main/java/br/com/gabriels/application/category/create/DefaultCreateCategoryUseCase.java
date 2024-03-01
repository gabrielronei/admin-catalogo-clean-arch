package br.com.gabriels.application.category.create;

import br.com.gabriels.domain.category.Category;
import br.com.gabriels.domain.category.CategoryGateway;
import br.com.gabriels.domain.validation.handler.Notification;
import io.vavr.API;
import io.vavr.control.Either;

public class DefaultCreateCategoryUseCase extends CreateCategoryUseCase {

    private final CategoryGateway categoryGateway;

    public DefaultCreateCategoryUseCase(CategoryGateway categoryGateway) {
        this.categoryGateway = categoryGateway;
    }

    @Override
    public Either<Notification, CreateCategoryOutput> execute(CreateCategoryCommand createCategoryCommand) {
        final Notification notification = new Notification();

        Category category = createCategoryCommand.buildCategory();
        category.validate(notification);

        return notification.hasErrors() ? API.Left(notification) : getCategoryOutput(category);
    }

    private Either<Notification, CreateCategoryOutput> getCategoryOutput(Category category) {
        return API.Try(() -> this.categoryGateway.create(category))
                .toEither()
                .bimap(Notification::create, CreateCategoryOutput::new);
    }
}
