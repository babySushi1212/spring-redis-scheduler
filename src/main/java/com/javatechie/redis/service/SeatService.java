package com.javatechie.redis.service;

import static com.javatechie.redis.respository.SeatRepository.HASH_KEY;

import com.javatechie.redis.dto.SeatDTO;
import com.javatechie.redis.entity.OpenHour;
import com.javatechie.redis.entity.Seat;
import com.javatechie.redis.respository.OpenHourRepository;
import com.javatechie.redis.respository.SeatRepository;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
public class SeatService {
    final private Integer TOTAL_SEAT = 60;
    @Autowired
    private SeatRepository seatRepository;

    @Autowired
    @Qualifier("redisTemplate")
    RedisTemplate template;

    @Autowired
    OpenHourRepository openHourRepository;

    public Boolean create() {
//        template.expire(SeatDao.HASH_KEY, 60, TimeUnit.SECONDS);

        return true;
    }

    public Boolean updateSeat(List<SeatDTO> dtos) {
        dtos.forEach(e -> updateSeat_(e.getDate(), e.getHour(), e.getChange()));
        return true;
    }

    private Boolean updateSeat_(String date, Integer hour, Integer change) {
        List<Integer> seat = seatRepository.findByDate(date);
        seat.set(hour - 1, seat.get(hour - 1) - change);
        seatRepository.save(new Seat(date, seat));
        return true;
    }

    public Boolean closedByDate(String date) {
        List<Integer> seatIn24Hour = new ArrayList<>(Collections
            .nCopies(24, 0));
        seatRepository.save(new Seat(date, seatIn24Hour));
        return true;
    }

    /**
     * Set seat for next month
     */
    @Scheduled(cron = "59 59 23 L * *")
    private void initialNextMonth(){
        LocalDate startDate = LocalDate.now().plusDays(1);
        LocalDate endDate = startDate.plusMonths(1).minusDays(1);
        setSeats(startDate, endDate);
    }

    /**
     * Initial 2022/11/1 ~ 2023/4/30 open hour of every day
     *
     * @return: how many date we set
     */
    public void initialSeat() {
        // Get the start date and end date
        // TODO: hard coded so far
        LocalDate startDate = LocalDate.of(2023, 4, 25);
        LocalDate endDate = LocalDate.of(2023, 4, 30);
        setSeats(startDate, endDate);
    }

    private Integer setSeats(LocalDate startDate, LocalDate endDate) {
        // openHour from 1~7 [[8,9],[8,9],[8,9],[8,9],[8,9],[8,11],[8,11]]
        List<List<Integer>> openRange = openHourRepository
            .findAll()
            .stream()
            .map(e -> getOpenHourRange(e))
            .collect(Collectors.toList());

        // Loop through the dates and add the hash values to Redis
        List<Integer> seats = null;
        Integer count = 0;
        for (LocalDate date = startDate; !date.isAfter(endDate); date = date.plusDays(1)) {
            if (date.getDayOfWeek() == DayOfWeek.MONDAY) {
                seats = getSeat(openRange.get(0));
            } else if (date.getDayOfWeek() == DayOfWeek.TUESDAY) {
                seats = getSeat(openRange.get(1));
            } else if (date.getDayOfWeek() == DayOfWeek.WEDNESDAY) {
                seats = getSeat(openRange.get(2));
            } else if (date.getDayOfWeek() == DayOfWeek.THURSDAY) {
                seats = getSeat(openRange.get(3));
            } else if (date.getDayOfWeek() == DayOfWeek.FRIDAY) {
                seats = getSeat(openRange.get(4));
            } else if (date.getDayOfWeek() == DayOfWeek.SATURDAY) {
                seats = getSeat(openRange.get(5));
            } else if (date.getDayOfWeek() == DayOfWeek.SUNDAY) {
                seats = getSeat(openRange.get(6));
            }
            template.opsForHash().put(HASH_KEY, date.toString(), seats);
            count++;
        }
        return count;
    }

    public List<Integer> findByDate(String date) {
        return seatRepository.findByDate(date);
    }

    private List<Integer> getOpenHourRange(OpenHour openHour) {
        // openRange: [start end], Time -> Integer of Hour
        // 24:00:00 -> 24
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(openHour.getOpenTimeStart());
        Integer start = calendar.get(Calendar.HOUR_OF_DAY);

        calendar.setTime(openHour.getOpenTimeEnd());
        Integer end = calendar.get(Calendar.HOUR_OF_DAY);
        return new ArrayList<>(Arrays.asList(start, end));
    }

    private List<Integer> getSeat(List<Integer> openRange) {
        // openRange: [start end], Integer, hour 0~24
        List<Integer> former = new ArrayList<>(Collections
            .nCopies(openRange.get(0) - 1, 0));
        List<Integer> middle = new ArrayList<>(Collections
            .nCopies(openRange.get(1) - openRange.get(0) + 1, 60));
        List<Integer> latter = new ArrayList<>(Collections
            .nCopies(24 - openRange.get(1), 0));

        // concat three part of list and return
        return Arrays.asList(former, middle, latter)
            .stream()
            .flatMap(List::stream)
            .collect(Collectors.toList());
    }
}
