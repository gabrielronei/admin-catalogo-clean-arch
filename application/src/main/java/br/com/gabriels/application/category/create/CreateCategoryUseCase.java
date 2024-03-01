package br.com.gabriels.application.category.create;

import br.com.gabriels.application.UseCase;
import br.com.gabriels.domain.validation.handler.Notification;
import io.vavr.control.Either;

public abstract class CreateCategoryUseCase extends UseCase<CreateCategoryCommand, Either<Notification, CreateCategoryOutput>> {
}