package com.itschool.hotelResvMgt.models.dtos;

import com.itschool.hotelResvMgt.models.entities.RoomType;
import lombok.Data;

import java.time.LocalDate;

@Data
public class ReservationDTOResponse {

    private Long id;
    private GuestDTO guestDTO;
    private RoomDTO roomDTO;
    private Integer numberOfGuests;
    private LocalDate checkInDate;
    private LocalDate checkOutDate;
}