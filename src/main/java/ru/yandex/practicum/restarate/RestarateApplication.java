package ru.yandex.practicum.restarate;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@EntityScan("ru.yandex.practicum.restarate.model")
@ComponentScan(basePackages = "ru.yandex.practicum.restarate")
public class RestarateApplication {
    public static void main(String[] args) {
        SpringApplication.run(RestarateApplication.class, args);
    }
}
