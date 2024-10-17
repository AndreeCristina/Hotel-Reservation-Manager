package com.itschool.hotelResvMgt.exceptions;

public class GuestNotFoundException extends RuntimeException {

    public GuestNotFoundException(Long guestId) {
        super("Guest with ID " + guestId + " not found");
    }
}