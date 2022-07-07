package com.example.clubmileage.repository;

import com.example.clubmileage.entity.PersonalPoint;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.UUID;

public interface PersonalPointRepository extends JpaRepository<PersonalPoint, UUID> {

    @Query(value = "Select point from personal_point where user_id = :userId", nativeQuery = true)
    Integer findPointById(UUID userId);
}
