package com.itschool.hotelResvMgt.services;

import com.itschool.hotelResvMgt.models.entities.RoomType;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class RoomPricingServiceImpl implements RoomPricingService {

    @Value("${room.price.standard}")
    private double standardPrice;

    @Value("${room.price.superior}")
    private double superiorPrice;

    @Value("${room.price.suite}")
    private double suitePrice;

    @Value("${room.price.family}")
    private double familyPrice;

    @Value("${room.price.executive}")
    private double executivePrice;

    public double getPricePerNight(RoomType type) {
        return switch (type) {
            case STANDARD -> standardPrice;
            case SUPERIOR -> superiorPrice;
            case SUITE -> suitePrice;
            case FAMILY -> familyPrice;
            case EXECUTIVE -> executivePrice;
            default -> throw new IllegalArgumentException("Unknown room type: " + type);
        };
    }
}