package com.intelligence.edge.config;


import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * @Author shik2
 * @Date 2020/06/16 16:23
 * @Description 无人车的配置信息，application.properties配置文件中端口相关的信息注入到该配置类
 **/
@Data
@Configuration
@ConfigurationProperties(prefix = "data")
public class CarConfig {
    // 无人车环境数据接收端口map
    private Map<String, Integer> carENVPort = new HashMap<>();

    // 无人车视频数据接收端口map
    private Map<String, Integer> carVideoPort = new HashMap<>();

    // 无人车视频数据接收端口map
    private Map<String, String> carIP = new HashMap<>();

    // 无人车连接状态map
    private Map<String, Boolean> connectFlag = new HashMap<>();
}
