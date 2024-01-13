package com.freeing.id.service.impl;

import com.freeing.id.core.bean.Id;
import com.freeing.id.core.enums.IdType;
import com.freeing.id.service.AbstractIdService;
import com.freeing.id.service.IdService;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * ID 服务 Lock 锁的安全实现
 */
public class IdServiceLockImpl extends AbstractIdService implements IdService {
    /**
     * 秒级或毫秒级的序号
     */
    private long sequence = 0;

    /**
     * 最后一次时间戳
     */
    private long lastTimestamp = -1;

    private final Lock lock = new ReentrantLock();

    public IdServiceLockImpl() {

    }

    public IdServiceLockImpl(IdType type) {
        super(type);
    }

    @Override
    protected void waitUntilPopulateId(Id id) {
        lock.lock();
        try {
            long timestamp = this.genTime();
            // 在同一个毫秒或秒内生成下一个唯一性id
            if (timestamp == lastTimestamp) {
                sequence++;
                // idMeta.getSeqBitMask() = 00...00 11...11 也即序号的最大值
                sequence &= idMeta.getSeqBitMask();
                // 已经超过达秒级或毫秒级内所能产生的最大id，需要等待下一秒或毫秒
                if (sequence == 0) {
                    timestamp = waitUntilNextTimeUnit();
                    // sequence = 0，sequence 已经是 0 无需在置 0
                }
            }
            // 下一秒或毫秒
            else {
                lastTimestamp = timestamp;
                // sequence 当前还是上一秒或毫秒的，可以置 0
                sequence = 0;
            }
            id.setSeq(sequence);
            id.setTime(timestamp);
        } finally {
            lock.unlock();
        }
    }

    /**
     * 校验是否时间回拨
     * @param lastTimestamp 最后生成ID的时间戳
     * @param timestamp 生成下一个id的时间戳
     */
    private void validateTimestamp(long lastTimestamp, long timestamp) {
        if (timestamp < lastTimestamp) {
            logger.error("Clock moved backwards.  Refusing to generate id for {} {}}.",
                lastTimestamp - timestamp,
                idType == IdType.MAX_PEAK ? "second" : "milisecond");
            throw new IllegalStateException(
                String.format(
                    "Clock moved backwards.  Refusing to generate id for %d %s.",
                    lastTimestamp - timestamp,
                    idType == IdType.MAX_PEAK ? "second" : "milisecond"));
        }
    }

    protected long waitUntilNextTimeUnit() {
        if (logger.isDebugEnabled())
            logger.info(String.format("Ids are used out during %d in machine %d. Waiting till next %s.",
                    lastTimestamp, machineId,
                    idType == IdType.MAX_PEAK ? "second" : "milisecond"));

        long timestamp = this.genTime();
        // 循环等待
        while (timestamp <= lastTimestamp) {
            //Thread.yield();
            timestamp = this.genTime();
        }

        if (logger.isDebugEnabled())
            logger.info(String.format("Next %s %d is up.",
                idType == IdType.MAX_PEAK ? "second" : "milisecond",
                timestamp));

        return timestamp;
    }
}
