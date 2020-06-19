package com.intelligence.edge.controller;

import com.intelligence.edge.config.CarConfig;
import com.intelligence.edge.service.CarNetService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

/**
 * @author shik2
 */
@RestController
@CrossOrigin
@RequestMapping("/data/car")
public class CarNetController {

    @Autowired
    private CarNetService carServer;

    @Autowired
    private CarConfig carConfig;

    /**
     * 传入设备id，开启对应无人车环境数据和视频数据的udp接收端
     *
     * @param carID
     * @return
     * @throws IOException
     */
    @RequestMapping("/connect")
    public String connect(@RequestParam("id") String carID){

        System.out.println("开始连接无人车 " + carID);
        // 判断是已经开启
        if (carConfig.getConnectFlag().get(carID)) {
            System.out.println("端口占用");
            return carID+" 已经连接";
        } else {
            carConfig.getConnectFlag().put(carID, true);
            carServer.receiveData(carID);
            carServer.receiveVideo(carID);
            return carID+" 连接成功";
        }
    }

    /**
     * 关闭对应id设备的数据接收端
     * @param carID
     * @return
     */
    @RequestMapping("/closeConnect")
    public String end(@RequestParam("id") String carID) {
        if (!carConfig.getConnectFlag().get(carID)) {
            System.out.println(carID+"已经断开连接");
            return carID+" 已经断开";
        } else {
            carConfig.getConnectFlag().put(carID, false);
            carServer.closeConnection(carID);
            carServer.closeVideo(carID);
            System.out.println(carID+"成功断开");
            return carID+" 成功断开";
        }
    }
}
