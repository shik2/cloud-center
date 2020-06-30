package com.intelligence.edge.data;

import com.intelligence.edge.dao.CarBasicDataMapper;
import com.intelligence.edge.pojo.CarBasicData;
import com.intelligence.edge.pojo.Position;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author shik2
 * @date 2020/06/29
 **/
@Component
public class CarTempData {
    public static Map<String, Position> carPos = new HashMap<>();

    @Autowired
    private CarBasicDataMapper carBasicDataMapper;

    @PostConstruct
    public void init() {
        //加载无人车设备
        List<CarBasicData> carList = carBasicDataMapper.getAllCarBasicData();

        for (CarBasicData carBasicData : carList) {
            Position position = new Position(0d,0d);
            carPos.put(carBasicData.getCarID(),position);
        }


    }
    @PreDestroy
    public void destroy() {
        //系统运行结束
    }

}
