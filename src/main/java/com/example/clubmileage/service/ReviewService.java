package com.example.clubmileage.service;

import com.example.clubmileage.dto.EventDto;
import com.example.clubmileage.entity.AttachedPhoto;
import com.example.clubmileage.entity.Review;
import com.example.clubmileage.repository.AttachedPhotoRepository;
import com.example.clubmileage.repository.ReviewRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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

    public ResponseEntity<String> event(EventDto eventDto) {
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

    // 리뷰 등록
    @Transactional
    public void reviewAdd (EventDto eventDto){
        if(reviewRepository.existsById(eventDto.getReviewId())){
            throw new IllegalArgumentException("리뷰 등록 실패 : 이미 존재하는 리뷰 입니다.");
        }
        Review review = new Review(eventDto.getReviewId(), eventDto.getContent(), eventDto.getUserId(), eventDto.getPlaceId());

        List<AttachedPhoto> attachedPhotos = new ArrayList<>();
        eventDto.getAttachedPhotoIds().forEach(p -> attachedPhotos.add(new AttachedPhoto(p, review))); // 받아온 데이터 AttachedPhoto 형식으로 변환
        review.setAttachedPhotoIds(attachedPhotos);

        mileageService.reviewAddMileage(eventDto); // 리뷰 등록 마일리지 적립
        reviewRepository.save(review);
    }

    // 리뷰 수정
    @Transactional
    public void reviewMod (EventDto eventDto){
        Review review = reviewRepository.findById(eventDto.getReviewId()).orElseThrow(() -> new IllegalArgumentException("리뷰 수정 실패 : 존재하지 않는 리뷰입니다."));
        List<AttachedPhoto> attachedPhotoList = attachedPhotoRepository.findByReview(review);
        attachedPhotoRepository.deleteAll(attachedPhotoList);// 리뷰의 기존 사진 삭제 후 재등록

        mileageService.reviewModMileage(eventDto, review, attachedPhotoList); // 리뷰 수정 마일리지 조정

        List<AttachedPhoto> attachedPhotos = new ArrayList<>();
        eventDto.getAttachedPhotoIds().forEach(p -> attachedPhotos.add(new AttachedPhoto(p, review))); // 받아온 데이터 AttachedPhoto 형식으로 변환

        review.modify(eventDto.getContent(), attachedPhotos);
        reviewRepository.save(review);

        attachedPhotoRepository.saveAll(attachedPhotos);
    }

    // 리뷰 삭제
    @Transactional
    public void reviewDelete (EventDto eventDto){
        Review review = reviewRepository.findById(eventDto.getReviewId()).orElseThrow(() -> new IllegalArgumentException("리뷰 삭제 실패 : 존재하지 않는 리뷰입니다."));

        mileageService.reviewDeleteMileage(eventDto); // 리뷰 삭제 마일리지 차감
        reviewRepository.delete(review);
    }

    public List<Review> reviewIndex(){
        return reviewRepository.findAll();
    }


}