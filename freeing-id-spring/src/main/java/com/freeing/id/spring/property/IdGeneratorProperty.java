package com.freeing.id.spring.property;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class IdGeneratorProperty {
    @Value("${id.generator.epoch:1420041600000}")
    private long epoch;

    /**
     * 00：嵌入发布模式 01：中心服务器发布模式 02：REST发布模式 03：保留未用
     */
    @Value("${id.generator.genMethod:0}")
    private long genMethod;

    /**
     * 0：最大峰值型 1：最小粒度型
     */
    @Value("${id.generator.type:0}")
    private long type;

    /**
     * 版本
     * 0：默认值，以免转化为整型再转化回字符串被截断
     * 1：表示扩展或者扩容中
     */
    @Value("${id.generator.version:0}")
    private long version;

    @Value("${id.generator.machineId}")
    private long machineId;

    public long getEpoch() {
        return epoch;
    }

    public void setEpoch(long epoch) {
        this.epoch = epoch;
    }

    public long getGenMethod() {
        return genMethod;
    }

    public void setGenMethod(long genMethod) {
        this.genMethod = genMethod;
    }

    public long getType() {
        return type;
    }

    public void setType(long type) {
        this.type = type;
    }

    public long getVersion() {
        return version;
    }

    public void setVersion(long version) {
        this.version = version;
    }

    public long getMachineId() {
        return machineId;
    }

    public void setMachineId(long machineId) {
        this.machineId = machineId;
    }
}
