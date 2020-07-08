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

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
    public boolean ping(String host) {
        String osName = System.getProperty("os.name");//获取操作系统类型
        String pingCommand = "";
        if (osName.contains("Linux")) {
            pingCommand = "ping -c 2 -i 0.7 " + host;
        } else {
            pingCommand = "ping " + host + " -n 2 -w 500";
        }

        BufferedReader in = null;
        Runtime r = Runtime.getRuntime();  // 将要执行的ping命令,此命令是windows格式的命令
        try {   // 执行命令并获取输出
            log.info(pingCommand);
            Process p = r.exec(pingCommand);
            if (p == null) {
                return false;
            }
            in = new BufferedReader(new InputStreamReader(p.getInputStream()));   // 逐行检查输出,计算类似出现=23ms TTL=62字样的次数
            int connectedCount = 0;
            String line = null;
            while ((line = in.readLine()) != null) {
                connectedCount += getCheckResult(line,osName);
            }   // 如果出现类似=23ms TTL=62这样的字样,出现的次数=测试次数则返回真
            return connectedCount == 2;
        } catch (Exception ex) {
            ex.printStackTrace();   // 出现异常则返回假
            return false;
        } finally {
            try {
                in.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private static int getCheckResult(String line,String osName) {
        Pattern pattern;
        if(osName.contains("Linux")){
            pattern = Pattern.compile("(ttl=\\d+)(\\s+time=)", Pattern.CASE_INSENSITIVE);
        }else{
            pattern = Pattern.compile("(\\d+ms)(\\s+)(TTL=\\d+)", Pattern.CASE_INSENSITIVE);
        }
        Matcher matcher = pattern.matcher(line);
        while (matcher.find()) {
            return 1;
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
//            log.info("修改对象" + param);
            if (carBasicDataMapper.setConnectState(param) == 1) {
                receiveData(carID);
                receiveVideo(carID);
                return 1;
            }
            return 2;
        }
    }

    public int closeConnect(String carID) {
        if (carBasicDataMapper.getConnectState(carID) == 0) {
            log.info(carID + "已经断开连接");
            return 0;
        } else {
            CarBasicData param = new CarBasicData();
            param.setState(0);
            param.setCarID(carID);
            if (carBasicDataMapper.setConnectState(param) == 1) {
                closeConnection(carID);
                closeVideo(carID);
                log.info(carID + "成功断开");
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
                    log.info("使用已有车辆数据连接");
                    flag = 1;
                    break;
                }
            }
            if (flag == 0) {
                useServer = new CarENVServer(carID, carConfig.getCarENVPort().get(carID));
                cenvList.add(useServer);
                log.info("创建新车辆数据连接");
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
                    log.info("使用已有车辆视频连接");
                    flag = 1;
                    break;
                }
            }
            if (flag == 0) {
                useServer = new CarVideoServer(carID, carConfig.getCarVideoPort().get(carID));
                cvsList.add(useServer);
                log.info("创建新车辆视频连接");
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
                log.info("关闭已有车辆数据连接");
                break;
            }
        }
    }

    public void closeVideo(String carID) {
        for (CarVideoServer cvs : cvsList) {
            if (cvs.getCarID().equals(carID)) {
                cvs.end();
//                cbsList.remove(cbs);
                log.info("关闭已有车辆视频连接");
                break;
            }
        }
    }
}
