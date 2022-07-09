package com.example.clubmileage.service;

import com.example.clubmileage.dto.EventDto;
import com.example.clubmileage.entity.AttachedPhoto;
import com.example.clubmileage.entity.PersonalPoint;
import com.example.clubmileage.entity.PointHistory;
import com.example.clubmileage.entity.Review;
import com.example.clubmileage.repository.ReviewRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
class MileageServiceTest {

    @Autowired MileageService mileageService;
    @Autowired ReviewService reviewService;

    @Test
    @Transactional
    void reviewAddMileage_ALLPOINTS() { // 전체 조건 만족
        String type = "REVIEW";
        String action = "ADD";
        UUID reviewId = UUID.fromString("240a0658-dc5f-4878-9381-ebb7b2667772");
        String content = "좋아요!";
        List<String> attachedPhotoIds = new ArrayList<String>(Arrays.asList("e4d1a64e-a531-46de-88d0-ff0ed70c0bb8", "afb0cef2-851d-4a50-bb07-9cc15cbdc332"));
        UUID userId = UUID.fromString("3ede0ef2-92b7-4817-a5f3-0c575361f745");
        UUID placeId = UUID.fromString("2e4baf1c-5acb-4efb-a1af-eddada31b00f");
        EventDto eventDto = new EventDto(type, action, reviewId, content, attachedPhotoIds, userId, placeId);

        // 예상
        PersonalPoint expect = new PersonalPoint(userId, 3);

        // 실제
        PersonalPoint truth = mileageService.reviewAddMileage(eventDto);

        // 비교
        assertEquals(expect.getPoint(), truth.getPoint());
    }

    @Test
    @Transactional
    void reviewAddMileage_NOTNEWREVIEW() { // 새로운 리뷰가 아닐 경우
        // 기존 리뷰 작성
        String type = "REVIEW";
        String action = "ADD";
        UUID reviewId = UUID.fromString("240a0658-dc5f-4878-9381-ebb7b2667771");
        String content = "좋아요!";
        List<String> attachedPhotoIds = new ArrayList<String>(Arrays.asList("e4d1a64e-a531-46de-88d0-ff0ed70c0bb8", "afb0cef2-851d-4a50-bb07-9cc15cbdc332"));
        UUID userId = UUID.fromString("3ede0ef2-92b7-4817-a5f3-0c575361f744");
        UUID placeId = UUID.fromString("2e4baf1c-5acb-4efb-a1af-eddada31b00f");
        EventDto eventDto = new EventDto(type, action, reviewId, content, attachedPhotoIds, userId, placeId);
        reviewService.event(eventDto);

        // 두번쨰 리뷰 작성
        type = "REVIEW";
        action = "ADD";
        reviewId = UUID.fromString("240a0658-dc5f-4878-9381-ebb7b2667772");
        content = "좋아요!";
        attachedPhotoIds = new ArrayList<String>(Arrays.asList("e4d1a64e-a531-46de-88d0-ff0ed70c0bb8", "afb0cef2-851d-4a50-bb07-9cc15cbdc332"));
        userId = UUID.fromString("3ede0ef2-92b7-4817-a5f3-0c575361f745");
        placeId = UUID.fromString("2e4baf1c-5acb-4efb-a1af-eddada31b00f");
        eventDto = new EventDto(type, action, reviewId, content, attachedPhotoIds, userId, placeId);

        // 예상
        PersonalPoint expect = new PersonalPoint(userId, 2);

        // 실제
        PersonalPoint truth = mileageService.reviewAddMileage(eventDto);

        // 비교
        assertEquals(expect.getPoint(), truth.getPoint());
    }

    @Test
    @Transactional
    void reviewAddMileage_NOTTEXT() { // 텍스트가 없을 경우
        String type = "REVIEW";
        String action = "ADD";
        UUID reviewId = UUID.fromString("240a0658-dc5f-4878-9381-ebb7b2667772");
        String content = "";
        List<String> attachedPhotoIds = new ArrayList<String>(Arrays.asList("e4d1a64e-a531-46de-88d0-ff0ed70c0bb8", "afb0cef2-851d-4a50-bb07-9cc15cbdc332"));
        UUID userId = UUID.fromString("3ede0ef2-92b7-4817-a5f3-0c575361f745");
        UUID placeId = UUID.fromString("2e4baf1c-5acb-4efb-a1af-eddada31b00f");
        EventDto eventDto = new EventDto(type, action, reviewId, content, attachedPhotoIds, userId, placeId);

        // 예상
        PersonalPoint expect = new PersonalPoint(userId, 2);

        // 실제
        PersonalPoint truth = mileageService.reviewAddMileage(eventDto);

        // 비교
        assertEquals(expect.getPoint(), truth.getPoint());
    }

