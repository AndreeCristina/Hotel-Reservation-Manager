package com.itschool.hotelResvMgt.models.dtos;

import com.itschool.hotelResvMgt.models.entities.RoomType;
import lombok.Data;

@Data
public class RoomDTO {

    private Long id;
    private String number;
    private RoomType type;
    private Double pricePerNight;
    private boolean availability;
}