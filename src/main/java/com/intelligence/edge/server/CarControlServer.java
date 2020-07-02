package com.intelligence.edge.server;

import com.intelligence.edge.dao.CarBasicDataMapper;
import com.intelligence.edge.data.CarTempData;
import com.intelligence.edge.pojo.CarBasicData;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.DatagramSocket;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * @author shik2
 * @date 2020/07/02
 **/
@Data
@Slf4j(topic = "c.CarControlServer")
public class CarControlServer {
    private String carID;
    private int port;
    private ServerSocket server;
    private Socket socket;
    private Thread socketThread;
    private DataOutputStream out;
    private DataInputStream in;

    public CarControlServer(String carID, int port) throws IOException {
        this.carID = carID;
        this.port = port;
    }

    @Autowired
    private CarBasicDataMapper carBasicDataMapper;

    public void openConnect() throws IOException {
        log.info(carID + ":" + port);
        server = new ServerSocket(port);//创建  ServerSocket类

//        in = new DataInputStream(socket.getInputStream());// 读取客户端传过来信息的DataInputStream

        socketThread = new Thread(new Task(socket));
        socketThread.start();
    }

    class Task implements Runnable {

        private Socket socket;

        public Task(Socket socket) {
            this.socket = socket;
        }

        @Override
        public void run() {
            try {
                socket = server.accept();// 等待客户连接
                out = new DataOutputStream(socket.getOutputStream());
                System.out.println("22222");
                DataInputStream in = new DataInputStream(socket.getInputStream());// 读取客户端传过来信息的DataInputStream
                String regex = "/^id=.*/";
                while (true) {
                    String accpet = in.readUTF();// 读取来自客户端的信息
                    log.info("收到信息" + accpet);//输出来自客户端的信息
                    CarTempData.carState.put(carID, 1);
                    break;
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }


    public void openConnect2() throws IOException {
        log.info(carID + ":" + port);
        server = new ServerSocket(port);//创建  ServerSocket类
        socket = server.accept();// 等待客户连接
        out = new DataOutputStream(socket.getOutputStream());
        in = new DataInputStream(socket.getInputStream());// 读取客户端传过来信息的DataInputStream
        String regex = "/^id=.*/";
        while (true) {
            String accpet = in.readUTF();// 读取来自客户端的信息
            log.info("收到信息" + accpet);//输出来自客户端的信息
            CarTempData.carState.put(carID, 1);
            /*if(accpet.matches(regex)){
                log.info("匹配成功");
                if (carBasicDataMapper.getConnectState(carID) == 1) {
                    log.info("已经连接");
                }else {
                    CarBasicData param = new CarBasicData();
                    param.setState(1);
                    param.setCarID(carID);
                    carBasicDataMapper.setConnectState(param);
                    log.info("成功连接");
                }
                break;
            }*/
            break;
        }

    }

    public void control(String instruction) {
        log.info("发送指令：" + instruction);
        try {
            out.writeUTF("客户端：" + instruction);//将客户端的信息传递给服务器
        } catch (IOException e) {
            log.info("发送指令失败");
        }
    }

    public void close() {
        try {
            socket.close();
            server.close();
            in.close();
            out.close();
        } catch (IOException e) {
            log.info("关闭异常！");
        }
    }
}
