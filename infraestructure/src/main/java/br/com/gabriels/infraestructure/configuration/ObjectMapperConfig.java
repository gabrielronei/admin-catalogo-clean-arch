package br.com.gabriels.infraestructure.configuration;

import br.com.gabriels.infraestructure.configuration.json.JSON;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ObjectMapperConfig {

    @Bean
    public ObjectMapper objectMapper() {
        return JSON.mapper();
    }
}
