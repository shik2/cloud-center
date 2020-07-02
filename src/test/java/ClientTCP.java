import java.io.*;
import java.net.Socket;

/**
 * @author shik2
 * @date 2020/07/02
 **/
public class ClientTCP {
    public static void main(String[] args) throws IOException {
        // 1.创建 Socket ( ip , port ) , 确定连接到哪里.
        Socket socket = new Socket("localhost", 7001);
        // 2.通过Scoket,获取输出流对象
        DataOutputStream out = new DataOutputStream(socket.getOutputStream());
        DataInputStream in = new DataInputStream(socket
                .getInputStream());// 读取服务器端传过来信息的DataInputStream
        try {
            out.writeUTF("id=car1");//将客户端的信息传递给服务器
            while (true) {
                String accpet = in.readUTF();// 读取来自服务器的信息
                System.out.println(accpet);//输出来自服务器的信息
            }
        } catch (IOException e) {
        }finally {
            in.close();
            out.close();
            socket.close();
        }



    }
}
