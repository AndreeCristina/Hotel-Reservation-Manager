package com.itschool.hotelResvMgt.services;

import com.itschool.hotelResvMgt.models.dtos.ReservationDTO;
import com.itschool.hotelResvMgt.models.entities.Reservation;

public interface ReservationService {

    public Reservation createReservation(ReservationDTO reservationDTO);
}