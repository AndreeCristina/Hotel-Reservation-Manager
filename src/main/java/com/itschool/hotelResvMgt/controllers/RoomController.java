package com.itschool.hotelResvMgt.controllers;

import com.itschool.hotelResvMgt.models.dtos.RoomDTO;
import com.itschool.hotelResvMgt.models.entities.Room;
import com.itschool.hotelResvMgt.services.RoomService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class RoomController {

    private RoomService roomService;

    public RoomController(RoomService roomService) {
        this.roomService = roomService;
    }

    @GetMapping("api/rooms")
    public ResponseEntity<List<RoomDTO>> getAvailableRooms() {
        List<RoomDTO> availableRooms = roomService.getAvailableRooms(true);

        return ResponseEntity.ok(availableRooms);
    }
}