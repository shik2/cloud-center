import com.intelligence.edge.Starter;
import com.intelligence.edge.dao.CarENVDataMapper;
import com.intelligence.edge.pojo.EnvironmentInfo;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = Starter.class)
public class SQLTest {
//    @Autowired
//    CarENVDataMapper carENVDataMapper;
//
//    @Test
//    public void test() {
//        EnvironmentInfo ei = new EnvironmentInfo("2020-11-11", "car1", 120.0, 23.0, 5.5, 9.1, 3.2);
//        System.out.println(ei);
//        carENVDataMapper.insertCarENVData(ei);
//    }

    @Test
    public void test2(){
        Pattern pattern = Pattern.compile("(ttl=\\d+)(\\s+time=)", Pattern.CASE_INSENSITIVE);
        String str = "64 bytes from 112.80.248.75: icmp_seq=1 ttl=50 time=12.9 ms";

        Pattern pattern2 = Pattern.compile("(^[+-]?\\d+\\.?\\d*$)", Pattern.CASE_INSENSITIVE);
        String str2 = "12.5";

        Matcher matcher = pattern.matcher(str);
        System.out.println(matcher.find());
    }
}
