package com.freeing.sample.spring;

import com.freeing.id.core.bean.Id;
import com.freeing.id.factory.IdServiceFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootApplication
public class Main {
    public static void main(String[] args) {
        ConfigurableApplicationContext context = SpringApplication.run(Config.class, args);
        IdServiceFactory idServiceFactory = context.getBean(IdServiceFactory.class);
        long id = idServiceFactory.get().genId();
        System.out.println(id);
        Id expId = idServiceFactory.get().expId(id);
        System.out.println(expId);
    }
}