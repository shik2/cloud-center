package com.intelligence.edge.config;


import com.intelligence.edge.pojo.Position;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

/**
 * @Author shik2
 * @Date 2020/06/16 16:23
 * @Description 无人车的配置信息，application.properties配置文件中端口相关的信息注入到该配置类
 **/
@Data
@Configuration
public class CarConfig {
    // 无人车环境数据接收端口map
    private Map<String, Integer> carENVPort = new HashMap<>();

    // 无人车视频接收端口map
    private Map<String, Integer> carVideoPort = new HashMap<>();

    // 无人车控制服务端map
    private Map<String, Integer> carControlPort = new HashMap<>();
}
