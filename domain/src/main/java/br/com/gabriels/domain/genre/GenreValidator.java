package br.com.gabriels.domain.genre;

import br.com.gabriels.domain.validation.Error;
import br.com.gabriels.domain.validation.ValidationHandler;
import br.com.gabriels.domain.validation.Validator;

public class GenreValidator extends Validator {

    private final Genre genre;

    public GenreValidator(final Genre genre, final ValidationHandler handler) {
        super(handler);
        this.genre = genre;
    }

    @Override
    public void validate() {
        final String name = this.genre.getName();

        if (name == null) {
            this.validationHandler().append(new Error("'name' should not be null!"));
            return;
        }

        if (name.isBlank()) {
            this.validationHandler().append(new Error("'name' should not be blank!"));
            return;
        }

        final int length = name.trim().length();
        if (length > 255 || length < 3) {
            this.validationHandler().append(new Error("'name' must be between 3 and 255 characters!"));
            return;
        }
    }
}
