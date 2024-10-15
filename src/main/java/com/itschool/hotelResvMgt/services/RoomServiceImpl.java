package com.itschool.hotelResvMgt.services;

import com.fasterxml.jackson.databind.ObjectMapper;
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

    private final RoomRepository roomRepository;
    private final ObjectMapper objectMapper;

    public RoomServiceImpl(RoomRepository roomRepository, ObjectMapper objectMapper) {
        this.roomRepository = roomRepository;
        this.objectMapper = objectMapper;
    }

    @Override
    public List<RoomDTO> getAvailableRooms(boolean availability) {
        return roomRepository.findByAvailability(availability)
                .stream()
                .map(this::mapToRoomDTO)
                .collect(Collectors.toList());
    }

    @Override
    public RoomDTO mapToRoomDTO(Room room) {
        try {
            RoomDTO roomDTO = objectMapper.convertValue(room, RoomDTO.class);
            roomDTO.setType(room.getType());

            return roomDTO;
        } catch (IllegalArgumentException e) {
            log.error("Error converting Room to RoomDTO: {}", e.getMessage());
            throw e;
        }
    }
}