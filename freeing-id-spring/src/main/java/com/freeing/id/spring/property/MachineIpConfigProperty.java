package com.freeing.id.spring.property;

import org.springframework.beans.factory.annotation.Value;

import java.io.InputStream;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

/**
 * 机器IP属性配置类
 */
public class MachineIpConfigProperty {
    /**
     * ip 映射配置
     *  格式1 包资源：classpath:ip_mapper.properties
     *  格式2 外部资源 : ip_mapper.properties
     */
    @Value("${id.generator.ipFilePath}")
    private String path;

    public Map<String, Long> getIpMap()  {
        Properties properties = new Properties();
        InputStream in;
        try {
            if (path.startsWith("classpath:")) {
                String finalPath = path.replaceFirst("classpath:", "").trim();
                URL resource = this.getClass().getClassLoader().getResource(finalPath);
                assert resource != null;
                in = resource.openStream();

            } else {
                in = Files.newInputStream(Paths.get(path));
            }
            properties.load(in);
            HashMap<String, Long> map = new HashMap<>();
            properties.forEach((k, v) -> {
                map.put(k.toString(), Long.parseLong(v.toString()));
            });
            return map;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
