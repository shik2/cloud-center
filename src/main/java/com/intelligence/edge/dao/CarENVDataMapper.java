package com.intelligence.edge.dao;

import com.intelligence.edge.pojo.EnvironmentInfo;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

@Mapper
@Repository
public interface CarENVDataMapper {
    int insertCarENVData(EnvironmentInfo environmentInfo);
}
