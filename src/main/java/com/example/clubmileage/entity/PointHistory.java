package com.example.clubmileage.entity;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(indexes = @Index(name = "i_reviewId", columnList = "ReviewID"))
@EntityListeners(AuditingEntityListener.class)
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

    @CreatedDate
    private LocalDateTime createDate;

    public PointHistory(UUID userId, String action, String actionDetail, UUID reviewId, Integer pointVariation) {
        this.userId = userId;
        this.action = action;
        this.actionDetail = actionDetail;
        this.reviewId = reviewId;
        this.pointVariation = pointVariation;
    }
}
