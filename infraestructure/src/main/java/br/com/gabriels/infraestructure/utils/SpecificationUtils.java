package br.com.gabriels.infraestructure.utils;

import org.springframework.data.jpa.domain.Specification;

public final class SpecificationUtils {

    public static <T> Specification<T> like(final String prop, final String term) {
        return (root, query, cb) -> cb.like(cb.upper(root.get(prop)), like(term));
    }

    private static String like(final String term) {
        return "%" + term.toUpperCase() + "%";
    }
}
