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
        this.machineId = machineId;
        validateMachineId();
    }

    @Override
    public long getMachineId() {
        return machineId;
    }

    public void setMachineId(long machineId) {
        this.machineId = machineId;
    }
}
