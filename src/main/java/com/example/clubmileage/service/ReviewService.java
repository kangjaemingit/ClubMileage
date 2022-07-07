package com.example.clubmileage.service;

import com.example.clubmileage.dto.EventDto;
import com.example.clubmileage.entity.PersonalPoint;
import com.example.clubmileage.entity.PointHistory;
import com.example.clubmileage.entity.Review;
import com.example.clubmileage.repository.PersonalPointRepository;
import com.example.clubmileage.repository.PointHistoryRepository;
import com.example.clubmileage.repository.ReviewRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class ReviewService {

    private final ReviewRepository reviewRepository;
    private final PointHistoryRepository pointHistoryRepository;
    private final PersonalPointRepository personalPointRepository;

    public Review review(EventDto eventDto) {
        switch (eventDto.getAction()) {
            case "ADD":
                log.info("새로운 리뷰 등록 : " + eventDto.getContent());
                return reviewAdd(eventDto);
            case "MOD":
                log.info("리뷰 수정 : " + eventDto.getContent());
                return reviewMod(eventDto);
            case "DELETE":
                log.info("리뷰 삭제 : " + eventDto.getContent());
                reviewDelete(eventDto);
                break;
            default:
                log.info("존재하지않는 액션입니다.");
                return null;
        }
        return null;
    }


    @Transactional
    public Review reviewAdd (EventDto eventDto){
        Review review = new Review(eventDto.getReviewId(), eventDto.getContent(), eventDto.getUserId(), eventDto.getAttachedPhotoIds().toString(), eventDto.getPlaceId());
        PersonalPoint personalPoint;
        if(personalPointRepository.existsById(eventDto.getUserId())){
            personalPoint = personalPointRepository.findById(eventDto.getUserId())
                    .map(p -> new PersonalPoint(p.getUserId(), p.getPoint()))
                    .orElse(null);
        } else {
            personalPoint = new PersonalPoint(eventDto.getUserId(), 0);
        }

        if(!reviewRepository.existsById(eventDto.getReviewId())){
            PointHistory pointHistory = new PointHistory(null, eventDto.getUserId(), eventDto.getAction(), "NewReview", eventDto.getReviewId(), 1);
            pointHistoryRepository.save(pointHistory);
            log.info("특정 장소에 첫 리뷰 작성 보너스 포인트 적립");
            personalPoint.pointVariation(1);
        }

        if(eventDto.getContent() != ""){
            PointHistory pointHistory = new PointHistory(null, eventDto.getUserId(), eventDto.getAction(), "Text", eventDto.getReviewId(), 1);
            pointHistoryRepository.save(pointHistory);
            log.info("1자 이상 텍스트 작성 포인트 적립");
            personalPoint.pointVariation(1);
        }

        if(eventDto.getAttachedPhotoIds().stream().toArray().length > 0){
            PointHistory pointHistory = new PointHistory(null, eventDto.getUserId(), eventDto.getAction(), "Photo", eventDto.getReviewId(), 1);
            pointHistoryRepository.save(pointHistory);
            log.info("1장 이상 사진 첨부 포인트 적립");
            personalPoint.pointVariation(1);
        }

        personalPointRepository.save(personalPoint);
        return reviewRepository.save(review);
    }

    @Transactional
    public Review reviewMod (EventDto eventDto){
        Review review = reviewRepository.findById(eventDto.getReviewId()).orElse(null);
        if(review == null){
            log.info("리뷰 수정 실패 : 존재하지 않는 리뷰입니다.");
            return null;
        }

        PersonalPoint personalPoint;
        try {
            personalPoint = personalPointRepository.findById(eventDto.getUserId())
                    .map(p -> new PersonalPoint(p.getUserId(), p.getPoint()))
                    .orElse(null);
        } catch(Exception e) {
            log.info("개인 포인트 내역이 존재하지않습니다.");
            return null;
        }

        if(review.getContent().equals("") && !eventDto.getContent().equals("")){
            PointHistory pointHistory = new PointHistory(null, eventDto.getUserId(), eventDto.getAction(), "Text", eventDto.getReviewId(), 1);
            pointHistoryRepository.save(pointHistory);
            log.info("1자 이상 텍스트 작성 포인트 적립");
            personalPoint.pointVariation(1);
        }

        if(!review.getContent().equals("") && eventDto.getContent().equals("")){
            PointHistory pointHistory = new PointHistory(null, eventDto.getUserId(), eventDto.getAction(), "TextRemove", eventDto.getReviewId(), -1);
            pointHistoryRepository.save(pointHistory);
            log.info("1자 이상 텍스트 작성 포인트 차감");
            personalPoint.pointVariation(-1);
        }

        if(review.getAttachedPhotoIds().equals("[]") && eventDto.getAttachedPhotoIds().stream().toArray().length > 0){
            PointHistory pointHistory = new PointHistory(null, eventDto.getUserId(), eventDto.getAction(), "Photo", eventDto.getReviewId(), 1);
            pointHistoryRepository.save(pointHistory);
            log.info("1장 이상 사진 첨부 포인트 적립");
            personalPoint.pointVariation(1);
        }

        if(!review.getAttachedPhotoIds().equals("[]") && eventDto.getAttachedPhotoIds().stream().toArray().length == 0){
            PointHistory pointHistory = new PointHistory(null, eventDto.getUserId(), eventDto.getAction(), "PhotoRemove", eventDto.getReviewId(), -1);
            pointHistoryRepository.save(pointHistory);
            log.info("1장 이상 사진 첨부 포인트 차감");
            personalPoint.pointVariation(-1);
        }

        review.modify(eventDto.getContent(), eventDto.getAttachedPhotoIds().toString());
        personalPointRepository.save(personalPoint);
        return reviewRepository.save(review);
    }

    @Transactional
    public void reviewDelete (EventDto eventDto){
        Review review = reviewRepository.findById(eventDto.getReviewId()).orElse(null);
        if (review == null) {
            log.info("리뷰 삭제 실패 : 존재하지 않는 리뷰입니다.");
            return;
        }

        PersonalPoint personalPoint;
        try {
            personalPoint = personalPointRepository.findById(eventDto.getUserId())
                    .map(p -> new PersonalPoint(p.getUserId(), p.getPoint()))
                    .orElse(null);
        } catch(Exception e) {
            log.info("개인 포인트 내역이 존재하지않습니다.");
            return;
        }

        Integer reviewPoint = pointHistoryRepository.findPointSumByReviewId(eventDto.getReviewId());
        personalPoint.pointVariation(-reviewPoint);
        personalPointRepository.save(personalPoint);
        log.info("리뷰 삭제 포인트 차감 : " + -reviewPoint);

        PointHistory pointHistory = new PointHistory(null, eventDto.getUserId(), eventDto.getAction(), "Delete Review", eventDto.getReviewId(), -reviewPoint);
        pointHistoryRepository.save(pointHistory);

        reviewRepository.delete(review);
    }


}