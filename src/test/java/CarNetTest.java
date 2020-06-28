import com.intelligence.edge.Starter;
import com.intelligence.edge.service.CarNetService;
import com.intelligence.edge.service.impl.CarBasicDataServiceImpl;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * @author shik2
 * @date 2020/06/28
 **/
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = Starter.class)
public class CarNetTest {

    @Autowired
    CarNetService carNetService;

    @Test
    public void test(){
        System.out.println(carNetService.ping("www.baidu.com"));

    }

}
