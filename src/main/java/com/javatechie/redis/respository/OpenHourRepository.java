package com.javatechie.redis.respository;

import com.javatechie.redis.entity.OpenHour;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OpenHourRepository extends JpaRepository<OpenHour, Integer> {
}

