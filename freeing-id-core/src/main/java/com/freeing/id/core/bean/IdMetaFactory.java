package com.freeing.id.core.bean;

import com.freeing.id.core.enums.IdType;

/**
 * IdMeta工厂类
 */
public class IdMetaFactory {
    private static final IdMeta MAX_PEAK = new IdMeta(
        (byte) 10, /*机器ID所占位数*/
        (byte) 20, /*序列号所占位数*/
        (byte) 30, /*时间序号所在位数*/
        (byte) 2, /*生成方式所占位数*/
        (byte) 1, /*ID类型所占位数*/
        (byte) 1 /*版本所占位数*/
    );

    private static final IdMeta MIN_GRANULARITY = new IdMeta(
        (byte) 10,
        (byte) 10,
        (byte) 40,
        (byte) 2,
        (byte) 1,
        (byte) 1);

    public static IdMeta getIdMeta(IdType type) {
        if (IdType.MAX_PEAK.equals(type)) {
            return MAX_PEAK;
        }
        else if (IdType.MIN_GRANULARITY.equals(type)) {
            return MIN_GRANULARITY;
        } else {
            throw new RuntimeException("unsupported IdType");
        }
    }
}
