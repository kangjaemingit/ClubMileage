package com.example.clubmileage.entity;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;

@Entity
@NoArgsConstructor
@AllArgsConstructor
public class AttachedPhoto {
    @Id
    @Column(name = "attachedPhotoIds")
    private String attachedPhotoIds;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "reviewId")
    private Review review;

}
