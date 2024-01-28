package com.freeing.id.core.provider;

/**
 * 机器ID获取接口
 */
public interface MachineIdProvider {
    long getMachineId();

    /**
     * 校验机器ID是否合法，规定机器大于：0 <= MachineId <1024
     */
    default void validateMachineId() {
        if (getMachineId() <= 0 || getMachineId() >= 1024) {
            throw new RuntimeException("machine id should: 0 <= id < 1024");
        }
    }
}
