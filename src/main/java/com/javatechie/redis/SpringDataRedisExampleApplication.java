package com.javatechie.redis;

import com.javatechie.redis.entity.Seat;
import com.javatechie.redis.respository.OpenHourRepository;
//import com.javatechie.redis.respository.SeatRepository;
import com.javatechie.redis.respository.SeatRepository;
import com.javatechie.redis.service.SeatService;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.concurrent.TimeUnit;
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
//        SpringApplication.run(SpringDataRedisExampleApplication.class, args);
        SpringApplication.exit(SpringApplication.run(SpringDataRedisExampleApplication.class, args));

    }

    @Override
    public void run(String... args) throws Exception {

        System.out.println(openHourRepository.findAll());

//
        service.initialOpenDate();

        LocalDate startDate = LocalDate.of(2023, 4, 25);
        LocalDate endDate = LocalDate.of(2023, 4, 30);

        // Loop through the dates and add the hash values to Redis
        for (LocalDate date = startDate; !date.isAfter(endDate); date = date.plusDays(1)) {
            System.out.println(date + ": " + template.opsForHash().get("Seat", date.toString()));
        }
        System.out.println(template.opsForHash().get("Seat", 2023-04-30));
//
        ArrayList<Integer> seats = new ArrayList<>(Arrays.asList(1, 2, 3));
        Seat seat = new Seat("1/1", seats);
        repository.save(seat);
//        repository.findByDate("1/1");
        System.out.println("get from date" + repository.findByDate("1/1"));

        ArrayList<Integer> seats1 = new ArrayList<>(Arrays.asList(1, 2, 4));
        Seat seat1 = new Seat("1/1", seats1);
        repository.save(seat1);
        System.out.println("get from date" + repository.findByDate("1/1"));

        ArrayList<Integer> seats2 = new ArrayList<>(Arrays.asList(1, 2, 4));
        Seat seat2 = new Seat("1/2", seats2);
        repository.save(seat2, 60, TimeUnit.SECONDS);
    }
}

//        try {
//            Scheduler scheduler = StdSchedulerFactory.getDefaultScheduler();
//            scheduler.start();
//            System.out.println("Scheduler started...");
//        } catch (SchedulerException e) {
//            e.printStackTrace();
//        }