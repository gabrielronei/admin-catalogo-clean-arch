package br.com.gabriels.domain.validation.handler;

import br.com.gabriels.domain.exceptions.DomainException;
import br.com.gabriels.domain.validation.Error;
import br.com.gabriels.domain.validation.ValidationHandler;

import java.util.ArrayList;
import java.util.List;

public class Notification implements ValidationHandler {

    private final List<Error> errors;

    public Notification() {
        this.errors = new ArrayList<>();
    }

    public Notification(final List<Error> errors) {
        this.errors = errors;
    }

    @Override
    public List<Error> getErrors() {
        return errors;
    }

    public static Notification create(Throwable t) {
        return new Notification(List.of(new Error(t.getMessage())));
    }

    public static Notification create(Error e) {
        return new Notification(List.of(e));
    }

    @Override
    public Notification append(final Error error) {
        this.errors.add(error);
        return null;
    }

    @Override
    public Notification append(final ValidationHandler anHandler) {
        this.errors.addAll(anHandler.getErrors());
        return null;
    }

    @Override
    public <T> T validate(final Validation<T> aValidation) {

        try {
            return aValidation.validate();
        } catch (final DomainException d) {
            this.errors.addAll(d.getErrors());
        } catch (final Throwable t) {
            this.errors.add(new Error(t.getMessage()));
        }

        return null;
    }
}
