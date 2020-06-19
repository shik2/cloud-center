package com.intelligence.edge.service.impl;

import com.intelligence.edge.config.CarConfig;
import com.intelligence.edge.server.CarENVServer;
import com.intelligence.edge.server.CarVideoServer;
import com.intelligence.edge.service.CarNetService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * @Author shik2
 * @Date 2020/06/16 15:51
 * @Description 无人车服务
 **/
@Service
public class CarNetServiceImpl implements CarNetService {
    @Autowired
    private CarConfig carConfig;

    /**
     * 无人车的接收视频文件和环境数据的连接列表，每辆车对应一个基本数据接收端
     */
    List<CarENVServer> cenvList = new ArrayList<>();
    List<CarVideoServer> cvsList = new ArrayList<>();

    /**
     * 接收无人车的发送的数据报文
     *
     * @param carID
     */
    @Override
    public void receiveData(String carID) {

        try {
            int flag = 0;
            CarENVServer useServer = null;
            for (CarENVServer cbs : cenvList) {
                if (cbs.getCarID().equals(carID)) {
                    useServer = cbs;
                    System.out.println("使用已有车辆数据连接");
                    flag = 1;
                    break;
                }
            }
            if (flag == 0) {
                useServer = new CarENVServer(carID, carConfig.getCarENVPort().get(carID));
                cenvList.add(useServer);
                System.out.println("创建新车辆数据连接");
            }
            useServer.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 接收无人车的发送的视频文件
     *
     * @param carID
     */
    public void receiveVideo(String carID) {

        try {
            int flag = 0;
            CarVideoServer useServer = null;
            for (CarVideoServer cvs : cvsList) {
                if (cvs.getCarID().equals(carID)) {
                    useServer = cvs;
                    System.out.println("使用已有车辆视频连接");
                    flag = 1;
                    break;
                }
            }
            if (flag == 0) {
                useServer = new CarVideoServer(carID, carConfig.getCarVideoPort().get(carID));
                cvsList.add(useServer);
                System.out.println("创建新车辆视频连接");
            }
            useServer.load();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void closeConnection(String carID) {
        for (CarENVServer cbs : cenvList) {
            if (cbs.getCarID().equals(carID)) {
                cbs.end();
//                cbsList.remove(cbs);
                System.out.println("关闭已有车辆数据连接");
                break;
            }
        }
    }

    @Override
    public void closeVideo(String carID) {
        for (CarVideoServer cvs : cvsList) {
            if (cvs.getCarID().equals(carID)) {
                cvs.end();
//                cbsList.remove(cbs);
                System.out.println("关闭已有车辆视频连接");
                break;
            }
        }
    }
}
