package com.example.clubmileage.service;

import com.example.clubmileage.dto.EventDto;
import com.example.clubmileage.entity.AttachedPhoto;
import com.example.clubmileage.entity.PersonalPoint;
import com.example.clubmileage.entity.PointHistory;
import com.example.clubmileage.entity.Review;
import com.example.clubmileage.repository.AttachedPhotoRepository;
import com.example.clubmileage.repository.PersonalPointRepository;
import com.example.clubmileage.repository.PointHistoryRepository;
import com.example.clubmileage.repository.ReviewRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class ReviewService {

    private final ReviewRepository reviewRepository;
    private final AttachedPhotoRepository attachedPhotoRepository;
    private final MileageService mileageService;

    public ResponseEntity<String> review(EventDto eventDto) {
        switch (eventDto.getAction()) {
            case "ADD":
                log.info("새로운 리뷰 등록 : " + eventDto.getContent());
                reviewAdd(eventDto);
                return ResponseEntity.ok("Review Add Success");
            case "MOD":
                log.info("리뷰 수정 : " + eventDto.getContent());
                reviewMod(eventDto);
                return ResponseEntity.ok("Review Modify Success");
            case "DELETE":
                log.info("리뷰 삭제 : " + eventDto.getContent());
                reviewDelete(eventDto);
                return ResponseEntity.ok("Review Delete Success");
            default:
                log.info("존재하지않는 액션입니다.");
                return ResponseEntity.badRequest().body("Error : Unknown Action");
        }
    }


    @Transactional
    public void reviewAdd (EventDto eventDto){
        Review review = new Review(eventDto.getReviewId(), eventDto.getContent(), eventDto.getUserId(), eventDto.getPlaceId());

        List<AttachedPhoto> attachedPhotos = new ArrayList<>();
        eventDto.getAttachedPhotoIds().forEach(p -> attachedPhotos.add(new AttachedPhoto(p, review)));
        review.setAttachedPhotoIds(attachedPhotos);

        mileageService.reviewAddMileage(eventDto);
        reviewRepository.save(review);
    }

    @Transactional
    public void reviewMod (EventDto eventDto){
        Review review = reviewRepository.findById(eventDto.getReviewId()).orElseThrow(() -> new IllegalArgumentException("리뷰 수정 실패 : 존재하지 않는 리뷰입니다."));

        List<AttachedPhoto> attachedPhotos = new ArrayList<>();
        eventDto.getAttachedPhotoIds().forEach(p -> attachedPhotos.add(new AttachedPhoto(p, review)));
        mileageService.reviewModMileage(eventDto, review, attachedPhotos);


        attachedPhotoRepository.deleteAll(attachedPhotoRepository.findByReview(review));
        review.modify(eventDto.getContent(), attachedPhotos);
        reviewRepository.save(review);
    }

    @Transactional
    public void reviewDelete (EventDto eventDto){
        Review review = reviewRepository.findById(eventDto.getReviewId()).orElseThrow(() -> new IllegalArgumentException("리뷰 삭제 실패 : 존재하지 않는 리뷰입니다."));

        mileageService.reviewDeleteMileage(eventDto);
        reviewRepository.delete(review);
    }


}