package com.intelligence.edge.server;

import com.intelligence.edge.dao.CarBasicDataMapper;
import com.intelligence.edge.data.CarTempData;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
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

    public CarControlServer(String carID, int port) {
        this.carID = carID;
        this.port = port;
    }

    @Autowired
    private CarBasicDataMapper carBasicDataMapper;

    public void openConnect() throws IOException {
        server = new ServerSocket(port);//创建ServerSocket类
        socketThread = new Thread(new Task(socket));
        socketThread.start();
        log.info("开启控制服务端：" + carID + "-" + port);
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
                    if(!carID.equals(msg)){
                        log.info("id与端口不匹配");
                        reset();
                    }else{
                        log.info(carID + " 控制服务端收到信息：" + msg);
                        CarTempData.carState.put(carID, 1);//表示设备已经在线
                        break;
                    }
                }
                log.info(carID+"设备状态修改完毕，可开启数据连接");
            } catch (IOException e) {
            }

        }
    }

    public void control(String instruction) {
        log.info(carID+"发送指令：" + instruction);
        try {
            out.write(instruction.getBytes());//将客户端的信息传递给服务器
        } catch (IOException e) {
            log.info("发送指令失败");
            try{
                server.close();
                CarTempData.carState.put(carID, 0);
                openConnect();
                log.info("重置"+carID+"-"+port+"控制服务端");
            }catch (IOException ex) {
                ex.printStackTrace();
                log.info("关闭异常！");
            }

        }
    }

    public void reset() {
        try {
            server.close();
            CarTempData.carState.put(carID, 0);
            openConnect();
            log.info("重置"+carID+"-"+port+"控制服务端");
        } catch (IOException e) {
            e.printStackTrace();
            log.info("关闭异常！");
        }
    }

    public void close() {
        try {
            server.close();
            log.info("关闭"+carID+"控制服务端");
        } catch (IOException e) {
            log.info("关闭异常！");
        }
    }

}
