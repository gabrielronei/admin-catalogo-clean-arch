package br.com.gabriels.domain.validation;

import java.util.Collections;
import java.util.List;

public interface ValidationHandler {

    ValidationHandler append(Error error);

    ValidationHandler append(ValidationHandler anHandler);

    <T> T validate(Validation<T> aValidation);

    default List<Error> getErrors() {
        return Collections.emptyList();
    }

    default boolean hasErrors() {
        return !getErrors().isEmpty();
    }

    interface Validation<T> {
        T validate();
    }
}
