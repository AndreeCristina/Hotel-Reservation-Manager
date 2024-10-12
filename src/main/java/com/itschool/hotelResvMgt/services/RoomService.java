package com.itschool.hotelResvMgt.services;

import com.itschool.hotelResvMgt.models.dtos.RoomDTO;
import com.itschool.hotelResvMgt.models.entities.Room;

import java.util.List;

public interface RoomService {

    Room createRoom(Room room);

    List<RoomDTO> getAvailableRooms(boolean availability);
    RoomDTO mapToRoomDTO(Room room);
}