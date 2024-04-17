package br.com.gabriels.domain.genre;

import br.com.gabriels.domain.pagination.Pagination;
import br.com.gabriels.domain.pagination.SearchQuery;

import java.util.Optional;

public interface GenreGateway {

    Genre create(Genre genre);

    void deleteById(GenreId genreId);

    Optional<Genre> findById(GenreId genreId);

    Genre update(Genre Genre);

    Pagination<Genre> findAll(SearchQuery searchQuery);
}
