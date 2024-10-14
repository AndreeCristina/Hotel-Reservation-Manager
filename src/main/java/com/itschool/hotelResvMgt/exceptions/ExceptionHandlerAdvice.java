package com.itschool.hotelResvMgt.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;

public class ExceptionHandlerAdvice {

    @ExceptionHandler(RoomNotFoundException.class)
    public ResponseEntity<String>
    handleRoomNotFoundException(RoomNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
    }

    @ExceptionHandler(GuestNotFoundException.class)
    public ResponseEntity<String> handleGuestNotFoundException(GuestNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
    }
}
