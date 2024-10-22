package com.itschool.hotelResvMgt.services;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.itschool.hotelResvMgt.models.dtos.RoomDTO;
import com.itschool.hotelResvMgt.models.entities.Room;
import com.itschool.hotelResvMgt.repositories.RoomRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;


@Slf4j
@Service
public class RoomServiceImpl implements RoomService {

    private final RoomRepository roomRepository;
    private final ObjectMapper objectMapper;
    private final RoomPricingService roomPricingService;

    public RoomServiceImpl(RoomRepository roomRepository, ObjectMapper objectMapper, RoomPricingService roomPricingService) {
        this.roomRepository = roomRepository;
        this.objectMapper = objectMapper;
        this.roomPricingService = roomPricingService;
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
        RoomDTO roomDTO = objectMapper.convertValue(room, RoomDTO.class);
        roomDTO.setType(room.getType());

        roomDTO.setPricePerNight(roomPricingService.getPricePerNight(room.getType()));

        return roomDTO;
    }
}