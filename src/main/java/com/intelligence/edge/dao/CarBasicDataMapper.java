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

    // 获取对应设备的连接状态
    int getConnectState(String carID);

    // 修改设备的连接状态
    int setConnectState(CarBasicData carBasicData);
}
