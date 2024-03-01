package br.com.gabriels.infraestructure.category.models;

import br.com.gabriels.infraestructure.JacksonTest;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.ObjectContent;

import java.io.IOException;

@JacksonTest
class UpdateCategoryFormTest {

    @Autowired
    private JacksonTester<CreateCategoryForm> jsonCategoryForm;

    @Test
    void should_serialize_correctly() throws IOException {
        final String name = "Angra";
        final String description = "Rebirth of a man";
        final boolean active = true;

        final var json = """
                {
                    "name": "%s",
                    "description": "%s",
                    "active": "%s"
                }
        """.formatted(name, description, active);


        final ObjectContent<CreateCategoryForm> parsed = this.jsonCategoryForm.parse(json);

        Assertions.assertThat(parsed)
                .hasFieldOrPropertyWithValue("name", name)
                .hasFieldOrPropertyWithValue("description", description)
                .hasFieldOrPropertyWithValue("active", active);
    }
}