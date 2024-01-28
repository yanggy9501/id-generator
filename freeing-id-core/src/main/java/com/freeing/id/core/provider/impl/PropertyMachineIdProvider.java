package com.freeing.id.core.provider.impl;

import com.freeing.id.core.provider.MachineIdProvider;

/**
 * 属性方式提供机器ID
 */
public class PropertyMachineIdProvider implements MachineIdProvider {
    /**
     * 机器ID
     */
    private long machineId;

    public PropertyMachineIdProvider(long machineId) {
        if (machineId < 0 || machineId >= 1024) {
            throw new RuntimeException("machine id should: 0 <= id < 1024");
        }
        this.machineId = machineId;
    }

    @Override
    public long getMachineId() {
        return machineId;
    }

    public void setMachineId(long machineId) {
        this.machineId = machineId;
    }
}
