package com.freeing.id.service;

import com.freeing.id.core.bean.Id;

import java.util.Date;

/**
 * ID 生成器接口服务
 */
public interface IdService {
    /**
     * 产生ID
     * 根据系统时间产生一个全局唯一的ID
     */
    long genId();

    /**
     * 反解ID
     * 对产生的ID进行反解
     */
    Id expId(long id);

    /**
     * 翻译时间
     * 把长整形的时间转化成可读的格式
     * @param time
     * @return
     */
    Date transTime(long time);

    /**
     * 制造ID
     * 通过给定的ID元素来制造ID
     */
    long makeId(long time, long seq);

    /**
     * 制造ID 通过给定的ID元素来制造ID
     */
    long makeId(long time, long seq, long machine);

    /**
     * 制造ID 通过给定的ID元素来制造ID
     */
    long makeId(long genMethod, long time, long seq, long machine);

    /**
     * 制造ID 通过给定的ID元素来制造ID
     */
    long makeId(long type, long genMethod, long time, long seq, long machine);

    /**
     * 制造ID 通过给定的ID元素来制造ID
     */
    long makeId(long version, long type, long genMethod, long time, long seq, long machine);
}
