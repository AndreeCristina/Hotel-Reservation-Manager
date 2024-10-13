package com.itschool.hotelResvMgt.repositories;

import com.itschool.hotelResvMgt.models.entities.Reservation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface ReservationRepository extends JpaRepository<Reservation, Long> {
    List<Reservation> findByRoomIdAndCheckInDateBetween(Long id, LocalDate checkInDate, LocalDate checkOutDate);
}