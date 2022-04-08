package Test.Demo.td.config;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@EnableJpaRepositories(basePackages = "Test.Demo.fw")
@EntityScan(basePackages = "Test.Demo.fw")
@SpringBootApplication(scanBasePackages = {"Test.Demo.td"})
public class Application{
    public static void main( String[] args )
    {
    	SpringApplication.run(Application.class, args);
    }

}
