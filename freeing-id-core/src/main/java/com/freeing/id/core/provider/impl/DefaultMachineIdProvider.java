package com.freeing.id.core.provider.impl;

import com.freeing.id.core.provider.MachineIdProvider;

import java.util.Random;

/**
 * 默认实现
 */
public class DefaultMachineIdProvider implements MachineIdProvider {
    @Override
    public long getMachineId() {
        return new Random().nextInt(1024);
    }
}
