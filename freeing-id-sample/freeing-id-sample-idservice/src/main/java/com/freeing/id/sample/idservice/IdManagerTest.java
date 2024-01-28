package com.freeing.id.sample.idservice;

import com.freeing.id.core.provider.impl.DefaultMachineIdProvider;
import com.freeing.id.core.provider.impl.PropertyMachineIdProvider;
import com.freeing.id.manager.IdManager;
import com.freeing.id.service.AbstractIdService;
import com.freeing.id.service.impl.IdServiceLockImpl;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class IdManagerTest {
    public static void main(String[] args) {
        AbstractIdService defaultIdService = new IdServiceLockImpl(new DefaultMachineIdProvider());
        IdManager idManager = new IdManager(defaultIdService);
        idManager.registry("order", new IdServiceLockImpl(new PropertyMachineIdProvider(55)));

        ExecutorService threadPool = Executors.newFixedThreadPool(8);
        for (int i = 0; i < 100; i++) {
            threadPool.submit(() -> {
                long id = idManager.genId();
                long keyid = idManager.genId("order");
                System.out.println(Thread.currentThread().getName() + ": id=" + id);
                System.out.println(Thread.currentThread().getName() + ": orderid=" + keyid);
            });
        }
        threadPool.shutdown();
    }
}