    @Test
    @Transactional
    void reviewAddMileage_NOTPHOTO() { // 사진이 없을 경우
        String type = "REVIEW";
        String action = "ADD";
        UUID reviewId = UUID.fromString("240a0658-dc5f-4878-9381-ebb7b2667772");
        String content = "좋아요!";
        List<String> attachedPhotoIds = new ArrayList<String>();
        UUID userId = UUID.fromString("3ede0ef2-92b7-4817-a5f3-0c575361f745");
        UUID placeId = UUID.fromString("2e4baf1c-5acb-4efb-a1af-eddada31b00f");
        EventDto eventDto = new EventDto(type, action, reviewId, content, attachedPhotoIds, userId, placeId);

        // 예상
        PersonalPoint expect = new PersonalPoint(userId, 2);

        // 실제
        PersonalPoint truth = mileageService.reviewAddMileage(eventDto);

        // 비교
        assertEquals(expect.getPoint(), truth.getPoint());
    }


    @Test
    @Transactional
    void reviewModMileage_NOTEXT_TO_TEXT() { // 텍스트 미작성 -> 텍스트 작성
        // 기존 리뷰 작성
        String type = "REVIEW";
        String action = "ADD";
        UUID reviewId = UUID.fromString("240a0658-dc5f-4878-9381-ebb7b2667771");
        String content = "";
        List<String> attachedPhotoIds = new ArrayList<>();
        UUID userId = UUID.fromString("3ede0ef2-92b7-4817-a5f3-0c575361f744");
        UUID placeId = UUID.fromString("2e4baf1c-5acb-4efb-a1af-eddada31b00f");
        EventDto eventDto = new EventDto(type, action, reviewId, content, attachedPhotoIds, userId, placeId);
        reviewService.event(eventDto);

        List<AttachedPhoto> attachedPhotoList = new ArrayList<>();
        Review review = new Review(reviewId, content, userId, attachedPhotoList, placeId, null, null);

        // 두번쨰 리뷰 수정
        action = "MOD";
        content = "좋아요!";
        eventDto = new EventDto(type, action, reviewId, content, attachedPhotoIds, userId, placeId);

        // 예상
        PersonalPoint expect = new PersonalPoint(userId, 2);

        // 실제
        PersonalPoint truth = mileageService.reviewModMileage(eventDto, review, attachedPhotoList);

        // 비교
        assertEquals(expect.getPoint(), truth.getPoint());
    }

    @Test
    @Transactional
    void reviewModMileage_TEXT_TO_NOTEXT() { // 텍스트 작성 -> 텍스트 삭제
        // 기존 리뷰 작성
        String type = "REVIEW";
        String action = "ADD";
        UUID reviewId = UUID.fromString("240a0658-dc5f-4878-9381-ebb7b2667771");
        String content = "좋아요!";
        List<String> attachedPhotoIds = new ArrayList<>();
        UUID userId = UUID.fromString("3ede0ef2-92b7-4817-a5f3-0c575361f744");
        UUID placeId = UUID.fromString("2e4baf1c-5acb-4efb-a1af-eddada31b00f");
        EventDto eventDto = new EventDto(type, action, reviewId, content, attachedPhotoIds, userId, placeId);
        reviewService.event(eventDto);

        List<AttachedPhoto> attachedPhotoList = new ArrayList<>();
        Review review = new Review(reviewId, content, userId, attachedPhotoList, placeId, null, null);

        // 두번쨰 리뷰 수정
        action = "MOD";
        content = "";
        eventDto = new EventDto(type, action, reviewId, content, attachedPhotoIds, userId, placeId);

        // 예상
        PersonalPoint expect = new PersonalPoint(userId, 1);

        // 실제
        PersonalPoint truth = mileageService.reviewModMileage(eventDto, review, attachedPhotoList);

        // 비교
        assertEquals(expect.getPoint(), truth.getPoint());
    }

    @Test
    @Transactional
    void reviewModMileage_NOPHOTO_TO_PHOTO() { // 사진 미첨부 -> 사진 첨부
        // 기존 리뷰 작성
        String type = "REVIEW";
        String action = "ADD";
        UUID reviewId = UUID.fromString("240a0658-dc5f-4878-9381-ebb7b2667771");
        String content = "";
        List<String> attachedPhotoIds = new ArrayList<>();
        UUID userId = UUID.fromString("3ede0ef2-92b7-4817-a5f3-0c575361f744");
        UUID placeId = UUID.fromString("2e4baf1c-5acb-4efb-a1af-eddada31b00f");
        EventDto eventDto = new EventDto(type, action, reviewId, content, attachedPhotoIds, userId, placeId);
        reviewService.event(eventDto);

        List<AttachedPhoto> attachedPhotoList = new ArrayList<>();
        Review review = new Review(reviewId, content, userId, attachedPhotoList, placeId, null, null);

        // 두번쨰 리뷰 수정
        action = "MOD";
        attachedPhotoIds.add("e4d1a64e-a531-46de-88d0-ff0ed70c0bb8");
        attachedPhotoIds.add("e4d1a64e-a531-46de-88d0-ff0ed70c0bb9");
        eventDto = new EventDto(type, action, reviewId, content, attachedPhotoIds, userId, placeId);

        // 예상
        PersonalPoint expect = new PersonalPoint(userId, 2);

        // 실제
        PersonalPoint truth = mileageService.reviewModMileage(eventDto, review, attachedPhotoList);

        // 비교
        assertEquals(expect.getPoint(), truth.getPoint());
    }

