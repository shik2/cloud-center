import java.io.DataInputStream; //导入 DataInputStream类
import java.io.DataOutputStream;//导入DataOutputStream类
import java.io.IOException;//导入IOException类
import java.net.ServerSocket;//导入ServerSocket类
import java.net.Socket; //导入Socket 类
import java.util.Scanner; //导入Scanner类

/**
 * 模拟qq聊天功能： 实现客户端与服务器（一对一）的聊天功能，客户端首先发起聊天，输入的内容在服务器端和客户端显示，
 * 然后服务器端也可以输入信息，同样信息也在客户端和服务器端显示
 */

// 服务器类
public class ChatServer {//ChatServer类
    private int port = 7777;// 默认服务器端口

    public ChatServer() {

    }

    // 创建指定端口的服务器
    public ChatServer(int port) {//构造方法
        this.port = port;//将方法参数赋值给类参数
    }

    // 提供服务
    public void service() {//创建service方法
        try {// 建立服务器连接
            ServerSocket server = new ServerSocket(port);//创建  ServerSocket类
            Socket socket = server.accept();// 等待客户连接
            try {
                DataInputStream in = new DataInputStream(socket
                        .getInputStream());// 读取客户端传过来信息的DataInputStream
                DataOutputStream out = new DataOutputStream(socket
                        .getOutputStream());// 向客户端发送信息的DataOutputStream
                Scanner scanner = new Scanner(System.in);//从键盘接受数据
                while (true) {
                    String accpet = in.readUTF();// 读取来自客户端的信息
                    System.out.println(accpet);//输出来自客户端的信息
                }
            } finally {// 建立连接失败的话不会执行socket.close();
                socket.close();//关闭连接
                server.close();//关闭
            }
        } catch (IOException e) {//捕获异常
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {//主程序方法
        new ChatServer().service();//调用 service方法
    }
}
