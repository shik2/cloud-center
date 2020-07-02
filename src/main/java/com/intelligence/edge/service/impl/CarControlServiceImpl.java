package com.intelligence.edge.service.impl;

import com.intelligence.edge.config.CarConfig;
import com.intelligence.edge.data.CarTempData;
import com.intelligence.edge.server.CarControlClient;
import com.intelligence.edge.server.CarControlServer;
import com.intelligence.edge.service.CarControlService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author shik2
 * @date 2020/07/02
 **/
@Service
@Slf4j(topic = "c.CarControlServiceImpl")
public class CarControlServiceImpl implements CarControlService {
    @Autowired
    private CarConfig carConfig;
    /**
     * 无人车的控制端列表
     */

    @Override
    public void control(String carID,String instruction) {
        CarControlServer cc = null;
        for (CarControlServer client : CarTempData.ccsList) {
            if(client.getCarID().equals(carID)){
                cc = client;
                break;
            }
        }
        if (cc == null){
            log.info("无该车连接");
        }
        cc.control(instruction);
    }

    public void closeConnection(String carID) {
        for (CarControlServer client : CarTempData.ccsList) {
            if (client.getCarID().equals(carID)) {
                client.close();
                log.info("关闭已有车辆控制连接");
                break;
            }
        }
    }


}
