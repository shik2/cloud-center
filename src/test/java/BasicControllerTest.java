import com.intelligence.edge.Starter;
import com.intelligence.edge.pojo.CarBasicData;
import com.intelligence.edge.service.CarBasicDataService;
import com.intelligence.edge.service.impl.CarBasicDataServiceImpl;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = Starter.class)
public class BasicControllerTest {

    @Autowired
    CarBasicDataServiceImpl carBasicDataService;

    @Test
    public void test(){
        CarBasicData cbd = new CarBasicData("car3","xy","shik2",10,0);
        System.out.println(cbd);
        carBasicDataService.insertCarBasicData(cbd);
    }
}
