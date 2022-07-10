package com.example.clubmileage.controller;

import com.example.clubmileage.dto.EventDto;
import com.example.clubmileage.entity.Review;
import com.example.clubmileage.service.ReviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

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

    // 리뷰 전체 조회
    @GetMapping("/index")
    public ResponseEntity<List<Review>> index (){
        return ResponseEntity.ok(reviewService.reviewIndex());
    }

}
