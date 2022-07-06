package com.example.clubmileage.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import java.util.UUID;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class PersonalPoint {

    @Id
    @Column(columnDefinition = "BINARY(16)")
    private UUID userId;

    @Column(name = "point")
    private Integer point;

    public void pointVariation(Integer point){
        this.point = this.point + point;
    }
}
