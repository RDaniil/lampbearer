package com.vdn.lampbearer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.stereotype.Component;

@SpringBootApplication
@Component
public class LampbearerApplication implements CommandLineRunner {

    @Autowired
    private GameStarter gameStarter;

    public static void main(String[] args) {
        SpringApplicationBuilder builder = new SpringApplicationBuilder(LampbearerApplication.class);
        builder.headless(false);
        ConfigurableApplicationContext context = builder.run(args);
    }


    @Override
    public void run(String... args) {
        gameStarter.start();
    }

}
