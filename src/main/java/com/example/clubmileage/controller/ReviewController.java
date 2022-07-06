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

    @PostMapping("/events")
    public ResponseEntity<Review> event(@RequestBody EventDto eventDto){
        return ResponseEntity.status(HttpStatus.OK).body(reviewService.review(eventDto));
    }

}
