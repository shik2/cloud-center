package com.intelligence.edge.runner;

import com.intelligence.edge.dao.CarBasicDataMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

/**
 * @author shik2
 * @date 2020/06/28
 **/
@Component
public class ResetConnect implements CommandLineRunner {

    @Autowired
    private CarBasicDataMapper carBasicDataMapper;

    @Override
    public void run(String... args) throws Exception {
        System.out.println("重置连接");
        carBasicDataMapper.resetState();
    }
}
