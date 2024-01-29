package com.freeing.id.sample.idservice;

import com.freeing.id.core.provider.impl.DefaultMachineIdProvider;
import com.freeing.id.core.provider.impl.PropertyMachineIdProvider;
import com.freeing.id.factory.IdServiceFactory;
import com.freeing.id.service.AbstractIdService;
import com.freeing.id.service.impl.IdServiceLockImpl;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class IdFactoryTest {
    public static void main(String[] args) {
        AbstractIdService defaultIdService = new IdServiceLockImpl(new DefaultMachineIdProvider());
        IdServiceFactory idServiceFactory = new IdServiceFactory(
            1420041600000L, 0, 0, 0, new DefaultMachineIdProvider()
        );
        idServiceFactory.registry("order", new IdServiceLockImpl(new PropertyMachineIdProvider(55)));

        ExecutorService threadPool = Executors.newFixedThreadPool(8);
        for (int i = 0; i < 100; i++) {
            threadPool.submit(() -> {
                long id = idServiceFactory.get().genId();
                long keyid = idServiceFactory.get("order").genId();
                System.out.println(Thread.currentThread().getName() + ": id=" + id);
                System.out.println(Thread.currentThread().getName() + ": orderid=" + keyid);
            });
        }
        threadPool.shutdown();
    }
}
