package br.com.gabriels.infraestructure.category.models;

import br.com.gabriels.application.category.retrieve.get.CategoryOutput;
import br.com.gabriels.domain.category.Category;
import br.com.gabriels.domain.category.CategoryID;
import br.com.gabriels.infraestructure.JacksonTest;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.json.*;

import java.io.IOException;

@JacksonTest
public class CategoryApiOutputTest {

    @Autowired
    private JacksonTester<CategoryApiOutput> json;

    @Test
    void test_output_from_jackson_deserializing_correctly() throws IOException {
        CategoryApiOutput categoryApiOutput = new CategoryApiOutput(new CategoryOutput(new Category(CategoryID.unique(), "Teste", "ego kill talent", true)));

        JsonContent<CategoryApiOutput> response = this.json.write(categoryApiOutput);

        Assertions.assertThat(response)
                .hasJsonPathValue("$.id", categoryApiOutput.getId())
                .hasJsonPathValue("$.name", categoryApiOutput.getName())
                .hasJsonPathValue("$.description", categoryApiOutput.getDescription())
                .hasJsonPathValue("$.active", categoryApiOutput.isActive())
                .hasJsonPathValue("$.created_at", categoryApiOutput.getCreatedAt())
                .hasJsonPathValue("$.updated_at", categoryApiOutput.getUpdatedAt())
                .hasJsonPathValue("$.removed_at", categoryApiOutput.getRemovedAt());
    }
}
