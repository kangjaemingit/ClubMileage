package com.example.clubmileage.repository;

import com.example.clubmileage.entity.Review;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface ReviewRepository extends JpaRepository<Review, UUID> {

    @Override
    boolean existsById(UUID reviewId);

    boolean existsByPlaceId(UUID placeId);

}
