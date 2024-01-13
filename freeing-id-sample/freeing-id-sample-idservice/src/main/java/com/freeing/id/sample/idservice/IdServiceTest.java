package com.freeing.id.sample.idservice;

import com.freeing.id.core.provider.MachineIdProvider;
import com.freeing.id.service.AbstractIdService;
import com.freeing.id.service.impl.IdServiceLockImpl;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author yanggy
 */
public class IdServiceTest {
    public static void main(String[] args) {
        AbstractIdService idService = new IdServiceLockImpl();
        idService.setMachineIdProvider(new MachineIdProvider() {
            @Override
            public long getMachineId() {
                return 111;
            }
        });
        idService.init();

        System.out.println("============== 多线程获取id ===============");
        idService.genId();
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
