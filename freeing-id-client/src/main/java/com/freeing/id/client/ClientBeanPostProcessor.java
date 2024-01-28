package com.freeing.id.client;

import com.freeing.id.spring.property.IdGeneratorProperty;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;

//@Component
public class ClientBeanPostProcessor implements BeanPostProcessor {
    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        // 嵌入发布模式为 0
        if (bean instanceof IdGeneratorProperty) {
            IdGeneratorProperty property = (IdGeneratorProperty) bean;
            property.setGenMethod(0);
        }
        return BeanPostProcessor.super.postProcessAfterInitialization(bean, beanName);
    }
}
