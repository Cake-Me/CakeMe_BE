package com.cakeme.backend.dto.cake;

import com.cakeme.backend.dto.response.ResponseDTO;
import com.cakeme.backend.entity.CakeEntity;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CakeRequestDTO {
    private String shape;
    private String flavor;
    private String color1;
    private String color2;
    private String occasion;
    private String theme;
    private String text;

}

