package com.intelligence.edge.server;

import com.google.gson.Gson;
import com.intelligence.edge.dao.CarBasicDataMapper;
import com.intelligence.edge.dao.CarENVDataMapper;
import com.intelligence.edge.pojo.EnvironmentInfo;
import com.intelligence.edge.utils.SpringUtils;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;

/**
 * 环境数据传输Server端
 * 功能说明： 负责接收无人车发送的环境数据（经纬度、速度、温度、湿度...）,存入MMySQL数据库中
 */
@Data
public class CarENVServer {
    private String carID;
    private int port;
    private Thread socketThread;
    private DatagramSocket socket;
    private volatile boolean runFlag = true;

    private ApplicationContext applicationContext = SpringUtils.getApplicationContext();

    private CarENVDataMapper carENVDataMapper = applicationContext.getBean(CarENVDataMapper.class);




    public CarENVServer(String carID, int port) throws SocketException {
        this.carID = carID;
        this.port = port;
    }

    /**
     * 开启数据接收服务端
     * @throws SocketException
     */
    public void start() throws SocketException {

        runFlag = true;
        socket = new DatagramSocket(port);
        // 每接收到一个Socket就建立一个新的线程来处理它
        socketThread = new Thread(new Task(socket));
        socketThread.start();
    }

    /**
     * 关闭数据接收服务端
     */
    public void end() {
        socket.close();
        runFlag = false;    //通过改变标志位让线程退出
    }


    class Task implements Runnable {

        private DatagramSocket socket;

        public Task(DatagramSocket socket) {
            this.socket = socket;
        }

        @Override
        public void run() {

            byte[] buf = new byte[2048];
            try {
                DatagramPacket packet = new DatagramPacket(buf, buf.length);
                System.out.println("server is on，waiting for client to send data......");
                while (runFlag) {
                    socket.receive(packet);
                    System.out.println(carID + " server received data from client：");
                    String strReceive =
                            new String(packet.getData(), 0,
                                    packet.getLength()) +
                                    " from " +
                                    packet.getAddress().getHostAddress() +
                                    ":" + packet.getPort();
                    System.out.println(strReceive);

                    String objStr = new String(packet.getData(), 0,
                            packet.getLength());

                    // 将收到的对象字符串转成对象实例
                    Gson gson = new Gson();
                    EnvironmentInfo ei = gson.fromJson(objStr, EnvironmentInfo.class);
                    ei.setCarID(carID);
                    System.out.println(ei);

                    // 将解析的环境数据对象存入到数据库中
                    carENVDataMapper.insertCarENVData(ei);

                    //由于dp_receive在接收了数据之后，其内部消息长度值会变为实际接收的消息的字节数，
                    //所以这里要将dp_receive的内部消息长度重新置为1024
                    packet.setLength(buf.length);

                }
            } catch (SocketException e) {
                return;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

}
