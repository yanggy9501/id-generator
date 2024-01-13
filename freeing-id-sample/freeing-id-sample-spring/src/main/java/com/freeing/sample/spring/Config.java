package com.freeing.sample.spring;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

/**
 * @author yanggy
 */
@Configuration
@ComponentScan("com.freeing")
@PropertySource({"classpath:id.property"})
public class Config {

}
