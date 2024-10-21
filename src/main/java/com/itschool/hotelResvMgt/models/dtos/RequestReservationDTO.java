package com.itschool.hotelResvMgt.models.dtos;

import lombok.Data;

import java.time.LocalDate;

@Data
public class RequestReservationDTO {

    private Long id;
    private Long guestId;
    private Long roomId;
    private Integer guestsNumber;
    private LocalDate checkInDate;
    private LocalDate checkOutDate;
}