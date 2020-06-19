package com.intelligence.edge.service.impl;

import com.intelligence.edge.dao.CarBasicDataMapper;
import com.intelligence.edge.pojo.CarBasicData;
import com.intelligence.edge.service.CarBasicDataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CarBasicDataServiceImpl implements CarBasicDataService {

    @Autowired
    private CarBasicDataMapper carBasicDataMapper;

    @Override
    public List<CarBasicData> getAllCarBasicData() {
        return carBasicDataMapper.getAllCarBasicData();
    }

    @Override
    public CarBasicData getCarBasicDataByID(String carID) {
        return carBasicDataMapper.getCarBasicDataByID(carID);
    }

    @Override
    public int insertCarBasicData(CarBasicData carBasicData) {
        return carBasicDataMapper.insertCarBasicData(carBasicData);
    }


    @Override
    public int deleteCarBasicDataByID(String carID) {
        return carBasicDataMapper.deleteCarBasicDataByID(carID);
    }

    public int updateCarBasicData(CarBasicData carBasicData) {
        return carBasicDataMapper.updateCarBasicData(carBasicData);
    }

}
