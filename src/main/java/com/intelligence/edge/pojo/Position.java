package com.intelligence.edge.pojo;

import lombok.Data;

/**
 * @author shik2
 * @date 2020/06/29
 **/
@Data
public class Position {
    private Double longitude;
    private Double latitude;

    public Position(Double longitude, Double latitude) {
        this.longitude = longitude;
        this.latitude = latitude;
    }
}
