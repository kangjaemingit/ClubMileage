package com.example.clubmileage.controller;

import com.example.clubmileage.entity.PersonalPoint;
import com.example.clubmileage.service.MileageService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@Controller
@RequiredArgsConstructor
public class MileageController {

    private final MileageService mileageService;

    @GetMapping("/mileage/{userId}")
    public ResponseEntity<Integer> mileage(@PathVariable UUID userId){
        return ResponseEntity.ok(mileageService.personalMileage(userId));
    }

}
