package ru.gentleman.quiz;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@EnableCaching
@SpringBootApplication
public class GentlemanQuizServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(GentlemanQuizServiceApplication.class, args);
    }

}
