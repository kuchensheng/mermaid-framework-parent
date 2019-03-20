package com.mermaid.framework;

import com.mysql.jdbc.Driver;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowire;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.sql.SQLException;
import java.util.UUID;

/**
 * @version 1.0
 * @Desription:
 * @Author:Hui
 * @CreateDate:2018/7/2 23:40
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = ApplicationTest.class)
public class ApplicationTest {

    public class Drivers {
        private Car car;

        public Car getCar() {
            return car;
        }

        public void setCar(Car car) {
            this.car = car;
        }
    }
    @Bean(autowire = Autowire.BY_TYPE)
    public Drivers drivers() {
        Drivers drivers = new Drivers();
        drivers.setCar(car());
        return drivers;

    }

    @Bean(autowire = Autowire.BY_TYPE)
    public Car car() {
        return new Car();
    }

    public class Car {

        public Car() {
            this.id = UUID.randomUUID().toString();
        }

        private String id;

        public String getId() {
            return id;
        }

        public void setId(String id) {
//            String Id = UUID.randomUUID().toString();
            this.id = id;
        }
    }
}
