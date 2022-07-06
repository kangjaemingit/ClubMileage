package com.example.clubmileage.repository;

import com.example.clubmileage.entity.PersonalPoint;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface PersonalPointRepository extends JpaRepository<PersonalPoint, UUID> {

}
