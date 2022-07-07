package com.example.clubmileage.repository;

import com.example.clubmileage.entity.PointHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.UUID;

public interface PointHistoryRepository extends JpaRepository<PointHistory, Long> {

    @Query(value = "select SUM(point_variation) from point_history where review_id = :reviewId", nativeQuery = true)
    Integer findPointSumByReviewId(UUID reviewId);
}
