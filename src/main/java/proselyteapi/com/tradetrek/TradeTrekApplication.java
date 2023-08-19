package proselyteapi.com.tradetrek;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.r2dbc.repository.config.EnableR2dbcRepositories;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.reactive.config.EnableWebFlux;

@SpringBootApplication
@EnableWebFlux
@EnableScheduling
@EnableR2dbcRepositories(basePackages = "proselyteapi.com.tradetrek.repository")
public class TradeTrekApplication {

    public static void main(String[] args) {
        SpringApplication.run(TradeTrekApplication.class, args);
    }

}
