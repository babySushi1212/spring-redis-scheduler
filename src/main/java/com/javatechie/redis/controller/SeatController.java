package com.javatechie.redis.controller;


import com.javatechie.redis.service.SeatService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/seat")
public class SeatController {

    @Autowired
    private SeatService service;

    @GetMapping("/find/{date}")
    public List<Integer> findByDate(@PathVariable String date){
        return service.findByDate(date);
    }

    @GetMapping("/try")
    public String test(){
        return "test ok";
    }
}