    @Test
    @Transactional
    void reviewModMileage_PHOTO_TO_NOPHOTO() { // 사진 첨부 -> 사진 미첨부
        // 기존 리뷰 작성
        String type = "REVIEW";
        String action = "ADD";
        UUID reviewId = UUID.fromString("240a0658-dc5f-4878-9381-ebb7b2667771");
        String content = "";
        List<String> attachedPhotoIds = new ArrayList<String>(Arrays.asList("e4d1a64e-a531-46de-88d0-ff0ed70c0bb8", "afb0cef2-851d-4a50-bb07-9cc15cbdc332"));
        UUID userId = UUID.fromString("3ede0ef2-92b7-4817-a5f3-0c575361f744");
        UUID placeId = UUID.fromString("2e4baf1c-5acb-4efb-a1af-eddada31b00f");
        EventDto eventDto = new EventDto(type, action, reviewId, content, attachedPhotoIds, userId, placeId);
        reviewService.event(eventDto);

        List<AttachedPhoto> attachedPhotoList = new ArrayList<>();
        Review review = new Review(reviewId, content, userId, attachedPhotoList, placeId, null, null);
        attachedPhotoList.add(new AttachedPhoto("e4d1a64e-a531-46de-88d0-ff0ed70c0bb8", review));
        attachedPhotoList.add(new AttachedPhoto("e4d1a64e-a531-46de-88d0-ff0ed70c0bb9", review));
        review.setAttachedPhotoIds(attachedPhotoList);

        // 두번쨰 리뷰 수정
        action = "MOD";
        attachedPhotoIds = new ArrayList<>();
        eventDto = new EventDto(type, action, reviewId, content, attachedPhotoIds, userId, placeId);

        // 예상
        PersonalPoint expect = new PersonalPoint(userId, 1);

        // 실제
        PersonalPoint truth = mileageService.reviewModMileage(eventDto, review, attachedPhotoList);

        // 비교
        assertEquals(expect.getPoint(), truth.getPoint());
    }

    @Test
    @Transactional
    void reviewDeleteMileage() { // 리뷰 삭제 시 포인트 차감
        // 기존 리뷰 작성
        String type = "REVIEW";
        String action = "ADD";
        UUID reviewId = UUID.fromString("240a0658-dc5f-4878-9381-ebb7b2667771");
        String content = "좋아요!";
        List<String> attachedPhotoIds = new ArrayList<String>(Arrays.asList("e4d1a64e-a531-46de-88d0-ff0ed70c0bb8", "afb0cef2-851d-4a50-bb07-9cc15cbdc332"));
        UUID userId = UUID.fromString("3ede0ef2-92b7-4817-a5f3-0c575361f744");
        UUID placeId = UUID.fromString("2e4baf1c-5acb-4efb-a1af-eddada31b00f");
        EventDto eventDto = new EventDto(type, action, reviewId, content, attachedPhotoIds, userId, placeId);
        reviewService.event(eventDto);

        // 예상
        PersonalPoint expect = new PersonalPoint(userId, 0);

        // 실제
        PersonalPoint truth = mileageService.reviewDeleteMileage(eventDto);

        // 비교
        assertEquals(expect.getPoint(), truth.getPoint());
    }

    @Test
    @Transactional
    void personalMileage() { // 입력한 리뷰가 있을 때
        // 기존 리뷰 작성
        String type = "REVIEW";
        String action = "ADD";
        UUID reviewId = UUID.fromString("240a0658-dc5f-4878-9381-ebb7b2667771");
        String content = "좋아요!";
        List<String> attachedPhotoIds = new ArrayList<String>(Arrays.asList("e4d1a64e-a531-46de-88d0-ff0ed70c0bb8", "afb0cef2-851d-4a50-bb07-9cc15cbdc332"));
        UUID userId = UUID.fromString("3ede0ef2-92b7-4817-a5f3-0c575361f744");
        UUID placeId = UUID.fromString("2e4baf1c-5acb-4efb-a1af-eddada31b00f");
        EventDto eventDto = new EventDto(type, action, reviewId, content, attachedPhotoIds, userId, placeId);
        reviewService.event(eventDto);

        // 예상
        Integer expect = 3;

        // 실제
        Integer truth = mileageService.personalMileage(userId);

        // 비교
        assertEquals(expect, truth);

    }

    @Test
    void personalMileage_NOPERSONALPOINT() { // 입력한 리뷰가 없을 때
        UUID userId = UUID.fromString("3ede0ef2-92b7-4817-a5f3-0c575361f744");

        // 예상
        Integer expect = 0;

        // 실제
        Integer truth = mileageService.personalMileage(userId);

        // 비교
        assertEquals(expect, truth);

    }
}