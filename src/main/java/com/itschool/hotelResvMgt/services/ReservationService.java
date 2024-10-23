package com.itschool.hotelResvMgt.services;

import com.itschool.hotelResvMgt.models.dtos.RequestReservationDTO;
import com.itschool.hotelResvMgt.models.dtos.ResponseReservationDTO;
import com.itschool.hotelResvMgt.models.entities.RoomType;

import java.time.LocalDate;
import java.util.List;

public interface ReservationService {

    ResponseReservationDTO createReservation(RequestReservationDTO requestReservationDTO);

    void deleteReservationById(Long reservationId);

    ResponseReservationDTO updateReservationCheckInDate(Long reservationId, RequestReservationDTO updateRequest);

    List<ResponseReservationDTO> getReservations(LocalDate checkInDate, LocalDate checkOutDate, RoomType roomType);
}