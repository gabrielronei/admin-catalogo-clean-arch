package br.com.gabriels.infraestructure;

import org.junit.jupiter.api.extension.BeforeEachCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.springframework.data.repository.CrudRepository;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Collection;

public class CleanUpExtensions implements BeforeEachCallback {
    @Override
    public void beforeEach(ExtensionContext extensionContext) throws Exception {
        Collection<CrudRepository> values = SpringExtension.getApplicationContext(extensionContext)
                .getBeansOfType(CrudRepository.class)
                .values();
        values.forEach(CrudRepository::deleteAll);
    }
}
