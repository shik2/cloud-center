package com.intelligence.edge.runner;

import com.intelligence.edge.config.CarConfig;
import com.intelligence.edge.dao.CarBasicDataMapper;
import com.intelligence.edge.data.CarTempData;
import com.intelligence.edge.pojo.CarBasicData;
import com.intelligence.edge.server.CarControlServer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * @author shik2
 * @date 2020/06/28
 **/
@Component
@Slf4j(topic = "c.ResetConnect")
public class ResetConnect implements CommandLineRunner {

    @Override
    public void run(String... args) throws Exception {
        log.info("重置所有设备连接");
        // 开启所有车的监听服务端
        for (CarBasicData car : CarTempData.carList) {
            // 新建各车的移动控制服务端，并开启监听
            CarControlServer server = new CarControlServer(car.getCarID(), CarTempData.carControlPort.get(car.getCarID()));
            server.openConnect();
            // 移动控制服务端存入CarTempData
            CarTempData.ccsMap.put(server.getCarID(),server);
        }
    }
}

