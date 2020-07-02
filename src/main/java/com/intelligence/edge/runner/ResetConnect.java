package com.intelligence.edge.runner;

import com.intelligence.edge.config.CarConfig;
import com.intelligence.edge.dao.CarBasicDataMapper;
import com.intelligence.edge.data.CarTempData;
import com.intelligence.edge.pojo.CarBasicData;
import com.intelligence.edge.server.CarControlServer;
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
public class ResetConnect implements CommandLineRunner {

    @Autowired
    private CarBasicDataMapper carBasicDataMapper;

    @Autowired
    private CarConfig carConfig;

    @Override
    public void run(String... args) throws Exception {
        System.out.println("重置连接");
        carBasicDataMapper.resetState();
        // 开启所有车的监听服务端
        for (CarBasicData carBasicData : CarTempData.carList) {
            CarControlServer server = new CarControlServer(carBasicData.getCarID(), carConfig.getCarControlPort().get(carBasicData.getCarID()));
            server.openConnect();
            CarTempData.ccsList.add(server);
            System.out.println("+1");
        }
    }
}
