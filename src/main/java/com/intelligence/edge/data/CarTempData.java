package com.intelligence.edge.data;

import com.intelligence.edge.dao.CarBasicDataMapper;
import com.intelligence.edge.pojo.CarBasicData;
import com.intelligence.edge.pojo.Position;
import com.intelligence.edge.server.CarControlServer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import sun.security.krb5.Config;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author shik2
 * @date 2020/06/29
 **/
@Component
public class CarTempData {
    // 位置
    public static Map<String, Position> carPos = new HashMap<>();
    // 基本信息
    public static List<CarBasicData> carList;
    // 智能车的移动控制服务端
    public static Map<String, CarControlServer> ccsMap = new HashMap<>();
    // 连接状态
    public static Map<String, Integer> carState = new HashMap<>();

    // 无人车控制服务端map
    public static Map<String, Integer> carControlPort = new HashMap<>();
    // 无人车环境数据接收端口map
    public static Map<String, Integer> carENVPort = new HashMap<>();
    // 无人车视频接收端口map
    public static Map<String, Integer> carVideoPort = new HashMap<>();


    @Autowired
    private CarBasicDataMapper carBasicDataMapper;


    /**
     * 初始化所有智能车的位置和连接状态
     */
    @PostConstruct
    public void init() {
        //加载无人车设备,配置端口信息
        carList = carBasicDataMapper.getAllCarBasicData();
        for (CarBasicData car : carList) {
            System.out.println(car);
            Position position = new Position(120.35, 30.32);
            carPos.put(car.getCarID(), position);
            carState.put(car.getCarID(), 0);  // 设备初始化为离线
            // 数据接收端状态置为0
            CarBasicData param = new CarBasicData();
            param.setState(0);
            param.setCarID(car.getCarID());
            carBasicDataMapper.setConnectState(param);
            carControlPort.put(car.getCarID(),car.getCPort());
            carENVPort.put(car.getCarID(),car.getEPort());
            carVideoPort.put(car.getCarID(),car.getVPort());
        }
    }

    @PreDestroy
    public void destroy() {
        //系统运行结束
    }

}
