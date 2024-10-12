package com.itschool.hotelResvMgt.models.dtos;

import lombok.Data;

import java.time.LocalDate;

@Data
public class ReservationDTORequest {

    private Long id;
    private Long guestId;
    private Long roomId;
    private Integer numberOfGuests;
    private LocalDate checkInDate;
    private LocalDate checkOutDate;
}