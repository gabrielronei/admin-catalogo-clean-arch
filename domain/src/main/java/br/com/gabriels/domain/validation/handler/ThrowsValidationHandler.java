package br.com.gabriels.domain.validation.handler;

import br.com.gabriels.domain.exceptions.DomainException;
import br.com.gabriels.domain.validation.Error;
import br.com.gabriels.domain.validation.ValidationHandler;

public class ThrowsValidationHandler implements ValidationHandler {

    @Override
    public ValidationHandler append(final Error error) {
        throw DomainException.with(error);
    }

    @Override
    public ValidationHandler append(final ValidationHandler anHandler) {
        throw DomainException.with(anHandler.getErrors());
    }

    @Override
    public <T> T validate(Validation<T> aValidation) {
        try {
            return aValidation.validate();
        } catch (final Exception ex) {
            throw DomainException.with(new Error(ex.getMessage()));
        }
    }
}
