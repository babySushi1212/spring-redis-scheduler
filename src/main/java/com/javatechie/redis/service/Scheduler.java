package com.javatechie.redis.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Calendar;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class Scheduler {
    // spring quartz not support String. like "L"
//    @Scheduled(cron = "*/5 * * * * *")
    @Scheduled(cron = "59 59 23 * * *")
    public void test(){
        int today = LocalDate.now(ZoneId.of("Asia/Taipei")).getDayOfMonth();
        int lastDay = Calendar.getInstance().getActualMaximum(Calendar.DAY_OF_MONTH);
        if (today == lastDay) {
            System.out.println("Today is the last day of the month.");
        }
        System.out.println(today + " " + lastDay);
        System.out.println(LocalDateTime.now());
    }
}
