package com.intelligence.edge.dao;

import com.intelligence.edge.pojo.CarBasicData;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
public interface CarBasicDataMapper {

    List<CarBasicData> getAllCarBasicData();

    CarBasicData getCarBasicDataByID(String carID);

    int insertCarBasicData(CarBasicData carBasicData);

    int deleteCarBasicDataByID(String carID);

    int updateCarBasicData(CarBasicData carBasicData);

}
