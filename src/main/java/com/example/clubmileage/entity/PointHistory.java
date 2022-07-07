package com.example.clubmileage.entity;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.UUID;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(indexes = @Index(name = "i_reviewId", columnList = "ReviewID"))
public class PointHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "userId", columnDefinition = "BINARY(16)")
    private UUID userId;

    @Column(name = "action")
    private String action;

    @Column(name = "actionDetail")
    private String actionDetail;

    @Column(name = "reviewId" ,columnDefinition = "BINARY(16)")
    private UUID reviewId;

    @Column(name = "pointVariation")
    private Integer pointVariation;

    public void init(UUID userId, String action, UUID reviewId, Integer pointVariation){
        this.userId = userId;
        this.action = action;
        this.reviewId = reviewId;
        this.pointVariation = pointVariation;
    }
}
