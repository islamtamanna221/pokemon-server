package org.example;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class PokemonServerApplication {

    public static void main(
            String[] args
    ) {

        SpringApplication.run(
                PokemonServerApplication.class,
                args
        );
    }
}