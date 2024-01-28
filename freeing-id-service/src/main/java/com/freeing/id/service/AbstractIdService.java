package com.freeing.id.service;

import com.freeing.id.core.bean.Id;
import com.freeing.id.core.bean.IdMeta;
import com.freeing.id.core.bean.IdMetaFactory;
import com.freeing.id.core.converter.IdConverter;
import com.freeing.id.core.converter.impl.IdConverterImpl;
import com.freeing.id.core.enums.IdType;
import com.freeing.id.core.provider.MachineIdProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Date;

/**
 * 基础的id服务类
 * PS: epoch、genMethod、version 根据需要进行自定义配置
 */
public abstract class AbstractIdService implements IdService {
    protected final Logger logger = LoggerFactory.getLogger(this.getClass());

    /**
     * 初始化状态
     */
    protected boolean initOK = false;

    /**
     * 开始时间
     * 2015/1/1 0:0:0
     */
    public long epoch = 1420041600000L;
    protected long machineId = -1;
    protected long genMethod = 0;
    protected long type = 0;
    protected long version = 0;
    protected IdType idType;
    /** IdMeta 由 IdType决定，不提供setter方法 */
    protected IdMeta idMeta;
    protected IdConverter idConverter;
    protected MachineIdProvider machineIdProvider;

    public AbstractIdService(MachineIdProvider machineIdProvider) {
        this(IdType.MAX_PEAK, machineIdProvider);
    }

    public AbstractIdService(IdType type, MachineIdProvider machineIdProvider) {
        idType = type;
        idMeta = IdMetaFactory.getIdMeta(idType);
        this.machineIdProvider = machineIdProvider;
        init();
    }

    public void init() {
        this.machineId = machineIdProvider.getMachineId();
        if (machineId < 0 || machineId >= 1024) {
            logger.error("The machine ID is not configured less than 1024 and more than 0 so that id Service refuses to start.");
            throw new IllegalStateException(
                "The machine ID is not configured properly less than 1024 and more than 0 so that ID Service refuses to start.");
        }

        this.type = idType.getCode();
        setIdConverter(new IdConverterImpl());
        finishInit();
    }

    private void finishInit() {
        initOK = true;
    }

    @Override
    public long genId() {
        Id id = new Id();
        waitUntilPopulateId(id);
        id.setMachine(machineId);
        id.setGenMethod(genMethod);
        id.setType(type);
        id.setVersion(version);
        long result = idConverter.convert(id);
        if (logger.isTraceEnabled()) {
            logger.trace("genId ==> ID: {}, id: {}", id, result);
        }
        return result;
    }

    /**
     * populate seq and time for ID
     * @param id
     */
    protected abstract void waitUntilPopulateId(Id id);

    @Override
    public Id expId(long id) {
        return idConverter.convert(id);
    }

    @Override
    public Date transTime(long time) {
        if (idType == IdType.MAX_PEAK) {
            return new Date(time * 1000 + epoch);
        } else if (idType == IdType.MIN_GRANULARITY) {
            return new Date(time + epoch);
        }
        return null;
    }

    @Override
    public long makeId(long time, long seq) {
        Id id = new Id(this.machineId, seq, time, this.genMethod, this.type, this.version);
        return idConverter.convert(id);
    }

    @Override
    public long makeId(long time, long seq, long machine) {
        Id id = new Id(machine, seq, time, this.genMethod, this.type, this.version);
        return idConverter.convert(id);
    }

    @Override
    public long makeId(long genMethod, long time, long seq, long machine) {
        Id id = new Id(machine, seq, time, genMethod, this.type, this.version);
        return idConverter.convert(id);
    }

    @Override
    public long makeId(long type, long genMethod, long time, long seq, long machine) {
        Id id = new Id(machine, seq, time, genMethod, type, this.version);
        return idConverter.convert(id);
    }

    @Override
    public long makeId(long version, long type, long genMethod, long time, long seq, long machine) {
        Id id = new Id(machine, seq, time, genMethod, type, version);
        return idConverter.convert(id);
    }

    /**
     * 获取当前 timeBit 上的秒级或毫秒级时间戳
     * @return
     */
    protected long genTime() {
        if (idType == IdType.MAX_PEAK) {
            return (System.currentTimeMillis() - epoch) / 1000;
        }
        else if (idType == IdType.MIN_GRANULARITY) {
            return (System.currentTimeMillis() - epoch);
        }
        return (System.currentTimeMillis() - epoch) / 1000;
    }

    /* ------------------------------------ setter ----------------------------------- */

    public boolean isInitOK() {
        return initOK;
    }

    public void setEpoch(long epoch) {
        this.epoch = epoch;
    }

    public void setGenMethod(long genMethod) {
        this.genMethod = genMethod;
    }

    public void setVersion(long version) {
        this.version = version;
    }

    public void setIdType(IdType idType) {
        this.idType = idType;
    }

    public void setIdConverter(IdConverter idConverter) {
        this.idConverter = idConverter;
    }

    public void setMachineIdProvider(MachineIdProvider machineIdProvider) {
        this.machineIdProvider = machineIdProvider;
    }
}
