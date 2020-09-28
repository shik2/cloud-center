package com.intelligence.edge.controller;

import com.intelligence.edge.dao.CarBasicDataMapper;
import com.intelligence.edge.pojo.CarBasicData;
import com.intelligence.edge.service.CarBasicDataService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/device/car")
@Slf4j(topic = "c.CarBasicDataController")
public class CarBasicDataController {
    @Autowired
    CarBasicDataService carBasicDataService;

    @RequestMapping(value = "test")
    public Integer hello() {

        return 1;
    }

    /**
     *
     * @return 获取所有无人车的基本信息
     */
    @RequestMapping(value = "allCarData")
    public List<CarBasicData> getAllCarBasicData() {
        return carBasicDataService.getAllCarBasicData();
    }

    /**
     * 根据设备id获取对应无人车基本信息
     * @param carID
     * @return
     */
    @RequestMapping(value = "getCarData")
    public CarBasicData getCarBasicDataByID(String carID) {
        return carBasicDataService.getCarBasicDataByID(carID);
    }

    /**
     * 新增无人车
     * @param car
     * @return
     */
    @PostMapping(value = "insertCar")
    public int insertCarBasicData(@RequestBody CarBasicData car) {
        car.setElectricity(100);
        car.setState(0);
        log.info("新增car:"+car);
        if(car.getCarID()==null){
            return 0;
        }
        return carBasicDataService.insertCarBasicData(car);
    }

    // 删除无人车
    @RequestMapping(value = "deleteCar")
    public int deleteCarBasicDataByID(String carID) {
        return carBasicDataService.deleteCarBasicDataByID(carID);
    }

    // 更新无人车信息
    @PostMapping(value = "updateCar")
    public int updateCarBasicData(@RequestBody CarBasicData carBasicData) {
        log.info("carBasicData:"+carBasicData);
        return carBasicDataService.updateCarBasicData(carBasicData);
    }

}
