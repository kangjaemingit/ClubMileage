package com.example.clubmileage.controller;

import com.example.clubmileage.dto.EventDto;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ReviewController {

    @PostMapping("/events")
    public String event(@RequestBody EventDto eventDto){
        return null;
    }

}
