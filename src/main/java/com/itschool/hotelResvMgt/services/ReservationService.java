package com.itschool.hotelResvMgt.services;

import com.itschool.hotelResvMgt.models.dtos.ReservationDTORequest;
import com.itschool.hotelResvMgt.models.dtos.ReservationDTOResponse;
import com.itschool.hotelResvMgt.models.entities.RoomType;

import java.time.LocalDate;
import java.util.List;

public interface ReservationService {

    public ReservationDTOResponse createReservationFromDTO(ReservationDTORequest reservationDTORequest);

    public void deleteReservationById(Long reservationId);

    public ReservationDTOResponse updateReservationCheckInDate(Long reservationId, ReservationDTORequest updateRequest);

    List<ReservationDTOResponse> getReservations(LocalDate checkInDate, LocalDate checkOutDate, RoomType roomType);
}