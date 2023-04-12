package com.javatechie.redis.controller;


import com.javatechie.redis.dto.CloseDTO;
import com.javatechie.redis.dto.SeatDTO;
import com.javatechie.redis.service.SeatService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
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

    @PostMapping("/update")
    public Boolean updateSeat(@RequestBody List<SeatDTO> seatDTO) {
        try {
            service.updateSeat(seatDTO);
            return true;
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }

    @PostMapping("/close")
    public Boolean closedByDate(@RequestBody CloseDTO dto) {
        try {
            service.closedByDate(dto.getDate());
            return true;
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }
}
