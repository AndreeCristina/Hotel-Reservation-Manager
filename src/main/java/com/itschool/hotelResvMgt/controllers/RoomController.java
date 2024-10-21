package com.itschool.hotelResvMgt.controllers;

import com.itschool.hotelResvMgt.models.dtos.RoomDTO;
import com.itschool.hotelResvMgt.services.RoomService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequestMapping("/api/rooms")
@RestController
public class RoomController {

    private final RoomService roomService;

    public RoomController(RoomService roomService) {
        this.roomService = roomService;
    }

    @GetMapping
    public ResponseEntity<List<RoomDTO>> getAvailableRooms() {
        return ResponseEntity.ok(roomService.getAvailableRooms(true));
    }
}