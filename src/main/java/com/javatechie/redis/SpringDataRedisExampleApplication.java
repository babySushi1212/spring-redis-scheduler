package com.javatechie.redis;

import com.javatechie.redis.respository.OpenHourRepository;
import com.javatechie.redis.respository.SeatRepository;
import com.javatechie.redis.service.SeatService;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.impl.StdSchedulerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class SpringDataRedisExampleApplication implements CommandLineRunner {

    @Autowired
    SeatRepository repository;

    @Autowired
    SeatService service;

    @Autowired
    @Qualifier("redisTemplate")
    RedisTemplate template;

    @Autowired
    OpenHourRepository openHourRepository;

    public static void main(String[] args) {
        SpringApplication.run(SpringDataRedisExampleApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {

        // start scheduler
        try {
            Scheduler scheduler = StdSchedulerFactory.getDefaultScheduler();
            scheduler.start();
            System.out.println("Scheduler started...");
        } catch (SchedulerException e) {
            e.printStackTrace();
        }

        // initial seat vacancy into redis
        service.initialSeat();
    }
}
