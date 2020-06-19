import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.InetSocketAddress;


public class UDPClient {
    // 上传的文件路径，可在main函数中修改
    private String filePath;
    // socket服务器地址和端口号
    private String host;
    private int port;

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public static void main(String[] args) {
        UDPClient client = new UDPClient();
        client.setHost("127.0.0.1");
        client.setPort(9898);
        client.setFilePath("E:\\");
        client.uploadFile("demo.mp4");
    }

    /**
     * 客户端文件上传
     * @param fileName 文件名
     */
    public void uploadFile(String fileName) {

        DatagramSocket dsk = null;
        DatagramPacket dpk = null;
        DataInputStream fis = null;
        try {
            dsk = new DatagramSocket(port, InetAddress.getByName(host));
            int bufferSize = 4096;
            byte[] buf = new byte[bufferSize];
            dpk = new DatagramPacket(buf, buf.length,new InetSocketAddress(InetAddress.getByName(host), 9002));
            // 选择进行传输的文件
            File fi = new File(filePath + fileName);
            System.out.println("文件长度:" + (int) fi.length());

            fis = new DataInputStream(new FileInputStream(filePath + fileName));
            buf = fi.getName().getBytes();
            dpk.setData(buf, 0, fileName.length());
            dsk.send(dpk);      // 发送文件名

            String fileLen = Long.toString((long) fi.length());

            buf = fileLen.getBytes();
            System.out.println("buf文件长度"+new String(buf));

            dpk.setData(buf, 0, fileLen.length());
            dsk.send(dpk);

            while (true) {
                int read = 0;
                if (fis != null) {
                    read = fis.read(buf);
                }

                if (read == -1) {
                    break;
                }
                dpk.setData(buf, 0, read);
                dsk.send(dpk);
            }
            //给服务器发布一个终止信号
            dpk.setData(buf, 0, 0);
            dsk.send(dpk);
            System.out.println("文件传输完成");
        } catch (Exception e) {
            System.out.println("服务器"+host+":"+port+"失去连接");
            e.printStackTrace();
        }finally{
            try{
                if(fis!=null)
                    fis.close();
                if(dsk != null)
                    dsk.close();
            }catch(IOException e){
                e.printStackTrace();
            }
        }
    }
}