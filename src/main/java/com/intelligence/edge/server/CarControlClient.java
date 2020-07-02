package com.intelligence.edge.server;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

/**
 * @author shik2
 * @date 2020/07/02
 **/
@Data
@Slf4j(topic = "c.CarControlClient")
public class CarControlClient {

    private String carID;
    private String host;
    private int port;
    private Socket socket;
    private Thread socketThread;
    private DataOutputStream out;

    public CarControlClient(String carID, String host,int port) throws IOException {
        this.carID = carID;
        this.host = host;
        this.port = port;

    }

    // 连接小车
    public void connect() throws IOException {
        log.info(host+port);
        this.socket = new Socket(host, port);
        out = new DataOutputStream(socket.getOutputStream());
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
            out.close();
        } catch (IOException e) {
            log.info("关闭异常！");
        }
    }

}

