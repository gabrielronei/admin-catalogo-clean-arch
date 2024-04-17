package br.com.gabriels.application.genre.create;

import br.com.gabriels.domain.genre.Genre;

public final class CreateGenreOutput {

    private final String genreId;

    public CreateGenreOutput(final Genre genre) {
        this.genreId = genre.getId().getValue();
    }

    public String getGenreId() {
        return genreId;
    }
}
