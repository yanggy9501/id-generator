package com.freeing.id.core.provider.impl;

import com.freeing.id.core.provider.MachineIdProvider;

/**
 * 默认实现
 */
public class DefaultMachineIdProvider implements MachineIdProvider {
    @Override
    public long getMachineId() {
        return 0b1000000000;
    }
}
