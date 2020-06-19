package com.intelligence.edge.pojo;


import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * @Author shik2
 * @Date 2020/06/19 15:19
 * @Description 无人车基本数据类
 **/
@Data
public class CarBasicData {
    @NotNull
    private String carID;
    private String type;
    private String owner;
    private Integer electricity;
    private String state;

    public CarBasicData(@NotNull String carID, String type, String owner, Integer electricity, String state) {
        this.carID = carID;
        this.type = type;
        this.owner = owner;
        this.electricity = electricity;
        this.state = state;
    }
}
