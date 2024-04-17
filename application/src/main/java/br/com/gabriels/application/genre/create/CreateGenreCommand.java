package br.com.gabriels.application.genre.create;

import br.com.gabriels.domain.category.CategoryID;
import br.com.gabriels.domain.genre.Genre;

import java.util.List;

public record CreateGenreCommand(String name, boolean isActive, List<String> categories) {


    public List<CategoryID> getCategoryIds() {
        return categories.stream().map(CategoryID::from).toList();
    }

    public Genre toGenre() {
        final Genre genre = new Genre(name, isActive);
        genre.addCategories(this.getCategoryIds());
        return genre;
    }
}
