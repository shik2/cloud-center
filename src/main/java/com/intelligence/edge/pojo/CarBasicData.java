package com.intelligence.edge.pojo;


import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * @Author shik2
 * @Date 2020/06/19 15:19
 * @Description 无人车基本数据类
 **/
public class CarBasicData {
    @NotNull
    private String carID;
    private String type;
    private String owner;
    private Integer electricity;
    private Integer state;
    private Double longitude;
    private Double latitude;
    private Integer cPort;
    private Integer ePort;
    private Integer vPort;

    public CarBasicData(@NotNull String carID, String type, String owner, Integer cPort, Integer ePort, Integer vPort) {
        this.carID = carID;
        this.type = type;
        this.owner = owner;
        this.cPort = cPort;
        this.ePort = ePort;
        this.vPort = vPort;
    }

    public CarBasicData() {
    }

    public String getCarID() {
        return carID;
    }

    public void setCarID(String carID) {
        this.carID = carID;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public Integer getElectricity() {
        return electricity;
    }

    public void setElectricity(Integer electricity) {
        this.electricity = electricity;
    }

    public Integer getState() {
        return state;
    }

    public void setState(Integer state) {
        this.state = state;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Integer getcPort() {
        return cPort;
    }

    public void setcPort(Integer cPort) {
        this.cPort = cPort;
    }

    public Integer getePort() {
        return ePort;
    }

    public void setePort(Integer ePort) {
        this.ePort = ePort;
    }

    public Integer getvPort() {
        return vPort;
    }

    public void setvPort(Integer vPort) {
        this.vPort = vPort;
    }

    @Override
    public String toString() {
        return "CarBasicData{" +
                "carID='" + carID + '\'' +
                ", type='" + type + '\'' +
                ", owner='" + owner + '\'' +
                ", electricity=" + electricity +
                ", state=" + state +
                ", longitude=" + longitude +
                ", latitude=" + latitude +
                ", cPort=" + cPort +
                ", ePort=" + ePort +
                ", vPort=" + vPort +
                '}';
    }
}
