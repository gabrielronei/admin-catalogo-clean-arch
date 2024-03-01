package br.com.gabriels.infraestructure;

import br.com.gabriels.InfraestructureApplication;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.lang.annotation.*;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@ActiveProfiles("test-e2e")
@SpringBootTest(classes = InfraestructureApplication.class)
@ExtendWith(CleanUpExtensions.class)
@AutoConfigureMockMvc
//@Tag("e2eTest")
public @interface E2ETest {
}
