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

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class MileageService {

    private final PersonalPointRepository personalPointRepository;
    private final ReviewRepository reviewRepository;
    private final PointHistoryRepository pointHistoryRepository;

    // 리뷰 등록 시 마일리지 적립
    public PersonalPoint reviewAddMileage(EventDto eventDto){
        PersonalPoint personalPoint;
            personalPoint = personalPointRepository.findById(eventDto.getUserId())
                    .map(p -> new PersonalPoint(p.getUserId(), p.getPoint()))
                    .orElse(new PersonalPoint(eventDto.getUserId(), 0));

        List<PointHistory> pointHistories = new ArrayList<>();
        // 첫 리뷰 보너스 포인트 적립
        if(!reviewRepository.existsByPlaceId(eventDto.getPlaceId())){
            pointHistories.add(new PointHistory(eventDto.getUserId(), eventDto.getAction(), "NewReview", eventDto.getReviewId(), 1));
            log.info("특정 장소에 첫 리뷰 작성 보너스 포인트 적립");
            personalPoint.pointVariation(1);
        }

        // 텍스트 작성 포인트 적립
        if(!eventDto.getContent().isBlank()){
            pointHistories.add(new PointHistory(eventDto.getUserId(), eventDto.getAction(), "Text", eventDto.getReviewId(), 1));
            log.info("1자 이상 텍스트 작성 포인트 적립");
            personalPoint.pointVariation(1);
        }

        // 사진 첨부 포인트 적립
        if(!eventDto.getAttachedPhotoIds().isEmpty()){
            pointHistories.add(new PointHistory(eventDto.getUserId(), eventDto.getAction(), "Photo", eventDto.getReviewId(), 1));
            log.info("1장 이상 사진 첨부 포인트 적립");
            personalPoint.pointVariation(1);
        }

        pointHistoryRepository.saveAll(pointHistories);
        return personalPointRepository.save(personalPoint);
    }

    // 리뷰 수정시 마일리지 조정
    public PersonalPoint reviewModMileage(EventDto eventDto, Review review, List<AttachedPhoto> attachedPhotoList){
        PersonalPoint personalPoint = personalPointRepository.findById(eventDto.getUserId())
                .map(p -> new PersonalPoint(p.getUserId(), p.getPoint()))
                .orElseThrow(() -> new IllegalArgumentException("리뷰 수정 포인트 적립 실패 : 개인 포인트 내역이 존재하지 않습니다."));

        List<PointHistory> pointHistories = new ArrayList<>();
        if(review.getContent().isBlank() && !eventDto.getContent().isBlank()){ // 텍스트 미작성 -> 텍스트 작성
            pointHistories.add(new PointHistory(eventDto.getUserId(), eventDto.getAction(), "Text", eventDto.getReviewId(), 1));
            log.info("1자 이상 텍스트 작성 포인트 적립");
            personalPoint.pointVariation(1);
        } else if(!review.getContent().isBlank() && eventDto.getContent().isBlank()){ // 텍스트 작성 -> 텍스트 삭제
            pointHistories.add(new PointHistory(eventDto.getUserId(), eventDto.getAction(), "TextRemove", eventDto.getReviewId(), -1));
            log.info("텍스트 삭제 포인트 차감");
            personalPoint.pointVariation(-1);
        }

        if(attachedPhotoList.isEmpty() && !eventDto.getAttachedPhotoIds().isEmpty()){ // 사진 미첨부 -> 사진 첨부
            pointHistories.add(new PointHistory(eventDto.getUserId(), eventDto.getAction(), "Photo", eventDto.getReviewId(), 1));
            log.info("1장 이상 사진 첨부 포인트 적립");
            personalPoint.pointVariation(1);
        } else if(!attachedPhotoList.isEmpty() && eventDto.getAttachedPhotoIds().isEmpty()){ // 사진 첨부 -> 사진 삭제
            pointHistories.add(new PointHistory(eventDto.getUserId(), eventDto.getAction(), "PhotoRemove", eventDto.getReviewId(), -1));
            log.info("사진 삭제 포인트 차감");
            personalPoint.pointVariation(-1);
        }
        pointHistoryRepository.saveAll(pointHistories);
        return personalPointRepository.save(personalPoint);

    }

    // 리뷰 삭제시 마일리지 차감
    public PersonalPoint reviewDeleteMileage(EventDto eventDto){
        PersonalPoint personalPoint = personalPointRepository.findById(eventDto.getUserId())
                .map(p -> new PersonalPoint(p.getUserId(), p.getPoint()))
                .orElseThrow(() -> new IllegalArgumentException("리뷰 수정 포인트 적립 실패 : 개인 포인트 내역이 존재하지 않습니다."));

        Integer reviewPoint = pointHistoryRepository.findPointSumByReviewId(eventDto.getReviewId()); // 작성한 리뷰로 적립한 포인트 합계 조회
        personalPoint.pointVariation(-reviewPoint);
        log.info("리뷰 삭제 포인트 차감 : " + -reviewPoint);

        PointHistory pointHistory = new PointHistory(eventDto.getUserId(), eventDto.getAction(), "Delete Review", eventDto.getReviewId(), -reviewPoint);
        pointHistoryRepository.save(pointHistory); // 포인트 차감 로그 기록
        return personalPointRepository.save(personalPoint);
    }

    // 개인별 포인트 조회
    public Integer personalMileage(UUID userId){
        Integer mileage = personalPointRepository.findPointById(userId);
        if(mileage == null){ // 등록된 개인 포인트 내역이 없으면 포인트 0 리턴
            return 0;
        }
        else{
            return mileage;
        }
    }
}
