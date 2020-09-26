package com.wsk.life;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableScheduling;


@EnableScheduling  // 开启定时任务注解功能支持
@SpringBootApplication
public class LifeApplication {

    public static void main(String[] args) {
        SpringApplication.run(LifeApplication.class, args);
    }

}
