package com.javatechie.redis.service;

import java.time.LocalDateTime;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class Scheduler {
//    @Scheduled(cron = "*/5 * * * * *")
    @Scheduled(cron = "59 59 23 L * *")
    public void test(){
        System.out.println(LocalDateTime.now());
    }
}
