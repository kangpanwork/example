package test.springsecurity;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;

@SpringBootApplication
public class Application {

  public static void main(String[] args) throws Throwable {
    SpringApplication.run(Application.class, args);
   }
}