package com.example.clubmileage.dto;

import lombok.Getter;

import java.util.List;
import java.util.UUID;

@Getter
public class EventDto {
    private String type;

    private String action;

    private UUID reviewId;

    private String content;

    private List<String> attachedPhotoIds;

    private UUID userId;

    private UUID placeId;

}
