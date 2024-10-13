package com.itschool.hotelResvMgt.services;

import com.itschool.hotelResvMgt.models.dtos.RoomDTO;
import com.itschool.hotelResvMgt.models.entities.Room;
import com.itschool.hotelResvMgt.repositories.RoomRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static com.itschool.hotelResvMgt.models.entities.RoomType.*;

@Slf4j
@Service
public class RoomServiceImpl implements RoomService {

    RoomRepository roomRepository;

    public RoomServiceImpl(RoomRepository roomRepository) {
        this.roomRepository = roomRepository;
    }

    @Override
    public List<RoomDTO> getAvailableRooms(boolean availability) {
        return roomRepository.findByAvailability(availability)
                .stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public RoomDTO mapToRoomDTO(Room room) {
        RoomDTO roomDTO = new RoomDTO();
        roomDTO.setId(room.getId());
        roomDTO.setType(room.getType());
        roomDTO.setAvailable(room.isAvailability());
        roomDTO.setNumber(room.getNumber());
        roomDTO.setPricePerNight(room.getPricePerNight());

        return roomDTO;
    }

    @Override
    public RoomDTO mapToDTO(Room room) {
        RoomDTO roomDTO = new RoomDTO();
        roomDTO.setId(room.getId());
        roomDTO.setType(room.getType());
        roomDTO.setAvailable(room.isAvailability());
        roomDTO.setNumber(room.getNumber());
        roomDTO.setPricePerNight(room.getPricePerNight());

        return roomDTO;
    }
}