package com.example.clubmileage.controller;

import com.example.clubmileage.dto.EventDto;
import com.example.clubmileage.entity.Review;
import com.example.clubmileage.service.ReviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class ReviewController {

    private final ReviewService reviewService;

    // 이벤트 API
    @PostMapping("/events")
    public ResponseEntity<String> event(@RequestBody EventDto eventDto){
        ResponseEntity<String> res = reviewService.event(eventDto);
        return res;
    }

}
