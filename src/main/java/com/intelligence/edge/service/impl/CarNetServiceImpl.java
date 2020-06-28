package com.intelligence.edge.service.impl;

import com.intelligence.edge.config.CarConfig;
import com.intelligence.edge.dao.CarBasicDataMapper;
import com.intelligence.edge.pojo.CarBasicData;
import com.intelligence.edge.server.CarENVServer;
import com.intelligence.edge.server.CarVideoServer;
import com.intelligence.edge.service.CarNetService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

/**
 * @Author shik2
 * @Date 2020/06/16 15:51
 * @Description 无人车服务
 **/
@Service
@Slf4j(topic = "c.CarNetServiceImpl")
public class CarNetServiceImpl implements CarNetService {
    @Autowired
    private CarConfig carConfig;
    @Autowired
    private CarBasicDataMapper carBasicDataMapper;

    /**
     * 测试设备连通性
     *
     * @param host 设备ip地址
     */
    public int ping(String host) {
        try {
            InetAddress inetAddress = InetAddress.getByName(host);
            boolean reachable = inetAddress.isReachable(3 * 1000);
            if (reachable) {
                log.info("ping success. Host name: " + inetAddress.getHostName() + ", IP addr: " + inetAddress.getHostAddress());
                return 1;
            } else {
                log.info("ping failed.");
                return 0;
            }
        } catch (UnknownHostException e1) {
            e1.printStackTrace();
        } catch (IOException e2) {
            e2.printStackTrace();
        }
        return 0;
    }


    /**
     * 无人车的接收视频文件和环境数据的连接列表，每辆车对应一个基本数据接收端
     */
    List<CarENVServer> cenvList = new ArrayList<>();
    List<CarVideoServer> cvsList = new ArrayList<>();


    public int connect(String carID) {
        log.info("开始连接智能车 " + carID);
        // 判断是已经开启
        if (carBasicDataMapper.getConnectState(carID) == 1) {
            log.info("端口占用");
            return 0;
        } else {
            CarBasicData param = new CarBasicData();
            param.setState(1);
            param.setCarID(carID);
            log.info("修改对象"+ param);
            if (carBasicDataMapper.setConnectState(param) == 1) {
                receiveData(carID);
                receiveVideo(carID);
                return 1;
            }
            return 2;
        }
    }

    public int closeConnect(String carID){
        if (carBasicDataMapper.getConnectState(carID) == 0) {
            log.info(carID+"已经断开连接");
            return 0;
        } else {
            CarBasicData param = new CarBasicData();
            param.setState(0);
            param.setCarID(carID);
            if (carBasicDataMapper.setConnectState(param) == 1) {
                closeConnection(carID);
                closeVideo(carID);
                log.info(carID+"成功断开");
                return 1;
            }
            return 2;
        }
    }


    /**
     * 接收无人车的发送的数据报文
     *
     * @param carID
     */
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
