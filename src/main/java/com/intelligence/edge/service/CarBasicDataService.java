package com.intelligence.edge.service;

import com.intelligence.edge.pojo.CarBasicData;

import java.util.List;

public interface CarBasicDataService {

    /**
     * 查询所有设备信息
     *
     * @return List<CarBasicData>
     */
    List<CarBasicData> getAllCarBasicData();

    /**
     * 通过设备编号查询设备信息
     *
     * @param carID
     * @return List<CarBasicData>
     */
    CarBasicData getCarBasicDataByID(String carID);

    /**
     * 插入设备信息
     *
     * @param carBasicData
     * @return int
     */
    int insertCarBasicData(CarBasicData carBasicData);


    /**
     * 删除
     *
     * @param Number
     */
    int deleteCarBasicDataByID(String carID);


    /**
     * 更新设备信息
     *
     * @param
     * @return int
     */
    int updateCarBasicData(CarBasicData carBasicData);


}
