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

    public CarControlServer(String carID, int port) throws IOException {
        this.carID = carID;
        this.port = port;
    }

    @Autowired
    private CarBasicDataMapper carBasicDataMapper;

    public void openConnect() throws IOException {
//        log.info(carID + ":" + port);
        server = new ServerSocket(port);//创建ServerSocket类

        socketThread = new Thread(new Task(socket));
        socketThread.start();
        log.info("开启控制服务端："+carID);
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
                DataInputStream in = new DataInputStream(socket.getInputStream());// 读取客户端传过来信息的DataInputStream
                String regex = "/^id=.*/";
                while (true) {
                    String msg = in.readLine();// 读取来自客户端的信息
                    log.info("收到信息" + msg);
                    log.info(carID + "可连接");
                    CarTempData.carState.put(carID, 1);//表示设备已经在线
                    break;
                }
                log.info("设备状态修改完毕");
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }

    public void control(String instruction) {
        log.info("发送指令：" + instruction);
        try {
            out.write(instruction.getBytes());//将客户端的信息传递给服务器
        } catch (IOException e) {
            log.info("发送指令失败");
        }
    }

    public void reset() {
        try {
            server.close();
            out.close();
            CarTempData.carState.put(carID, 0);
            openConnect();
            log.info("------！");
        } catch (IOException e) {
            e.printStackTrace();
            log.info("关闭异常！");
        }
    }
}
