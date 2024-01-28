package com.freeing.id.sample.idservice;

import com.freeing.id.core.provider.impl.DefaultMachineIdProvider;
import com.freeing.id.service.IdService;
import com.freeing.id.service.impl.IdServiceLockImpl;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class IdServiceTest {
    public static void main(String[] args) {
        IdService idService = new IdServiceLockImpl(new DefaultMachineIdProvider());

        System.out.println("============== 多线程获取id ===============");
        ExecutorService threadPool = Executors.newFixedThreadPool(8);
        for (int i = 0; i < 100; i++) {
            threadPool.submit(() -> {
                long id = idService.genId();
                System.out.println(Thread.currentThread().getName() + ": id=" + id);
            });
        }
        threadPool.shutdown();
    }
}
