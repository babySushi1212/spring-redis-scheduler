package com.javatechie.redis.respository;


import com.javatechie.redis.entity.Seat;
import java.util.List;
import java.util.concurrent.TimeUnit;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class SeatRepository {
    public static final String HASH_KEY = "Seat";

    @Autowired
    @Qualifier("redisTemplate")
    private RedisTemplate template;

    public Boolean save(Seat seat) {
        template.opsForHash().put(
            HASH_KEY,
            seat.getId(),
            seat.getSeat()
        );
        return true;
    }
    public Boolean save(Seat seat, Integer timeout, TimeUnit timeUnit) {
        template.expire(HASH_KEY, timeout, timeUnit);
        save(seat);
        return true;
    }

    public List<Integer> findByDate(String date) {
        return (List) template.opsForHash().get(HASH_KEY, date);
    }
}
