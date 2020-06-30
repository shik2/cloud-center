package com.intelligence.edge.service.impl;

import com.intelligence.edge.dao.CarBasicDataMapper;
import com.intelligence.edge.data.CarTempData;
import com.intelligence.edge.pojo.CarBasicData;
import com.intelligence.edge.pojo.Position;
import com.intelligence.edge.service.CarBasicDataService;
import javafx.geometry.Pos;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CarBasicDataServiceImpl implements CarBasicDataService {

    @Autowired
    private CarBasicDataMapper carBasicDataMapper;

    @Override
    public List<CarBasicData> getAllCarBasicData() {
        List<CarBasicData> carList = carBasicDataMapper.getAllCarBasicData();
        for (CarBasicData car : carList) {
            car.setLongitude(CarTempData.carPos.get(car.getCarID()).getLongitude());
            car.setLatitude(CarTempData.carPos.get(car.getCarID()).getLatitude());
        }
        return carList;
    }

    @Override
    public CarBasicData getCarBasicDataByID(String carID) {
        CarBasicData car = carBasicDataMapper.getCarBasicDataByID(carID);
        car.setLongitude(CarTempData.carPos.get(car.getCarID()).getLongitude());
        car.setLatitude(CarTempData.carPos.get(car.getCarID()).getLatitude());
        return car;
    }

    @Override
    public int insertCarBasicData(CarBasicData carBasicData) {
        int res = 0;
        try {
            res = carBasicDataMapper.insertCarBasicData(carBasicData);
        }catch (Exception e){
        }
        if(res==1){
            Position position = new Position(0.0, 0.0);
            CarTempData.carPos.put(carBasicData.getCarID(),position);
        }
        return res;
    }


    @Override
    public int deleteCarBasicDataByID(String carID) {
        int res = 0;
        try {
            res = carBasicDataMapper.deleteCarBasicDataByID(carID);
        }catch (Exception e){
        }
        if(res==1){
            CarTempData.carPos.remove(carID);
        }
        return res;
    }

    public int updateCarBasicData(CarBasicData carBasicData) {
        return carBasicDataMapper.updateCarBasicData(carBasicData);
    }

}
