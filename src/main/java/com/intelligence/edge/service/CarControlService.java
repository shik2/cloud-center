package com.intelligence.edge.service;

/**
 * @author shik2
 * @date 2020/07/02
 **/
public interface CarControlService {

    void control(String carID, String instruction);

    void closeConnection(String carID);
}
