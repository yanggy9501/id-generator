package com.freeing.id.core.provider.impl;

import com.freeing.id.core.provider.MachineIdProvider;
import com.freeing.id.core.util.IpUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

/**
 * 基于IP地址的机器ID提供器
 */
public class IpConfigurableMachineIdProvider implements MachineIdProvider {
    private static final Logger logger = LoggerFactory.getLogger(IpConfigurableMachineIdProvider.class);

    /**
     * 本机ID
     */
    private long machineId;

    /**
     * 本机IP
     */
    private String localhost;

    /**
     * IP 映射机器ID
     */
    private Map<String /* localhost */, Long /* machineId */> ipsMapper;

    /**
     * 根据 ip 在数据中的位置决定机器ID, 顺序不同同一个机器的机器ID也会不同
     *
     * @param ipArray
     */
    @Deprecated
    public IpConfigurableMachineIdProvider(String[] ipArray) {
        if (ipArray == null || ipArray.length == 0 || ipArray.length >= 1024) {
            throw new IllegalArgumentException("ip array size should be more than 0 and less than 1024.");
        }
        ipsMapper = new HashMap<>(ipArray.length);
        for (int i = 0; i < ipArray.length; i++) {
            ipsMapper.put(ipArray[i], (long) i);
        }
    }

    /**
     * 外界规定映射，可以保证同一个机器的机器ID不变
     *
     * @param ipsMapper
     */
    public IpConfigurableMachineIdProvider(Map<String /* localhost */, Long /* machineId */> ipsMapper) {
        if (ipsMapper == null || ipsMapper.size() == 0 || ipsMapper.size() >= 1024) {
            throw new IllegalArgumentException("ip size should be more than 0 and less than 1024.");
        }
        this.ipsMapper = new HashMap<>(ipsMapper);
        init();
    }

    @Override

    public long getMachineId() {
        return machineId;
    }

    public void init() {
        String localhost = IpUtils.getHostIp();
        if (localhost == null || localhost.isEmpty()) {
            String msg = "Fail to get host IP address. Stop to initialize the IpConfigurableMachineIdProvider provider.";
            logger.error(msg);
            throw new IllegalStateException(msg);
        }

        if (!ipsMapper.containsKey(localhost)) {
            String msg = String.format("Fail to configure ID for host IP address %s. " +
                "Stop to initialize the IpConfigurableMachineIdProvider provider.", localhost);
            logger.error(msg);
            throw new IllegalStateException(msg);
        }
        machineId = ipsMapper.get(localhost);
        logger.info("IpConfigurableMachineIdProvider.init ip {} id {}", localhost, machineId);
    }
}
