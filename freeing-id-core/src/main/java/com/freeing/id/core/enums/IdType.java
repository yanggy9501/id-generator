package com.freeing.id.core.enums;

/**
 * Id 生成类型
 * 根据时间的位数和序列号的位数, ID类型可分为最大峰值型和最小粒度型。
 * 最大峰值型能承受更大的峰值压力,但是粗略有序的粒度有点大,最小粒度型有较细致的粒度,但是每个毫秒能承受的理论峰值有限,
 * 同一毫秒如果有更多的请求产生,必须等到下一个毫秒再响应
 */
public enum IdType {
    /**
     * 最大峰值型: 采用秒极有序, 秒极时间占用30位, 序列号占用20位
     */
    MAX_PEAK(0, "max-peak"),

    /**
     * 最小粒度型: 采用毫秒级有序, 毫秒级时间占用40位, 序列号占用10位
     */
    MIN_GRANULARITY(1, "min-granularity"),
    ;

    private final int code;
    private final String name;

    IdType(Integer code, String name) {
        this.code = code;
        this.name = name;
    }

    public static IdType parse(String name) {
        if (MIN_GRANULARITY.name.equals(name)) {
            return MIN_GRANULARITY;
        }
        else if (MAX_PEAK.name.equals(name)) {
            return MAX_PEAK;
        }
        return null;
    }

    public static IdType parse(int type) {
        if (MIN_GRANULARITY.code == type) {
            return MIN_GRANULARITY;
        }
        else if (MAX_PEAK.code == type) {
            return MAX_PEAK;
        }
        return null;
    }

    public String getName() {
        return name;
    }

    public int getCode() {
        return code;
    }
}
