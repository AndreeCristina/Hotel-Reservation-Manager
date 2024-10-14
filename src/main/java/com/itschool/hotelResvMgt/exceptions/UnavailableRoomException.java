package com.itschool.hotelResvMgt.exceptions;

import com.itschool.hotelResvMgt.models.entities.Room;

import java.time.LocalDate;

public class UnavailableRoomException extends RuntimeException {

    public UnavailableRoomException(Room room, LocalDate checkInDate, LocalDate checkOutDate) {
        super("Room with ID " + room.getId() + " is unavailable for dates " + checkInDate + " to " + checkOutDate);
    }
}
