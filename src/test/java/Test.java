import java.io.IOException;
import java.net.*;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author shik2
 * @date 2020/07/16
 **/
public class Test {
    public static void main(String[] args) throws IOException {
        DatagramSocket ds = new DatagramSocket();
        byte[] buf = new byte[2048];
        DatagramPacket dp = new DatagramPacket(buf,buf.length);
        ds.receive(dp);
        ds.close();
    }

}
