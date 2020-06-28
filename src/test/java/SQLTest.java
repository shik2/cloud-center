import com.intelligence.edge.Starter;
import com.intelligence.edge.dao.CarBasicDataMapper;
import com.intelligence.edge.dao.CarENVDataMapper;
import com.intelligence.edge.pojo.CarBasicData;
import com.intelligence.edge.pojo.EnvironmentInfo;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = Starter.class)
public class SQLTest {
    @Autowired
    CarENVDataMapper carENVDataMapper;

    @Test
    public void test() {
        EnvironmentInfo ei = new EnvironmentInfo("2020-11-11", "car1", 120.0, 23.0, 5.5, 9.1, 3.2);
        System.out.println(ei);
        carENVDataMapper.insertCarENVData(ei);
    }
}
