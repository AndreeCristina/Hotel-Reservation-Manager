package com.itschool.hotelResvMgt.controllers;

import com.itschool.hotelResvMgt.models.entities.Room;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;


@RestController
public class RoomController {

    @PostMapping("/api/rooms")
    public ResponseEntity<Room> createRoom(@RequestBody Room room) {
        return ResponseEntity.ok(null);
    }
}