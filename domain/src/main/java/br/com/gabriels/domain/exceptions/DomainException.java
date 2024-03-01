package br.com.gabriels.domain.exceptions;

import br.com.gabriels.domain.validation.Error;

import java.util.ArrayList;
import java.util.List;

public class DomainException extends NoStackTraceException {

    protected List<Error> errors = new ArrayList<>();

    protected DomainException(final String message, final List<Error> errors) {
        super(message);
        this.errors = errors;
    }

    public static DomainException with(final String errorMessage) {
        return DomainException.with(new Error(errorMessage));
    }

    public static DomainException with(final Error error) {
        return new DomainException(error.message(), List.of(error));
    }

    public static DomainException with(final List<Error> errors) {
        return new DomainException("", errors);
    }

    public List<Error> getErrors() {
        return errors;
    }
}
