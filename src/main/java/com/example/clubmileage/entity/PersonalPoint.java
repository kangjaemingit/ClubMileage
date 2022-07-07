package com.example.clubmileage.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.UUID;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Table(indexes = @Index(name = "i_userId", columnList = "userId"))
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
