package com.itschool.hotelResvMgt.models.entities;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "rooms")
public class Room {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "room_id")
    private Long id;

    @Column(name = "room_no", nullable = false)
    private String number;

    @Enumerated(EnumType.STRING)
    @Column(name = "room_type", nullable = false)
    private RoomType type;

    @Column(name = "room_price", nullable = false)
    private Double pricePerNight;

    @Column(name = "availability", nullable = false)
    private boolean available;

    public double getPricePerNight() {
        return switch (type) {
            case STANDARD -> 50.0;
            case SUPERIOR -> 80.0;
            case SUITE -> 100;
            case FAMILY -> 120;
            case EXECUTIVE -> 200;
            default -> throw new IllegalArgumentException("Unknown room type");
        };
    }
}