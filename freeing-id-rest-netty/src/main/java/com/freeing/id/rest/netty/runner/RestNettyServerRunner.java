package com.freeing.id.rest.netty.runner;

import com.freeing.id.rest.netty.RestNettyServer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import javax.annotation.PreDestroy;

@Component
public class RestNettyServerRunner implements CommandLineRunner {
    @Value("${nettysrv.port:12380}")
    private int port;

    RestNettyServer restNettyServer;

    @Override
    public void run(String... args) throws Exception {
        restNettyServer = new RestNettyServer(port);
        restNettyServer.start();
    }

    @PreDestroy
    public void destroy() {
        restNettyServer.shutdown();
    }
}
