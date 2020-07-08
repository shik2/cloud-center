package com.intelligence.edge.server;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.io.*;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;

/**
 * 文件传输Server端
 * 功能说明： 负责接收无人车发送的视频文件
 */
@Data
@Slf4j(topic = "c.CarVideoServer")
public class CarVideoServer {

    private String carID;
    private int port;
    private Thread socketThread;
    private DatagramSocket socket;
    private volatile boolean runFlag = true;

    public CarVideoServer(String carID, int port) throws IOException {
        this.carID = carID;
        this.port = port;
    }

    /**
     * 使用线程处理每个客户端传输的文件
     *
     * @throws Exception
     */
    public void load() throws IOException {
        runFlag = true;
        socket = new DatagramSocket(port);
        // 每接收到一个Socket就建立一个新的线程来处理它
        socketThread = new Thread(new CarVideoServer.Task(socket));
        socketThread.start();

    }


    public void end() {
        socket.close();
        runFlag = false;
    }


    /**
     * 处理客户端传输过来的文件线程类
     */
    class Task implements Runnable {

        private DatagramSocket socket;
        private DataOutputStream dos = null;

        public Task(DatagramSocket socket) {
            this.socket = socket;
        }

        @Override
        public void run() {

            byte[] buf = new byte[4096];
            DataOutputStream fileOut = null;//文件输出流
            String fileName = null;//接受的文件名
            String fileDir = "D:\\FTCache\\";

            try {
                DatagramPacket packet = new DatagramPacket(buf, buf.length);
                log.info(carID+" 视频数据接收端打开：");
                while (runFlag) {

                    // 本次文件传输的信息
                    long passedlen = 0;         //传输获得的大小
                    long len = 0;               //发送文件的大小
                    int readSize = 0;           //每次读取数据的大小

                    socket.receive(packet);
                    // 本地保存路径，文件名会自动从服务器端继承而来。
                    // 获取文件名
                    fileName = new String(buf).trim();
                    String fileDir_tmp = fileDir + carID;
                    File file = new File(fileDir_tmp);
                    if (!file.exists()) {
                        file.mkdir();
                    }
                    String filePath = fileDir_tmp + "\\" + fileName;
                    socket.receive(packet);
                    System.out.println("buf文件的路径" + filePath + "\n");
                    file = new File(filePath);

                    if (!file.exists()) {
                        file.createNewFile();
                    }

                    fileOut = new DataOutputStream(new BufferedOutputStream(new FileOutputStream(file)));

                    len = Long.parseLong(new String(buf, 0, packet.getLength()));

                    System.out.println(len);

                    System.out.println("开始接收文件!" + "\n");
                    socket.receive(packet);
                    while ((readSize = packet.getLength()) != 0) {

                        passedlen += readSize;
//                        System.out.println("文件接收了" + (passedlen * 100 / len) + "%\n");
                        fileOut.write(buf, 0, readSize);
                        fileOut.flush();
                        socket.receive(packet);
                    }
                    System.out.println("over!" + "\n");
                    if (passedlen != len) {
                        System.out.printf("IP:%s发来的%s传输过程有损失\n", packet.getAddress(), fileName);
                        file.delete();//将缺损文件删除
                    } else
                        System.out.println("接收完成，文件存为" + filePath + "\n");
                    if (fileOut != null)
                        fileOut.close();

                }
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (SocketException e) {
                return;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

}
