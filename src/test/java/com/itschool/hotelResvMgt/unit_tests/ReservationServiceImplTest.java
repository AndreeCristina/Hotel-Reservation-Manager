package com.itschool.hotelResvMgt.unit_tests;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.itschool.hotelResvMgt.exceptions.GuestNotFoundException;
import com.itschool.hotelResvMgt.exceptions.RoomNotFoundException;
import com.itschool.hotelResvMgt.exceptions.UnavailableRoomException;
import com.itschool.hotelResvMgt.models.dtos.RequestReservationDTO;
import com.itschool.hotelResvMgt.models.dtos.ResponseReservationDTO;
import com.itschool.hotelResvMgt.models.entities.Guest;
import com.itschool.hotelResvMgt.models.entities.Reservation;
import com.itschool.hotelResvMgt.models.entities.Room;
import com.itschool.hotelResvMgt.repositories.GuestRepository;
import com.itschool.hotelResvMgt.repositories.ReservationRepository;
import com.itschool.hotelResvMgt.repositories.RoomRepository;
import com.itschool.hotelResvMgt.services.GuestService;
import com.itschool.hotelResvMgt.services.ReservationServiceImpl;
import com.itschool.hotelResvMgt.services.RoomService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Optional;

import static org.assertj.core.api.Fail.fail;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ReservationServiceImplTest {

    @Mock
    private ObjectMapper objectMapper;

    @Mock
    private ReservationRepository reservationRepository;

    @Mock
    private RoomRepository roomRepository;

    @Mock
    private GuestRepository guestRepository;

    @Mock
    private RoomService roomService;

    @Mock
    private GuestService guestService;

    @InjectMocks
    private ReservationServiceImpl reservationService;

    @BeforeEach
    public void setUp() {
        reservationService = new ReservationServiceImpl(objectMapper,
                reservationRepository, roomRepository, guestRepository, roomService, guestService);
    }

    @Test
    public void testCreateReservationSuccess() throws Exception {
        Long roomId = 1L;
        Long guestId = 2L;
        LocalDate checkInDate = LocalDate.now().plusDays(2);
        LocalDate checkOutDate = LocalDate.now().plusDays(5);

        RequestReservationDTO request = new RequestReservationDTO();
        request.setRoomId(roomId);
        request.setGuestId(guestId);
        request.setCheckInDate(checkInDate);
        request.setCheckOutDate(checkOutDate);

        Room room = new Room();
        room.setId(roomId);
        Guest guest = new Guest();
        guest.setId(guestId);

        Optional<Room> roomOptional = Optional.of(room);
        Optional<Guest> guestOptional = Optional.of(guest);

        when(roomRepository.findById(roomId)).thenReturn(roomOptional);
        when(guestRepository.findById(guestId)).thenReturn(guestOptional);
        when(reservationRepository.save(any(Reservation.class))).thenReturn(new Reservation());

        Reservation reservation = new Reservation();
        reservation.setCheckInDate(request.getCheckInDate());
        reservation.setCheckOutDate(request.getCheckOutDate());
        when(reservationRepository.save(reservation)).thenReturn(reservation);

        ResponseReservationDTO response = reservationService.createReservation(request);

        verify(reservationRepository, times(1)).save(any(Reservation.class));
        verify(objectMapper, times(2)).convertValue((Object) any(), (Class<Object>) any());
        assertNotNull(response);
    }

    @Test
    public void testCreateReservationRoomNotFound() throws Exception {
        Long roomId = 1L;
        Long guestId = 2L;
        LocalDate checkInDate = LocalDate.now().plusDays(2);
        LocalDate checkOutDate = LocalDate.now().plusDays(5);

        RequestReservationDTO request = new RequestReservationDTO();
        request.setRoomId(roomId);
        request.setGuestId(guestId);
        request.setCheckInDate(checkInDate);
        request.setCheckOutDate(checkOutDate);

        when(roomRepository.findById(roomId)).thenReturn(Optional.empty());

        try {
            reservationService.createReservation(request);
            fail("Expected RoomNotFoundException");
        } catch (RoomNotFoundException e) {
        }
    }

    @Test
    public void testCreateReservationGuestNotFound() throws Exception {
        Long roomId = 1L;
        Long guestId = 2L;
        LocalDate checkInDate = LocalDate.now().plusDays(2);
        LocalDate checkOutDate = LocalDate.now().plusDays(5);

        RequestReservationDTO request = new RequestReservationDTO();
        request.setRoomId(roomId);
        request.setGuestId(guestId);
        request.setCheckInDate(checkInDate);
        request.setCheckOutDate(checkOutDate);

        Room room = new Room();
        room.setId(roomId);
        Optional<Room> roomOptional = Optional.of(room);

        when(roomRepository.findById(roomId)).thenReturn(roomOptional);
        when(guestRepository.findById(guestId)).thenReturn(Optional.empty());

        try {
            reservationService.createReservation(request);
            fail("Expected GuestNotFoundException");
        } catch (GuestNotFoundException e) {
        }
    }

    @Test
    public void testCreateReservationUnavailableRoom() throws Exception {
        Long roomId = 1L;
        Long guestId = 2L;
        LocalDate checkInDate = LocalDate.now().plusDays(2);
        LocalDate checkOutDate = LocalDate.now().plusDays(5);

        RequestReservationDTO request = new RequestReservationDTO();
        request.setRoomId(roomId);
        request.setGuestId(guestId);
        request.setCheckInDate(checkInDate);
        request.setCheckOutDate(checkOutDate);

        try {
            reservationService.createReservation(request);
            fail("Expected UnavailableRoomException");
        } catch (UnavailableRoomException e) {
        } catch (Exception e) {
        }
    }
}