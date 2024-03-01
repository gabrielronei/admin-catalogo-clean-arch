package br.com.gabriels.infraestructure.configuration.usecases;

import br.com.gabriels.application.category.create.CreateCategoryUseCase;
import br.com.gabriels.application.category.create.DefaultCreateCategoryUseCase;
import br.com.gabriels.application.category.delete.DefaultDeleteCategoryUseCase;
import br.com.gabriels.application.category.delete.DeleteCategoryUseCase;
import br.com.gabriels.application.category.retrieve.get.DefaultGetCategoryByIdUseCase;
import br.com.gabriels.application.category.retrieve.get.GetCategoryByIdUseCase;
import br.com.gabriels.application.category.retrieve.list.DefaultListCategoryUseCase;
import br.com.gabriels.application.category.retrieve.list.ListCategoryUseCase;
import br.com.gabriels.application.category.update.DefaultUpdateCategoryUseCase;
import br.com.gabriels.application.category.update.UpdateCategoryUseCase;
import br.com.gabriels.infraestructure.category.DefaultCategoryGateway;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CategoryUseCaseConfig {

    private final DefaultCategoryGateway defaultCategoryGateway;

    public CategoryUseCaseConfig(DefaultCategoryGateway defaultCategoryGateway) {
        this.defaultCategoryGateway = defaultCategoryGateway;
    }

    @Bean
    public CreateCategoryUseCase createCategoryUseCase() {
        return new DefaultCreateCategoryUseCase(defaultCategoryGateway);
    }

    @Bean
    public UpdateCategoryUseCase updateCategoryUseCase() {
        return new DefaultUpdateCategoryUseCase(defaultCategoryGateway);
    }

    @Bean
    public GetCategoryByIdUseCase getCategoryByIdUseCase() {
        return new DefaultGetCategoryByIdUseCase(defaultCategoryGateway);
    }

    @Bean
    public DeleteCategoryUseCase deleteCategoryUseCase() {
        return new DefaultDeleteCategoryUseCase(defaultCategoryGateway);
    }

    @Bean
    public ListCategoryUseCase listCategoryUseCase() {
        return new DefaultListCategoryUseCase(defaultCategoryGateway);
    }
}
