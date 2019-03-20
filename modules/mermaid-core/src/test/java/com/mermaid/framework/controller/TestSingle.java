package com.mermaid.framework.controller;

import com.mermaid.framework.ApplicationTest;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author Chensheng.Ku
 * @version 创建时间：2019/3/19 15:19
 */
public class TestSingle extends ApplicationTest{


    @Autowired
    private Drivers drivers;

    @Autowired
    private Car car;

    @Test
    public void testCar() {
//        boolean isEq = drivers.getCar() == car;
        System.out.println("drivers.getCar().getId()\t"+drivers.getCar().getId());
        System.out.println("car.getId\t"+car.getId());
//        Assert.assertEquals(drivers.getCar(),car);
    }

}
