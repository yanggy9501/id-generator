package com.freeing.id.spring.config;

import com.freeing.id.core.enums.IdType;
import com.freeing.id.core.provider.MachineIdProvider;
import com.freeing.id.core.provider.impl.DefaultMachineIdProvider;
import com.freeing.id.core.provider.impl.PropertyMachineIdProvider;
import com.freeing.id.manager.IdManager;
import com.freeing.id.service.impl.IdServiceLockImpl;
import com.freeing.id.spring.property.IdGeneratorProperty;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

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

    @Bean
    @ConditionalOnBean(MachineIdProvider.class)
    public IdManager idManager(MachineIdProvider machineIdProvider) {
        IdServiceLockImpl idService = new IdServiceLockImpl(IdType.parse((int)idGeneratorProperty.getType()));
        idService.setEpoch(idGeneratorProperty.getEpoch());
        idService.setVersion(idGeneratorProperty.getVersion());
        idService.setMachineIdProvider(machineIdProvider);
        idService.setGenMethod(idGeneratorProperty.getGenMethod());
        return new IdManager(idService);
    }
}
