package com.freeing.id.core.bean;

/**
 * Id 数据结构模型
 * -----------------------------------------------------------
 * | version(1) | type(1) | genMethod(2) | time | seq | machine |
 * -----------------------------------------------------------
 */
public class IdMeta {
    /**
     * 机器ID所占位数
     */
    private byte machineBit;

    /**
     * 序列号所占位数
     */
    private byte seqBit;

    /**
     * 秒级时间/毫秒级时间所占位数
     */
    private byte timeBit;

    /**
     * 生成方式所占位数，如 嵌入发布模式、中心服务器发布模式、REST发布模式等等方式生成
     */
    private byte genMethodBit;

    /**
     * ID类型所占位数
     * @see com.freeing.id.core.enums.IdType
     */
    private byte typeBit;

    /**
     * 版本所占位数，用来做扩展位或者扩容时候的临时方案
     * 0：默认值，以免转化为整型再转化回字符串被截断
     * 1：表示扩展或者扩容中
     */
    private byte versionBit;

    public IdMeta(byte machineBit, byte seqBit, byte timeBit, byte genMethodBit, byte typeBit, byte versionBit) {
        this.machineBit = machineBit;
        this.seqBit = seqBit;
        this.timeBit = timeBit;
        this.genMethodBit = genMethodBit;
        this.typeBit = typeBit;
        this.versionBit = versionBit;
    }

    /**
     * 获取其在64位二进制中的开始位置
     * @return
     */
    public long getSeqBitStartPos() {
        return machineBit;
    }

    public long getTimeBitStartPos() {
        return machineBit + seqBit;
    }

    public long getGenMethodBitStartPos() {
        return machineBit + seqBit + timeBit;
    }

    public long getTypeBitStartPos() {
        return machineBit + seqBit + timeBit + genMethodBit;
    }

    public long getVersionBitStartPos() {
        return machineBit + seqBit + timeBit + genMethodBit + typeBit;
    }
    
    /**
     * 获取操作码
     * -1L 64位二进制位：
     * 1111111111111111111111111111111111111111111111111111111111111111
     * 左移 machineBit 如 10
     * 1111111111111111111111111111111111111111111111111111110000000000
     * 取反操作
     * 000000000000000000000000000000000000000000000000000000111111111
     * @return原码
     */
    public long getMachineBitMask() {
        return ~(-1L << machineBit);
    }

    public long getSeqBitMask() {
        return ~(-1L << seqBit);
    }

    public long getTimeBitMask() {
        return ~(-1L << timeBit);
    }

    public long getGenMethodBitMask() {
        return ~(-1L << genMethodBit);
    }

    public long getTypeBitMask() {
        return ~(-1L << typeBit);
    }

    public long getVersionBitMask() {
        return ~(-1L << versionBit);
    }

    public byte getMachineBit() {
        return machineBit;
    }

    public void setMachineBit(byte machineBit) {
        this.machineBit = machineBit;
    }

    public byte getSeqBit() {
        return seqBit;
    }

    public void setSeqBit(byte seqBit) {
        this.seqBit = seqBit;
    }

    public byte getTimeBit() {
        return timeBit;
    }

    public void setTimeBit(byte timeBit) {
        this.timeBit = timeBit;
    }

    public byte getGenMethodBit() {
        return genMethodBit;
    }

    public void setGenMethodBit(byte genMethodBit) {
        this.genMethodBit = genMethodBit;
    }

    public byte getTypeBit() {
        return typeBit;
    }

    public void setTypeBit(byte typeBit) {
        this.typeBit = typeBit;
    }

    public byte getVersionBit() {
        return versionBit;
    }

    public void setVersionBit(byte versionBit) {
        this.versionBit = versionBit;
    }

    @Override
    public String toString() {
        return "IdMeta{" +
            "machineBit=" + machineBit +
            ", seqBit=" + seqBit +
            ", timeBit=" + timeBit +
            ", genMethodBit=" + genMethodBit +
            ", typeBit=" + typeBit +
            ", versionBit=" + versionBit +
            '}';
    }
}
