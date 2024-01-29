package com.freeing.id.factory;

import com.freeing.id.core.enums.IdType;
import com.freeing.id.core.provider.MachineIdProvider;
import com.freeing.id.service.AbstractIdService;
import com.freeing.id.service.IdService;
import com.freeing.id.service.impl.IdServiceLockImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * ID 管理器, 管理所有业务场景的ID生成器, 不同的业务可能是可以存在相同的 ID
 */
public class IdServiceFactory {
    private static final Logger logger = LoggerFactory.getLogger(IdServiceFactory.class);

    /**
     * 一次能获取最大的ID数量
     */
    private static int MAX_ID_GET_NUM = 1024;

    private final Map<String /* 业务KEY */, IdService /* 业务KEY的ID生成器 */> ID_SERVICE_MAP = new ConcurrentHashMap<>();

    /**
     * 默认的ID生成器
     */
    private IdService defaultIdService;

    private long epoch;
    private long version;
    private long type;
    private long genMethod;
    private MachineIdProvider machineIdProvider;

    public IdServiceFactory(long epoch, long version, long type, long genMethod, MachineIdProvider machineIdProvider) {
        this.epoch = epoch;
        this.version = version;
        this.type = type;
        this.genMethod = genMethod;
        this.machineIdProvider = machineIdProvider;

        AbstractIdService defaultIdService = new IdServiceLockImpl(IdType.parse((int) type), machineIdProvider);
        defaultIdService.setEpoch(epoch);
        defaultIdService.setVersion(version);
        defaultIdService.setGenMethod(genMethod);
        this.defaultIdService = defaultIdService;
    }

    public void registry(String key) {
        if (ID_SERVICE_MAP.containsKey(key)) {
            logger.info("{} id generator service is existing already.", key);
            return;
        }
        AbstractIdService service = new IdServiceLockImpl(IdType.parse((int) type), machineIdProvider);
        service.setEpoch(epoch);
        service.setVersion(version);
        service.setGenMethod(genMethod);
        ID_SERVICE_MAP.put(key, service);
    }

    @Deprecated
    public void registry(String key, AbstractIdService idService) {
        if (ID_SERVICE_MAP.containsKey(key)) {
            logger.info("{} id generator service is existing already.", key);
            return;
        }
        ID_SERVICE_MAP.put(key, idService);
    }

    public IdService get() {
        return defaultIdService;
    }

    public IdService get(String bizKey) {
        return ID_SERVICE_MAP.get(bizKey);
    }
}
