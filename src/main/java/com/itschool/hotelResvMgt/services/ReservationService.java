package com.itschool.hotelResvMgt.services;

import com.itschool.hotelResvMgt.models.dtos.ReservationDTORequest;
import com.itschool.hotelResvMgt.models.dtos.ReservationDTOResponse;

public interface ReservationService {

    public ReservationDTOResponse createReservationFromDTO(ReservationDTORequest reservationDTORequest);
    public void deleteReservationById(Long reservationId);
}