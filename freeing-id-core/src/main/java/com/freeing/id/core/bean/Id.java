package com.freeing.id.core.bean;

import java.io.Serializable;

/**
 * ID的数据
 */
public class Id implements Serializable {
    private static final long serialVersionUID = -1L;

    /**
     * 机器ID
     * 10位,最多支持1000+个服务器
     */
    private long machine;

    /**
     * 序列号
     * 最大峰值型: 20
     * 最小粒度型: 10
     */
    private long seq;

    /**
     * 秒级时间/毫秒级时间
     * 最大峰值型: 30
     * 最小粒度型: 40
     */
    private long time;

    /**
     * 生成方式
     * 2位,用来区分三种发布模式: 嵌入发布模式，中心服务器发布模式，REST发布模式.
     * 00：嵌入发布模式 01：中心服务器发布模式 02：REST发布模式 03：保留未用
     */
    private long genMethod;

    /**
     * ID类型
     * 1位，用来区分两种ID类型:最大峰值型和最小粒度型
     * 0：最大峰值型 1：最小粒度型
     */
    private long type;

    /**
     * 版本
     * 1位，用来做扩展位或者扩容时候的临时方案。
     * 0：默认值，以免转化为整型再转化回字符串被截断
     * 1：表示扩展或者扩容中
     */
    private long version;

    public Id() {
    }

    public Id(long machine, long seq, long time, long genMethod, long type, long version) {
        this.machine = machine;
        this.seq = seq;
        this.time = time;
        this.genMethod = genMethod;
        this.type = type;
        this.version = version;
    }

    public long getMachine() {
        return machine;
    }

    public void setMachine(long machine) {
        this.machine = machine;
    }

    public long getSeq() {
        return seq;
    }

    public void setSeq(long seq) {
        this.seq = seq;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
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

    @Override
    public String toString() {
        return "Id{" +
            "machine=" + machine +
            ", seq=" + seq +
            ", time=" + time +
            ", genMethod=" + genMethod +
            ", type=" + type +
            ", version=" + version +
            '}';
    }
}
