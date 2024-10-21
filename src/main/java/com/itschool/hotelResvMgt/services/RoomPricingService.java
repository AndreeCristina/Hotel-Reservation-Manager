package com.itschool.hotelResvMgt.services;

import com.itschool.hotelResvMgt.models.entities.RoomType;

public interface RoomPricingService {

    double getPricePerNight(RoomType type);
}