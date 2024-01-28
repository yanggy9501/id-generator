package com.freeing.sample.spring;

import com.freeing.id.core.bean.Id;
import com.freeing.id.manager.IdManager;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootApplication
public class Main {
    public static void main(String[] args) {
        ConfigurableApplicationContext context = SpringApplication.run(Config.class, args);
        IdManager idManager = context.getBean(IdManager.class);
        long id = idManager.genId();
        System.out.println(id);
        Id expId = idManager.expId(id);
        System.out.println(expId);
    }
}