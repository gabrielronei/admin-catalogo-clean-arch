package br.com.gabriels.domain.genre;

import br.com.gabriels.domain.Identifier;
import br.com.gabriels.domain.category.CategoryID;

import java.util.Objects;
import java.util.UUID;

public class GenreId extends Identifier {
    private final String value;

    private GenreId(final String value) {
        Objects.requireNonNull(value);

        this.value = value;
    }

    @Override
    public String getValue() {
        return value;
    }

    public static GenreId unique() {
        return from(UUID.randomUUID());
    }

    public static GenreId from(final String id) {
        return new GenreId(id);
    }

    public static GenreId from(UUID uuid) {
        return new GenreId(uuid.toString().toLowerCase());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        GenreId that = (GenreId) o;
        return Objects.equals(value, that.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }
}
