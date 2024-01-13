package com.freeing.id.manager;

import com.freeing.id.core.bean.Id;
import com.freeing.id.service.AbstractIdService;
import com.freeing.id.service.IdService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Date;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * ID 管理器, 管理所有业务场景的ID生成
 */
public class IdManager {
    private static final Logger logger = LoggerFactory.getLogger(IdManager.class);

    private final Map<String /* 业务KEY */, IdService /* 业务KEY的ID生成器 */> ID_SERVICE_MAP = new ConcurrentHashMap<>();
    /**
     * 默认的ID生成器
     */
    private final IdService defaultIdService;

    public IdManager(AbstractIdService defaultIdService) {
        this.defaultIdService = defaultIdService;
        if (!defaultIdService.isInitOK()) {
            defaultIdService.init();
        }
    }

    public void registry(String key, AbstractIdService idService) {
        if (ID_SERVICE_MAP.containsKey(key)) {
            logger.info("Id generator service of {} is updating", key);
        }
        if (!idService.isInitOK()) {
            idService.init();
        }
        ID_SERVICE_MAP.put(key, idService);
    }

    /**
     * 产生ID
     * 根据系统时间产生一个全局唯一的ID
     */
    public long nextId() {
        return defaultIdService.genId();
    }

    /**
     * 产生ID
     * 根据系统时间产生一个全局唯一的ID
     */
    public long nextId(String key) {
        if (ID_SERVICE_MAP.containsKey(key)) {
            return ID_SERVICE_MAP.get(key).genId();
        }
        logger.warn("Id service of {} is not existing.", key);
        throw new IllegalArgumentException("Id service is not existing of " + key);
    }

    /**
     * 反解ID
     * 对产生的ID进行反解
     */
    public Id expId(long id) {
        // 任何一个IdService 反解都是一样的
        return defaultIdService.expId(id);
    }

    /**
     * 翻译时间
     * 把长整形的时间转化成可读的格式
     * @param time long 类型时间戳
     * @return Date
     */
    public Date transTime(long time) {
        return defaultIdService.transTime(time);
    }

    /**
     * 制造ID
     * 通过给定的ID元素来制造ID
     */
    long makeId(long time, long seq) {
        return defaultIdService.makeId(time, seq);
    }

    /**
     * 制造ID 通过给定的ID元素来制造ID
     */
    public long makeId(long time, long seq, long machine) {
        return defaultIdService.makeId(time, seq, machine);
    }

    /**
     * 制造ID 通过给定的ID元素来制造ID
     */
    public long makeId(long genMethod, long time, long seq, long machine) {
        return defaultIdService.makeId(genMethod, time, seq, machine);
    }

    /**
     * 制造ID 通过给定的ID元素来制造ID
     */
    public long makeId(long type, long genMethod, long time, long seq, long machine) {
        return defaultIdService.makeId(type, genMethod, time, seq, machine);
    }

    /**
     * 制造ID 通过给定的ID元素来制造ID
     */
    public long makeId(long version, long type, long genMethod, long time, long seq, long machine) {
        return defaultIdService.makeId(version, type, genMethod, time, seq, machine);
    }
}
