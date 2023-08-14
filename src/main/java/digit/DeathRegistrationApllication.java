package digit;


import org.egov.tracer.config.TracerConfiguration;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Import;

@Import({TracerConfiguration.class})
@SpringBootApplication
@ComponentScan(basePackages = {"digit", "digit.web.controllers", "digit.config"})
public class DeathRegistrationApllication {


    public static void main(String[] args) {
        SpringApplication.run(DeathRegistrationApllication.class, args);
    }

}
