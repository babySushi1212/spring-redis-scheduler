package com.javatechie.redis.service;

import static com.javatechie.redis.respository.SeatRepository.HASH_KEY;

import com.javatechie.redis.entity.Seat;
import com.javatechie.redis.respository.OpenHourRepository;
import com.javatechie.redis.respository.SeatRepository;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

@Service
//@Component
public class SeatService {
    final private Integer TOTAL_SEAT = 60;
    @Autowired
    private SeatRepository seatDao;

    @Autowired
    @Qualifier("redisTemplate")
    RedisTemplate template;

    @Autowired
    OpenHourRepository openHourRepository;

    public Boolean create() {
//        template.expire(SeatDao.HASH_KEY, 60, TimeUnit.SECONDS);

        return true;
    }

    public Boolean closedByDate(String date) {
        List<Integer> openHour = IntStream.range(0, 24)
            .mapToObj(i -> TOTAL_SEAT)
            .collect(Collectors.toList());
        seatDao.save(new Seat(date, openHour));
        return true;
    }

    /**
     * Initial 2022/11/1 ~ 2023/4/30 open hour of every day
     *
     * @return
     */
    public Integer initialOpenDate() {
        // Get the start date and end date
//        LocalDate startDate = LocalDate.of(2022, 11, 1);
        LocalDate startDate = LocalDate.of(2023, 4, 25);
        LocalDate endDate = LocalDate.of(2023, 4, 30);

        // Create a list with 24 60s
        List<Integer> minutes = new ArrayList<>(Collections.nCopies(24, 60));

        // Loop through the dates and add the hash values to Redis
        for (LocalDate date = startDate; !date.isAfter(endDate); date = date.plusDays(1)) {
            System.out.println(date);
            if (date.getDayOfWeek() == DayOfWeek.SUNDAY) {
                System.out.println(1);
            } else if (date.getDayOfWeek() == DayOfWeek.MONDAY) {
                System.out.println(2);
            } else if (date.getDayOfWeek() == DayOfWeek.TUESDAY) {
                System.out.println(3);
            } else if (date.getDayOfWeek() == DayOfWeek.WEDNESDAY) {
                System.out.println(4);
            } else if (date.getDayOfWeek() == DayOfWeek.THURSDAY) {
                System.out.println(5);
            } else if (date.getDayOfWeek() == DayOfWeek.FRIDAY) {
                System.out.println(6);
            } else if (date.getDayOfWeek() == DayOfWeek.SATURDAY) {
                System.out.println(7);
            } else {
                System.out.println(0); // fallback return               }
            }
            template.opsForHash().put(HASH_KEY, date.toString(), minutes);
        }
        return 1;
    }

    public List<Integer> findByDate(String date){
        return seatDao.findByDate(date);
    }


}
