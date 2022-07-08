package com.example.clubmileage.service;

import com.example.clubmileage.dto.EventDto;
import com.example.clubmileage.entity.AttachedPhoto;
import com.example.clubmileage.entity.PersonalPoint;
import com.example.clubmileage.entity.PointHistory;
import com.example.clubmileage.entity.Review;
import com.example.clubmileage.repository.PersonalPointRepository;
import com.example.clubmileage.repository.PointHistoryRepository;
import com.example.clubmileage.repository.ReviewRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
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
            personalPoint = personalPointRepository.findById(eventDto.getUserId())
                    .map(p -> new PersonalPoint(p.getUserId(), p.getPoint()))
                    .orElse(new PersonalPoint(eventDto.getUserId(), 0));

        // 첫 리뷰 보너스 포인트 적립
        if(!reviewRepository.existsById(eventDto.getReviewId())){
            PointHistory pointHistory = new PointHistory(eventDto.getUserId(), eventDto.getAction(), "NewReview", eventDto.getReviewId(), 1);
            pointHistoryRepository.save(pointHistory);
            log.info("특정 장소에 첫 리뷰 작성 보너스 포인트 적립");
            personalPoint.pointVariation(1);
        }

        // 텍스트 작성 포인트 적립
        if(!eventDto.getContent().isBlank()){
            PointHistory pointHistory = new PointHistory(eventDto.getUserId(), eventDto.getAction(), "Text", eventDto.getReviewId(), 1);
            pointHistoryRepository.save(pointHistory);
            log.info("1자 이상 텍스트 작성 포인트 적립");
            personalPoint.pointVariation(1);
        }

        // 사진 첨부 포인트 적립
        if(!eventDto.getAttachedPhotoIds().isEmpty()){
            PointHistory pointHistory = new PointHistory(eventDto.getUserId(), eventDto.getAction(), "Photo", eventDto.getReviewId(), 1);
            pointHistoryRepository.save(pointHistory);
            log.info("1장 이상 사진 첨부 포인트 적립");
            personalPoint.pointVariation(1);
        }

        return personalPointRepository.save(personalPoint);
    }

    public PersonalPoint reviewModMileage(EventDto eventDto, Review review, List<AttachedPhoto> attachedPhotoList){
        PersonalPoint personalPoint = personalPointRepository.findById(eventDto.getUserId())
                .map(p -> new PersonalPoint(p.getUserId(), p.getPoint()))
                .orElseThrow(() -> new IllegalArgumentException("리뷰 수정 포인트 적립 실패 : 개인 포인트 내역이 존재하지 않습니다."));


        if(review.getContent().isBlank() && !eventDto.getContent().isBlank()){ // 텍스트 미작성 -> 텍스트 작성
            PointHistory pointHistory = new PointHistory(eventDto.getUserId(), eventDto.getAction(), "Text", eventDto.getReviewId(), 1);
            pointHistoryRepository.save(pointHistory);
            log.info("1자 이상 텍스트 작성 포인트 적립");
            personalPoint.pointVariation(1);
        } else if(!review.getContent().isBlank() && eventDto.getContent().isBlank()){ // 텍스트 작성 -> 텍스트 삭제
            PointHistory pointHistory = new PointHistory(eventDto.getUserId(), eventDto.getAction(), "TextRemove", eventDto.getReviewId(), -1);
            pointHistoryRepository.save(pointHistory);
            log.info("1자 이상 텍스트 작성 포인트 차감");
            personalPoint.pointVariation(-1);
        }

        if(review.getAttachedPhotoIds().isEmpty() && !attachedPhotoList.isEmpty()){ // 사진 미첨부 -> 사진 첨부
            PointHistory pointHistory = new PointHistory(eventDto.getUserId(), eventDto.getAction(), "Photo", eventDto.getReviewId(), 1);
            pointHistoryRepository.save(pointHistory);
            log.info("1장 이상 사진 첨부 포인트 적립");
            personalPoint.pointVariation(1);
        } else if(!review.getAttachedPhotoIds().isEmpty() && attachedPhotoList.isEmpty()){ // 사진 첨부 -> 사진 삭제
            PointHistory pointHistory = new PointHistory(eventDto.getUserId(), eventDto.getAction(), "PhotoRemove", eventDto.getReviewId(), -1);
            pointHistoryRepository.save(pointHistory);
            log.info("1장 이상 사진 첨부 포인트 차감");
            personalPoint.pointVariation(-1);
        }

        return personalPointRepository.save(personalPoint);
    }

    public PersonalPoint reviewDeleteMileage(EventDto eventDto){
        PersonalPoint personalPoint = personalPointRepository.findById(eventDto.getUserId())
                .map(p -> new PersonalPoint(p.getUserId(), p.getPoint()))
                .orElseThrow(() -> new IllegalArgumentException("리뷰 수정 포인트 적립 실패 : 개인 포인트 내역이 존재하지 않습니다."));

        Integer reviewPoint = pointHistoryRepository.findPointSumByReviewId(eventDto.getReviewId());
        personalPoint.pointVariation(-reviewPoint);
        log.info("리뷰 삭제 포인트 차감 : " + -reviewPoint);

        PointHistory pointHistory = new PointHistory(eventDto.getUserId(), eventDto.getAction(), "Delete Review", eventDto.getReviewId(), -reviewPoint);
        pointHistoryRepository.save(pointHistory);
        return personalPointRepository.save(personalPoint);
    }

    public Integer personalMileage(UUID userId){
        Integer mileage = personalPointRepository.findPointById(userId);
        if(mileage == null){
            return 0;
        }
        else{
            return mileage;
        }
    }
}
