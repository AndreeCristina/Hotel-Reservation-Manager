package com.itschool.hotelResvMgt.unit_tests;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.itschool.hotelResvMgt.models.dtos.RoomDTO;
import com.itschool.hotelResvMgt.models.entities.Room;
import com.itschool.hotelResvMgt.models.entities.RoomType;
import com.itschool.hotelResvMgt.repositories.RoomRepository;
import com.itschool.hotelResvMgt.services.RoomPricingServiceImpl;
import com.itschool.hotelResvMgt.services.RoomServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

class RoomServiceImplTest {

    @Mock
    private RoomRepository roomRepository;

    @Mock
    private ObjectMapper objectMapper;

    @Mock
    private RoomPricingServiceImpl roomPricingServiceImpl;

    @InjectMocks
    private RoomServiceImpl roomService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetAvailableRooms() {
        Room room1 = new Room();
        room1.setId(1L);
        room1.setType(RoomType.STANDARD);
        room1.setAvailability(true);

        Room room2 = new Room();
        room2.setId(2L);
        room2.setType(RoomType.SUPERIOR);
        room2.setAvailability(true);

        List<Room> availableRooms = Arrays.asList(room1, room2);

        when(roomRepository.findByAvailability(true)).thenReturn(availableRooms);
        when(objectMapper.convertValue(room1, RoomDTO.class)).thenReturn(new RoomDTO());
        when(objectMapper.convertValue(room2, RoomDTO.class)).thenReturn(new RoomDTO());
        when(roomPricingServiceImpl.getPricePerNight(RoomType.STANDARD)).thenReturn(250.0);
        when(roomPricingServiceImpl.getPricePerNight(RoomType.SUPERIOR)).thenReturn(280.0);

        List<RoomDTO> result = roomService.getAvailableRooms(true);

        assertEquals(2, result.size());
        assertEquals(250.0, result.get(0).getPricePerNight());
        assertEquals(280.0, result.get(1).getPricePerNight());
    }
}