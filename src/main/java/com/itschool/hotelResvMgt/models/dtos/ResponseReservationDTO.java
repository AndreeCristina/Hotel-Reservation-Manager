package com.itschool.hotelResvMgt.models.dtos;

import lombok.Data;

import java.time.LocalDate;

@Data
public class ResponseReservationDTO {

    private Long id;
    private GuestDTO guestDTO;
    private RoomDTO roomDTO;
    private Integer guestsNumber;
    private LocalDate checkInDate;
    private LocalDate checkOutDate;
}