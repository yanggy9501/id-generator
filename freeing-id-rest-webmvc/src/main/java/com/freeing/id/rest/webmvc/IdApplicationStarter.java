package com.freeing.id.rest.webmvc;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan("com.freeing")
public class IdApplicationStarter {
    public static void main(String[] args) {
        ConfigurableApplicationContext ctx = SpringApplication.run(IdApplicationStarter.class, args);
        System.out.println(ctx.getEnvironment().getProperty("id.generator.provider"));
    }
}
