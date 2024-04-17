package br.com.gabriels.application.genre.create;

import br.com.gabriels.application.UseCase;
import br.com.gabriels.application.category.create.CreateCategoryCommand;
import br.com.gabriels.application.category.create.CreateCategoryOutput;
import br.com.gabriels.domain.validation.handler.Notification;
import io.vavr.control.Either;

public abstract class CreateGenreUseCase extends UseCase<CreateGenreCommand, Either<Notification, CreateGenreOutput>> {
}