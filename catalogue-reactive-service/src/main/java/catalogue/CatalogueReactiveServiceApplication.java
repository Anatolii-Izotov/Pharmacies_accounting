package catalogue;
import io.mongock.runner.springboot.EnableMongock;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.mongodb.repository.config.EnableReactiveMongoRepositories;

@SpringBootApplication
@EnableReactiveMongoRepositories(basePackages = "catalogue.repository")
public class CatalogueReactiveServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(CatalogueReactiveServiceApplication.class, args);
    }
}
