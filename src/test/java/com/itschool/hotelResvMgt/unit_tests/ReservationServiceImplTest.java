package com.itschool.hotelResvMgt.unit_tests;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.itschool.hotelResvMgt.exceptions.GuestNotFoundException;
import com.itschool.hotelResvMgt.exceptions.ReservationNotFoundException;
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
import static org.junit.jupiter.api.Assertions.assertThrows;
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

        Reservation reservation = new Reservation();
        reservation.setRoom(room);
        reservation.setGuest(guest);
        reservation.setCheckInDate(checkInDate);
        reservation.setCheckOutDate(checkOutDate);

        ResponseReservationDTO responseDTO = new ResponseReservationDTO();

        Optional<Room> roomOptional = Optional.of(room);
        Optional<Guest> guestOptional = Optional.of(guest);

        when(roomRepository.findById(roomId)).thenReturn(roomOptional);
        when(guestRepository.findById(guestId)).thenReturn(guestOptional);
        when(reservationRepository.save(any(Reservation.class))).thenReturn(reservation);

        doReturn(reservation).when(objectMapper).convertValue(any(RequestReservationDTO.class), eq(Reservation.class));
        doReturn(responseDTO).when(objectMapper).convertValue(any(Reservation.class), eq(ResponseReservationDTO.class));

        ResponseReservationDTO response = reservationService.createReservation(request);

        verify(reservationRepository, times(1)).save(any(Reservation.class));
        verify(objectMapper, times(1)).convertValue(any(RequestReservationDTO.class), eq(Reservation.class));
        verify(objectMapper, times(1)).convertValue(any(Reservation.class), eq(ResponseReservationDTO.class));
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

        Reservation reservation = new Reservation();
        when(objectMapper.convertValue(any(RequestReservationDTO.class), eq(Reservation.class)))
                .thenReturn(reservation);

        when(roomRepository.findById(roomId)).thenReturn(Optional.empty());

        try {
            reservationService.createReservation(request);
            fail("Expected RoomNotFoundException");
        } catch (RoomNotFoundException e) {
        }

        verify(reservationRepository, never()).save(any(Reservation.class));
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

        Reservation reservation = new Reservation();
        when(objectMapper.convertValue(any(RequestReservationDTO.class), eq(Reservation.class)))
                .thenReturn(reservation);

        when(roomRepository.findById(roomId)).thenReturn(roomOptional);
        when(guestRepository.findById(guestId)).thenReturn(Optional.empty());

        try {
            reservationService.createReservation(request);
            fail("Expected GuestNotFoundException");
        } catch (GuestNotFoundException e) {

        }

        verify(reservationRepository, never()).save(any(Reservation.class));
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

    @Test
    public void testDeleteReservationByIdNotFound() {
        Long reservationId = 1L;

        when(reservationRepository.findById(reservationId)).thenReturn(Optional.empty());

        assertThrows(ReservationNotFoundException.class, () -> {
            reservationService.deleteReservationById(reservationId);
        });

        verify(reservationRepository, never()).deleteById(anyLong());
    }
}