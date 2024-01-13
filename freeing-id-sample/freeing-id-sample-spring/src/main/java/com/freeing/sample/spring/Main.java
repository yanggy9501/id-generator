package com.freeing.sample.spring;

import com.freeing.id.manager.IdManager;
import com.freeing.id.spring.config.IdGeneratorAutoConfig;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class Main {
    public static void main(String[] args) {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(Config.class);
        IdManager idManager = context.getBean(IdManager.class);
        IdGeneratorAutoConfig d = context.getBean(IdGeneratorAutoConfig.class);
        for (int i = 0; i < 10000; i++) {
            long id = idManager.nextId();
            System.out.println(id);
            System.out.println(idManager.expId(id));
        }
        System.out.println("=");
    }
}