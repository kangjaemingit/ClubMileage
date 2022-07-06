package com.example.clubmileage.entity;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import java.util.UUID;

@Entity
@AllArgsConstructor
@NoArgsConstructor
public class Review {

    @Id
    @Column(columnDefinition = "BINARY(16)")
    private UUID reviewId;

    @Column(name = "content")
    private String content;

    @Column(name = "userId")
    private UUID userId;

    @Column(name = "attachedPhotoIds")
    private String attachedPhotoIds;

    @Column(name = "placeId")
    private UUID placeId;


    public Review modify(String content, String attachedPhotoIds){
        this.content = content;
        this.attachedPhotoIds = attachedPhotoIds;

        return this;
    }
}