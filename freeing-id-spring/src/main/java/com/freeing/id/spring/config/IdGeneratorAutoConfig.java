package com.freeing.id.spring.config;

import com.freeing.id.core.enums.IdType;
import com.freeing.id.core.provider.MachineIdProvider;
import com.freeing.id.core.provider.impl.DefaultMachineIdProvider;
import com.freeing.id.core.provider.impl.IpConfigurableMachineIdProvider;
import com.freeing.id.core.provider.impl.PropertyMachineIdProvider;
import com.freeing.id.manager.IdManager;
import com.freeing.id.service.AbstractIdService;
import com.freeing.id.service.impl.IdServiceLockImpl;
import com.freeing.id.spring.property.IdGeneratorProperty;
import com.freeing.id.spring.property.MachineIpConfigProperty;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;

@Configuration
@ComponentScan("com.freeing.id.spring")
public class IdGeneratorAutoConfig {

    @Autowired
    IdGeneratorProperty idGeneratorProperty;

    @ConditionalOnProperty(name = "id.generator.provider", havingValue = "default")
    @ConditionalOnMissingBean(MachineIdProvider.class)
    @Bean
    public MachineIdProvider defaultMachineIdProvider() {
        return new DefaultMachineIdProvider();
    }

    @ConditionalOnProperty(name = "id.generator.provider", havingValue = "property")
    @ConditionalOnMissingBean(MachineIdProvider.class)
    @Bean
    public MachineIdProvider propertyMachineIdProvider() {
        return new PropertyMachineIdProvider(idGeneratorProperty.getMachineId());
    }

    @ConditionalOnProperty(name = "id.generator.provider", havingValue = "ip")
    @Bean
    public MachineIpConfigProperty machineIpConfigProperty() {
        return new MachineIpConfigProperty();
    }

    @ConditionalOnProperty(name = "id.generator.provider", havingValue = "ip")
    @ConditionalOnMissingBean(MachineIdProvider.class)
    @DependsOn({"machineIpConfigProperty"})
    @Bean
    public MachineIdProvider ipConfigurableMachineIdProvider(MachineIpConfigProperty machineIpConfigProperty) {
        return new IpConfigurableMachineIdProvider(machineIpConfigProperty.getIpMap());
    }

    @Bean
    @ConditionalOnBean(MachineIdProvider.class)
    public IdManager idManager(MachineIdProvider machineIdProvider) {
        AbstractIdService defaultIdService = new IdServiceLockImpl(IdType.parse((int)idGeneratorProperty.getType()),
                                                                    machineIdProvider);
        defaultIdService.setEpoch(idGeneratorProperty.getEpoch());
        defaultIdService.setVersion(idGeneratorProperty.getVersion());
        defaultIdService.setGenMethod(idGeneratorProperty.getGenMethod());
        return new IdManager(defaultIdService);
    }
}
