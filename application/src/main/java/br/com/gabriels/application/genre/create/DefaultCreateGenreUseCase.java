package br.com.gabriels.application.genre.create;

import br.com.gabriels.domain.category.CategoryGateway;
import br.com.gabriels.domain.category.CategoryID;
import br.com.gabriels.domain.exceptions.NotificationException;
import br.com.gabriels.domain.genre.Genre;
import br.com.gabriels.domain.genre.GenreGateway;
import br.com.gabriels.domain.validation.Error;
import br.com.gabriels.domain.validation.ValidationHandler;
import br.com.gabriels.domain.validation.handler.Notification;
import io.vavr.control.Either;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class DefaultCreateGenreUseCase extends CreateGenreUseCase {

    private final GenreGateway genreGateway;
    private final CategoryGateway categoryGateway;

    public DefaultCreateGenreUseCase(GenreGateway genreGateway,
                                     CategoryGateway categoryGateway) {
        this.genreGateway = genreGateway;
        this.categoryGateway = categoryGateway;
    }

    @Override
    public Either<Notification, CreateGenreOutput> execute(CreateGenreCommand createGenreCommand) {

        final Notification notification = new Notification();
        notification.append(getValidCategories(createGenreCommand));

        final Genre genre = notification.validate(createGenreCommand::toGenre);

        if(notification.hasErrors()) {
            throw new NotificationException(notification);
        }
        return Either.right(new CreateGenreOutput(this.genreGateway.create(genre)));
    }

    private ValidationHandler getValidCategories(CreateGenreCommand createGenreCommand) {
        final Notification notification = new Notification();
        final var categories = createGenreCommand.getCategoryIds();

        if (categories == null || categories.isEmpty()) {
            return notification;
        }

        List<CategoryID> categoryIDS = this.categoryGateway.existsByIds(categories);
        if (categoryIDS.size() != categories.size()) {
            final var categoriesIds = new ArrayList<>(categories);
            categoriesIds.removeAll(categoryIDS);

            final var missingIds = categoriesIds.stream().map(CategoryID::getValue).collect(Collectors.joining(", "));
            notification.append(new Error("Some categories are missing! " + missingIds));
        }

        return notification;
    }
}
