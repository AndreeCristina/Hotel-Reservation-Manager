package com.itschool.hotelResvMgt.models.entities;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
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
    private boolean availability;

    public double getPricePerNight() {
        return switch (type) {
            case STANDARD -> 250.0;
            case SUPERIOR -> 280.0;
            case SUITE -> 500.0;
            case FAMILY -> 820.0;
            case EXECUTIVE -> 1000.0;
            default -> throw new IllegalArgumentException("Unknown room type");
        };
    }
}