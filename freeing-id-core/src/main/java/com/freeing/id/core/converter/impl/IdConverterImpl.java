package com.freeing.id.core.converter.impl;

import com.freeing.id.core.bean.Id;
import com.freeing.id.core.bean.IdMeta;
import com.freeing.id.core.bean.IdMetaFactory;
import com.freeing.id.core.converter.IdConverter;
import com.freeing.id.core.enums.IdType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * IdConverter 实现类
 */
public class IdConverterImpl implements IdConverter {
    private static final Logger logger = LoggerFactory.getLogger(IdConverter.class);

    @Override
    public long convert(Id id) {
        return doConvert(id);
    }

    @Override
    public Id convert(long id) {
        return doConvert(id);
    }

    protected long doConvert(Id id) {
        long result = 0;

        IdMeta idMeta = IdMetaFactory.getIdMeta(IdType.parse((int)id.getType()));
        if (idMeta == null) {
            logger.error("unsupported id type value " + id.getType());
            throw new IllegalArgumentException("Illegal id type " + id.getType());
        }
        // | 0~0 | 0~0 | 0~0 | 0~0 | 0~0 | machine  |
        result |= id.getMachine();
        // | 0~0 | 0~0 | 0~0 | 0~0 | seq | machine  |
        result |= id.getSeq() << idMeta.getSeqBitStartPos();
        // | 0~0 | 0~0 | 0~0 | time | seq | machine  |
        result |= id.getTime() << idMeta.getTimeBitStartPos();
        // | 0~0 | 0~0 | genMethod | time | seq | machine  |
        result |= id.getGenMethod() << idMeta.getGenMethodBitStartPos();
        // | version | type | genMethod | time | seq | machine  |
        result |= id.getType() << idMeta.getTypeBitStartPos();
        // | version | type| genMethod | time | seq | machine  |
        result |= id.getVersion() << idMeta.getVersionBitStartPos();

        return result;
    }

    protected Id doConvert(long id) {
        /*  获取 long 类型 64 位二进制第二位即是 type ID类型
         * 0(0|1)00 0000 0000 0000 0000 0000 0000 0000 0000 0000 0000 0000 0000 0000
         * ------------------------------------------------------
         * | version(1) | type(1) | genMethod | time | seq | machine  |
         * ------------------------------------------------------
         */

        // 0100000000000000000000000000000000000000000000000000000000000000
        long typeBMark = 1L << 62;
        int type = (typeBMark & id) != 0 ? 1 : 0;
        IdType idType = IdType.parse(type);
        IdMeta idMeta = IdMetaFactory.getIdMeta(idType);

        Id result = new Id();
        result.setMachine(id & idMeta.getMachineBitMask());
        result.setSeq((id >>> idMeta.getSeqBitStartPos()) & idMeta.getSeqBitMask());
        result.setTime((id >>> idMeta.getTimeBitStartPos()) & idMeta.getTimeBitMask());
        result.setGenMethod((id >>> idMeta.getGenMethodBitStartPos()) & idMeta.getGenMethodBitMask());
        result.setType((id >>> idMeta.getTypeBitStartPos()) & idMeta.getTypeBitMask());
        result.setVersion((id >>> idMeta.getVersionBitStartPos()) & idMeta.getVersionBitMask());

        return result;
    }
}
