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

import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class MileageService {

    private final PersonalPointRepository personalPointRepository;
    private final ReviewRepository reviewRepository;
    private final PointHistoryRepository pointHistoryRepository;

    public PersonalPoint reviewAddMileage(EventDto eventDto){
        PersonalPoint personalPoint;
        if(personalPointRepository.existsById(eventDto.getUserId())){
            personalPoint = personalPointRepository.findById(eventDto.getUserId())
                    .map(p -> new PersonalPoint(p.getUserId(), p.getPoint()))
                    .orElse(null);
        } else {
            personalPoint = new PersonalPoint(eventDto.getUserId(), 0);
        }

        // 첫 리뷰 보너스 포인트 적립
        if(!reviewRepository.existsById(eventDto.getReviewId())){
            PointHistory pointHistory = new PointHistory(eventDto.getUserId(), eventDto.getAction(), "NewReview", eventDto.getReviewId(), 1);
            pointHistoryRepository.save(pointHistory);
            log.info("특정 장소에 첫 리뷰 작성 보너스 포인트 적립");
            personalPoint.pointVariation(1);
        }

        // 텍스트 작성 포인트 적립
        if(eventDto.getContent() != ""){
            PointHistory pointHistory = new PointHistory(eventDto.getUserId(), eventDto.getAction(), "Text", eventDto.getReviewId(), 1);
            pointHistoryRepository.save(pointHistory);
            log.info("1자 이상 텍스트 작성 포인트 적립");
            personalPoint.pointVariation(1);
        }

        // 사진 첨부 포인트 적립
        if(eventDto.getAttachedPhotoIds().stream().toArray().length > 0){
            PointHistory pointHistory = new PointHistory(eventDto.getUserId(), eventDto.getAction(), "Photo", eventDto.getReviewId(), 1);
            pointHistoryRepository.save(pointHistory);
            log.info("1장 이상 사진 첨부 포인트 적립");
            personalPoint.pointVariation(1);
        }

        return personalPointRepository.save(personalPoint);
    }

    public PersonalPoint reviewModMileage(EventDto eventDto, Review review){
        PersonalPoint personalPoint;
        try {
            personalPoint = personalPointRepository.findById(eventDto.getUserId())
                    .map(p -> new PersonalPoint(p.getUserId(), p.getPoint()))
                    .orElse(null);
        } catch(Exception e) {
            log.info("개인 포인트 내역이 존재하지않습니다.");
            return null;
        }

        // 텍스트 미작성 -> 텍스트 작성
        if(review.getContent().equals("") && !eventDto.getContent().equals("")){
            PointHistory pointHistory = new PointHistory(eventDto.getUserId(), eventDto.getAction(), "Text", eventDto.getReviewId(), 1);
            pointHistoryRepository.save(pointHistory);
            log.info("1자 이상 텍스트 작성 포인트 적립");
            personalPoint.pointVariation(1);
        }

        // 텍스트 작성 -> 텍스트 삭제
        if(!review.getContent().equals("") && eventDto.getContent().equals("")){
            PointHistory pointHistory = new PointHistory(eventDto.getUserId(), eventDto.getAction(), "TextRemove", eventDto.getReviewId(), -1);
            pointHistoryRepository.save(pointHistory);
            log.info("1자 이상 텍스트 작성 포인트 차감");
            personalPoint.pointVariation(-1);
        }

        // 사진 미첨부 -> 사진 첨부
        if(review.getAttachedPhotoIds().equals("[]") && eventDto.getAttachedPhotoIds().stream().toArray().length > 0){
            PointHistory pointHistory = new PointHistory(eventDto.getUserId(), eventDto.getAction(), "Photo", eventDto.getReviewId(), 1);
            pointHistoryRepository.save(pointHistory);
            log.info("1장 이상 사진 첨부 포인트 적립");
            personalPoint.pointVariation(1);
        }

        // 사진 첨부 -> 사진 삭제
        if(!review.getAttachedPhotoIds().equals("[]") && eventDto.getAttachedPhotoIds().stream().toArray().length == 0){
            PointHistory pointHistory = new PointHistory(eventDto.getUserId(), eventDto.getAction(), "PhotoRemove", eventDto.getReviewId(), -1);
            pointHistoryRepository.save(pointHistory);
            log.info("1장 이상 사진 첨부 포인트 차감");
            personalPoint.pointVariation(-1);
        }

        return personalPointRepository.save(personalPoint);
    }

    public PersonalPoint reviewDeleteMileage(EventDto eventDto){
        PersonalPoint personalPoint;
        try {
            personalPoint = personalPointRepository.findById(eventDto.getUserId())
                    .map(p -> new PersonalPoint(p.getUserId(), p.getPoint()))
                    .orElse(null);
        } catch(Exception e) {
            log.info("개인 포인트 내역이 존재하지않습니다.");
            return null;
        }

        Integer reviewPoint = pointHistoryRepository.findPointSumByReviewId(eventDto.getReviewId());
        personalPoint.pointVariation(-reviewPoint);
        log.info("리뷰 삭제 포인트 차감 : " + -reviewPoint);

        PointHistory pointHistory = new PointHistory(eventDto.getUserId(), eventDto.getAction(), "Delete Review", eventDto.getReviewId(), -reviewPoint);
        pointHistoryRepository.save(pointHistory);
        return personalPointRepository.save(personalPoint);
    }

    public Integer personalMileage(UUID userId){
        return personalPointRepository.findPointById(userId);
    }
}
