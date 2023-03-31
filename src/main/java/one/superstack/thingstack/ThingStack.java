package one.superstack.thingstack;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableAsync
public class ThingStack {
    public static void main(String[] args) {
        SpringApplication.run(ThingStack.class, args);
    }
}