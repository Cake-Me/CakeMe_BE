package com.cakeme.backend.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

// @Setter 사용 금지
@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CakeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String shape;
    private String flavor;
    private String color1;
    private String color2;
    private String occasion;
    private String theme;
    private String imageUrl;
    private String text;

    // 필요한 생성자
    public CakeEntity(String shape, String flavor, String color1, String color2, String occasion, String theme, String imageUrl, String text) {
        this.shape = shape;
        this.flavor = flavor;
        this.color1 = color1;
        this.color2 = color2;
        this.occasion = occasion;
        this.theme = theme;
        this.imageUrl = imageUrl;
        this.text = text;
    }
}