package com.intelligence.edge.controller;

import com.intelligence.edge.config.CarConfig;
import com.intelligence.edge.data.CarTempData;
import com.intelligence.edge.service.CarControlService;
import com.intelligence.edge.service.CarNetService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author shik2
 * @date 2020/07/02
 **/
@RestController
@CrossOrigin
@RequestMapping("/control/car")
@Slf4j(topic = "c.CarNetController")
public class CarControlController {
    @Autowired
    private CarControlService carControlService;

    @Autowired
    private CarNetService carServer;

    @Autowired
    private CarConfig carConfig;


    @RequestMapping("/disConnect")
    public int disConnect(@RequestParam("carID") String carID) {
        String carIP = CarTempData.carIP.get(carID);
        if (!carServer.ping(carIP)) {
            log.info("设备离线：" + carID);
            return -1;
        }
        carControlService.closeConnection(carID);
        return 1;
    }

    @RequestMapping("/send")
    public int control(@RequestParam("carID") String carID,@RequestParam("instruction") String instruction) {
        String carIP = CarTempData.carIP.get(carID);
        /*if (!carServer.ping(carIP)) {
            log.info("设备离线：" + carID);
            return -1;
        }*/
        if(CarTempData.carState.get(carID)==0){
            log.info("设备离线：" + carID);
            return 0;
        }
        carControlService.control(carID,instruction);
        return 1;
    }
}