package br.com.gabriels.domain.exceptions;


import br.com.gabriels.domain.AggregateRoot;
import br.com.gabriels.domain.Identifier;
import br.com.gabriels.domain.validation.Error;

import java.util.Collections;
import java.util.List;

public class NotFoundException extends DomainException {

    protected NotFoundException(final String message, final List<Error> errors) {
        super(message, errors);
    }

    public static NotFoundException with(final Class<? extends AggregateRoot<?>> anAggregate, final Identifier id) {
        return new NotFoundException("%s with ID %s not found!".formatted(anAggregate.getSimpleName(), id.getValue()), Collections.emptyList());
    }
}
