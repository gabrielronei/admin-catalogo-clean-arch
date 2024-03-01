package br.com.gabriels.infraestructure.category.persistence;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<CategoryEntity, String> {

    Page<CategoryEntity> findAll(Specification<CategoryEntity> where, Pageable page);
}
