package com.freeing.id.core.converter;

import com.freeing.id.core.bean.Id;

/**
 *  ID转换器
 *  ID对象和long之间的转换
 */
public interface IdConverter {
    /**
     * Id 转换为 long
     *
     * @param id Id
     * @return Id
     */
    long convert(Id id);

    /**
     * long 转换为 Id
     *
     * @param id long
     * @return
     */
    Id convert(long id);
}
