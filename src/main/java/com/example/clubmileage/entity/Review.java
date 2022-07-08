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

    @OneToMany(mappedBy = "review", cascade = CascadeType.ALL)
    private List<AttachedPhoto> attachedPhotoIds = new ArrayList<>();

    @Column(name = "placeId" ,columnDefinition = "BINARY(16)")
    private UUID placeId;

    @CreatedDate
    private LocalDateTime createDate;

    @LastModifiedDate
    private LocalDateTime lastupdatedDate;

    public Review(UUID reviewId, String content, UUID userId, UUID placeId) {
        this.reviewId = reviewId;
        this.content = content;
        this.userId = userId;
        this.placeId = placeId;
    }

    public void setAttachedPhotoIds(List<AttachedPhoto> attachedPhotoIds) {
        this.attachedPhotoIds = attachedPhotoIds;
    }

    public Review modify(String content, List<AttachedPhoto> attachedPhotoIds){
        this.content = content;
        this.attachedPhotoIds = attachedPhotoIds;

        return this;
    }
}
