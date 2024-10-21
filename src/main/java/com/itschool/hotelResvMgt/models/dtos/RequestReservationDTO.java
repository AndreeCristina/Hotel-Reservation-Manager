package com.itschool.hotelResvMgt.models.dtos;

import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

import java.time.LocalDate;

@Data
public class RequestReservationDTO {

    @NotNull(message = "Room ID is required")
    private Long roomId;

    @NotNull(message = "Guest ID is required")
    private Long guestId;

    @NotNull(message = "Number of guests is required")
    @Positive(message = "Number of guests must be a positive integer")
    private Integer guestsNumber;

    @NotNull(message = "Check-in date is required")
    @FutureOrPresent(message = "Check-in date must be in the present or future")
    private LocalDate checkInDate;

    @NotNull(message = "Check-out date is required")
    @FutureOrPresent(message = "Check-out date must be in the present or future")
    private LocalDate checkOutDate;
}