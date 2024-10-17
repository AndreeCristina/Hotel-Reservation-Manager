package com.itschool.hotelResvMgt.exceptions;

public class RoomNotFoundException extends RuntimeException {

    public RoomNotFoundException(Long roomId) {
        super("Room with ID " + roomId + " not found");
    }
}