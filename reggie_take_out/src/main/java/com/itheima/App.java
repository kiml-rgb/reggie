package com.itheima;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author zyf
 * @program: reggie_take_out
 * @description:
 * @date 2022-09-11 15:12:13
 */

@SpringBootApplication
@MapperScan("com.itheima.mapper")
public class App {
    public static void main(String[] args) {
        SpringApplication.run(App.class, args);
    }
}
