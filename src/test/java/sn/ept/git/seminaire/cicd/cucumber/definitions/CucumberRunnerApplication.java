package sn.ept.git.seminaire.cicd.cucumber.definitions;

import io.cucumber.spring.CucumberContextConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import sn.ept.git.seminaire.cicd.TodoApplication;

@AutoConfigureMockMvc
@SpringBootTest(
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
        classes = TodoApplication.class
)
@CucumberContextConfiguration()
@TestPropertySource(locations = {"classpath:application.properties", "classpath:cucumber.properties"})
public class CucumberRunnerApplication {
}