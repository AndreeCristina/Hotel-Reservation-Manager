package com.itschool.hotelResvMgt.services;

import com.itschool.hotelResvMgt.models.entities.Reservation;
import com.itschool.hotelResvMgt.models.entities.RoomType;
import jakarta.persistence.criteria.Join;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDate;


public class ReservationSpecification {

    public static Specification<Reservation> checkInDateContains(LocalDate checkInDate) {
        return (reservation, query, criteriaBuilder) -> checkInDate == null ? null :
                criteriaBuilder.equal(reservation.get("checkInDate"), checkInDate);
    }

    public static Specification<Reservation> checkOutDateContains(LocalDate checkOutDate) {
        return (reservation, query, criteriaBuilder) -> checkOutDate == null ? null :
                criteriaBuilder.equal(reservation.get("checkInDate"), checkOutDate);
    }

    public static Specification<Reservation> findByRoomType(RoomType roomType) {
        return (reservation, query, criteriaBuilder) -> {
            Join<Object, Object> roomJoin = reservation.join("room");
            return criteriaBuilder.equal(roomJoin.get("type"), roomType);
        };
    }
}
