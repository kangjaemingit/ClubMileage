package com.example.clubmileage.service;

import com.example.clubmileage.dto.EventDto;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class ReviewServiceTest {

    @Autowired ReviewService reviewService;

    @Test
    @Transactional
    void event_ADD_Success() { // 등록 성공 : 리뷰 등록 성공
        String type = "REVIEW";
        String action = "ADD";
        UUID reviewId = UUID.fromString("240a0658-dc5f-4878-9381-ebb7b2667772");
        String content = "좋아요!";
        List<String> attachedPhotoIds = new ArrayList<String>(Arrays.asList("e4d1a64e-a531-46de-88d0-ff0ed70c0bb8", "afb0cef2-851d-4a50-bb07-9cc15cbdc332"));
        UUID userId = UUID.fromString("3ede0ef2-92b7-4817-a5f3-0c575361f745");
        UUID placeId = UUID.fromString("2e4baf1c-5acb-4efb-a1af-eddada31b00f");
        EventDto eventDto = new EventDto(type, action, reviewId, content, attachedPhotoIds, userId, placeId);

        // 예상
        ResponseEntity<String> expect = ResponseEntity.ok("Review Add Success");

        // 실제
        ResponseEntity<String> truth = reviewService.event(eventDto);

        // 비교
        assertEquals(expect, truth);

    }

    @Test
    @Transactional
    void event_ADD_Failure() { // 등록 실패 : 중복 리뷰 등록
        String type = "REVIEW";
        String action = "ADD";
        UUID reviewId = UUID.fromString("240a0658-dc5f-4878-9381-ebb7b2667772");
        String content = "좋아요!";
        List<String> attachedPhotoIds = new ArrayList<String>(Arrays.asList("e4d1a64e-a531-46de-88d0-ff0ed70c0bb8", "afb0cef2-851d-4a50-bb07-9cc15cbdc332"));
        UUID userId = UUID.fromString("3ede0ef2-92b7-4817-a5f3-0c575361f745");
        UUID placeId = UUID.fromString("2e4baf1c-5acb-4efb-a1af-eddada31b00f");
        EventDto eventDto = new EventDto(type, action, reviewId, content, attachedPhotoIds, userId, placeId);
        reviewService.reviewAdd(eventDto);

        // 예상
        String expect = "리뷰 등록 실패 : 이미 존재하는 리뷰 입니다.";

        // 실제
        IllegalArgumentException truth = assertThrows(IllegalArgumentException.class, () -> reviewService.event(eventDto));

        // 비교
        assertEquals(expect, truth.getMessage());
    }

    @Test
    @Transactional
    void event_MOD_Success() { // 리뷰 수정 성공
        // 테스트 데이터 입력
        String type = "REVIEW";
        String action = "ADD";
        UUID reviewId = UUID.fromString("240a0658-dc5f-4878-9381-ebb7b2667772");
        String content = "좋아요!";
        List<String> attachedPhotoIds = new ArrayList<String>(Arrays.asList("e4d1a64e-a531-46de-88d0-ff0ed70c0bb8", "afb0cef2-851d-4a50-bb07-9cc15cbdc332"));
        UUID userId = UUID.fromString("3ede0ef2-92b7-4817-a5f3-0c575361f745");
        UUID placeId = UUID.fromString("2e4baf1c-5acb-4efb-a1af-eddada31b00f");
        EventDto eventDto = new EventDto(type, action, reviewId, content, attachedPhotoIds, userId, placeId);
        reviewService.reviewAdd(eventDto);

        // 데이터 수정
        action = "MOD";
        content = "별로에요!";
        attachedPhotoIds = new ArrayList<String>(Arrays.asList("something_photo_id1", "something_photo_id2", "something_photo_id3"));
        eventDto = new EventDto(type, action, reviewId, content, attachedPhotoIds, userId, placeId);

        // 예상
        ResponseEntity<String> expect = ResponseEntity.ok("Review Modify Success");

        // 실제
        ResponseEntity<String> truth = reviewService.event(eventDto);

        // 비교
        assertEquals(expect, truth);
    }

    @Test
    @Transactional
    void event_MOD_Failure() { // 리뷰 수정 실패 : 존재하지 않는 리뷰에 수정 시도
        String type = "REVIEW";
        String action = "MOD";
        UUID reviewId = UUID.fromString("240a0658-dc5f-4878-9381-ebb7b2667772");
        String content = "좋아요!";
        List<String> attachedPhotoIds = new ArrayList<String>(Arrays.asList("e4d1a64e-a531-46de-88d0-ff0ed70c0bb8", "afb0cef2-851d-4a50-bb07-9cc15cbdc332"));
        UUID userId = UUID.fromString("3ede0ef2-92b7-4817-a5f3-0c575361f745");
        UUID placeId = UUID.fromString("2e4baf1c-5acb-4efb-a1af-eddada31b00f");
        EventDto eventDto = new EventDto(type, action, reviewId, content, attachedPhotoIds, userId, placeId);

        // 예상
        String expect = "리뷰 수정 실패 : 존재하지 않는 리뷰입니다.";

        // 실제
        IllegalArgumentException truth = assertThrows(IllegalArgumentException.class, () -> reviewService.event(eventDto));

        // 비교
        assertEquals(expect, truth.getMessage());
    }

    @Test
    @Transactional
    void event_DELETE_Success() { // 리뷰 삭제 성공
        // 테스트 데이터 입력
        String type = "REVIEW";
        String action = "ADD";
        UUID reviewId = UUID.fromString("240a0658-dc5f-4878-9381-ebb7b2667772");
        String content = "좋아요!";
        List<String> attachedPhotoIds = new ArrayList<String>(Arrays.asList("e4d1a64e-a531-46de-88d0-ff0ed70c0bb8", "afb0cef2-851d-4a50-bb07-9cc15cbdc332"));
        UUID userId = UUID.fromString("3ede0ef2-92b7-4817-a5f3-0c575361f745");
        UUID placeId = UUID.fromString("2e4baf1c-5acb-4efb-a1af-eddada31b00f");
        EventDto eventDto = new EventDto(type, action, reviewId, content, attachedPhotoIds, userId, placeId);
        reviewService.reviewAdd(eventDto);

        // 데이터 수정
        action = "DELETE";
        eventDto = new EventDto(type, action, reviewId, content, attachedPhotoIds, userId, placeId);

        // 예상
        ResponseEntity<String> expect = ResponseEntity.ok("Review Delete Success");

        // 실제
        ResponseEntity<String> truth = reviewService.event(eventDto);

        // 비교
        assertEquals(expect, truth);
    }

    @Test
    @Transactional
    void event_DELETE_Failure() { // 리뷰 삭제 실패 : 존재하지 않는 리뷰에 삭제 시도
        String type = "REVIEW";
        String action = "DELETE";
        UUID reviewId = UUID.fromString("240a0658-dc5f-4878-9381-ebb7b2667772");
        String content = "좋아요!";
        List<String> attachedPhotoIds = new ArrayList<String>(Arrays.asList("e4d1a64e-a531-46de-88d0-ff0ed70c0bb8", "afb0cef2-851d-4a50-bb07-9cc15cbdc332"));
        UUID userId = UUID.fromString("3ede0ef2-92b7-4817-a5f3-0c575361f745");
        UUID placeId = UUID.fromString("2e4baf1c-5acb-4efb-a1af-eddada31b00f");
        EventDto eventDto = new EventDto(type, action, reviewId, content, attachedPhotoIds, userId, placeId);

        // 예상
        String expect = "리뷰 삭제 실패 : 존재하지 않는 리뷰입니다.";

        // 실제
        IllegalArgumentException truth = assertThrows(IllegalArgumentException.class, () -> reviewService.event(eventDto));

        // 비교
        assertEquals(expect, truth.getMessage());
    }

    @Test
    @Transactional
    void event_NonAction_Active() { // 존재하지 않는 액션을 입력받은 경우
        String type = "REVIEW";
        String action = "NON";
        UUID reviewId = UUID.fromString("240a0658-dc5f-4878-9381-ebb7b2667772");
        String content = "좋아요!";
        List<String> attachedPhotoIds = new ArrayList<String>(Arrays.asList("e4d1a64e-a531-46de-88d0-ff0ed70c0bb8", "afb0cef2-851d-4a50-bb07-9cc15cbdc332"));
        UUID userId = UUID.fromString("3ede0ef2-92b7-4817-a5f3-0c575361f745");
        UUID placeId = UUID.fromString("2e4baf1c-5acb-4efb-a1af-eddada31b00f");
        EventDto eventDto = new EventDto(type, action, reviewId, content, attachedPhotoIds, userId, placeId);

        // 예상
        ResponseEntity<String> expect = ResponseEntity.badRequest().body("Error : Unknown Action");

        // 실제
        ResponseEntity<String> truth = reviewService.event(eventDto);

        // 비교
        assertEquals(expect, truth);
    }

}