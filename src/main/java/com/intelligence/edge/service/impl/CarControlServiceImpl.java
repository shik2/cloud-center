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
        CarControlServer cc = CarTempData.ccsMap.get(carID);
        cc.control(instruction);
    }

    @Override
    public void reset(String carID) {
        CarControlServer cc = CarTempData.ccsMap.get(carID);
        cc.reset();
    }

    /*public void closeConnection(String carID) {
        CarControlServer cc = CarTempData.ccsMap.get(carID);
        cc.close();
    }*/


}
