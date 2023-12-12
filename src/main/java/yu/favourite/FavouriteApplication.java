package yu.favourite;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class FavouriteApplication {

    public static void main(String[] args) {
        SpringApplication.run(FavouriteApplication.class, args);
    }

}
