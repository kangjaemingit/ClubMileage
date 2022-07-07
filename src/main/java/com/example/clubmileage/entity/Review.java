package com.example.clubmileage.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Table(name = "review")
@EntityListeners(AuditingEntityListener.class)
public class Review {

    @Id
    @Column(columnDefinition = "BINARY(16)")
    private UUID reviewId;

    @Column(name = "content")
    private String content;

    @Column(name = "userId" ,columnDefinition = "BINARY(16)")
    private UUID userId;

    @Column(name = "attachedPhotoIds")
    private String attachedPhotoIds;

    @Column(name = "placeId" ,columnDefinition = "BINARY(16)")
    private UUID placeId;

    @CreatedDate
    private LocalDateTime createDate;

    @LastModifiedDate
    private LocalDateTime lastupdatedDate;

    public Review(UUID reviewId, String content, UUID userId, String attachedPhotoIds, UUID placeId) {
        this.reviewId = reviewId;
        this.content = content;
        this.userId = userId;
        this.attachedPhotoIds = attachedPhotoIds;
        this.placeId = placeId;
    }

    public Review modify(String content, String attachedPhotoIds){
        this.content = content;
        this.attachedPhotoIds = attachedPhotoIds;

        return this;
    }
}
