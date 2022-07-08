package com.example.clubmileage.repository;

import com.example.clubmileage.entity.AttachedPhoto;
import com.example.clubmileage.entity.PersonalPoint;
import com.example.clubmileage.entity.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.UUID;

public interface AttachedPhotoRepository extends JpaRepository<AttachedPhoto, String> {

    List<AttachedPhoto> findByReview(Review review);
}
