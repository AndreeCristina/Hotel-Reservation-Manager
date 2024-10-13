package com.itschool.hotelResvMgt.services;

import com.itschool.hotelResvMgt.models.dtos.ReservationDTORequest;
import com.itschool.hotelResvMgt.models.dtos.ReservationDTOResponse;
import com.itschool.hotelResvMgt.models.entities.Guest;
import com.itschool.hotelResvMgt.models.entities.Reservation;
import com.itschool.hotelResvMgt.models.entities.Room;
import com.itschool.hotelResvMgt.repositories.GuestRepository;
import com.itschool.hotelResvMgt.repositories.ReservationRepository;
import com.itschool.hotelResvMgt.repositories.RoomRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Slf4j
@Service
public class ReservationServiceImpl implements ReservationService {

    private ReservationRepository reservationRepository;
    private RoomRepository roomRepository;
    private GuestRepository guestRepository;
    private RoomService roomService;
    private GuestService guestService;

    public ReservationServiceImpl(ReservationRepository reservationRepository, RoomRepository roomRepository,
                                  GuestRepository guestRepository, RoomService roomService, GuestService guestService) {
        this.reservationRepository = reservationRepository;
        this.roomRepository = roomRepository;
        this.guestRepository = guestRepository;
        this.roomService = roomService;
        this.guestService = guestService;
    }

    @Override
    public ReservationDTOResponse createReservationFromDTO(ReservationDTORequest reservationDTORequest) {
        Reservation reservation = mapToReservation(reservationDTORequest);
        Reservation savedReservation = reservationRepository.save(reservation);

        return mapToReservationDTO(savedReservation);
    }

    private ReservationDTOResponse mapToReservationDTO(Reservation reservation) {
        ReservationDTOResponse reservationDTOResponse = new ReservationDTOResponse();
        reservationDTOResponse.setId(reservation.getId());
        reservationDTOResponse.setCheckOutDate(reservation.getCheckOutDate());
        reservationDTOResponse.setCheckInDate(reservation.getCheckInDate());
        reservationDTOResponse.setRoomDTO(roomService.mapToRoomDTO(reservation.getRoom()));
        reservationDTOResponse.setGuestDTO(guestService.mapToGuestDTO(reservation.getGuest()));
        reservationDTOResponse.setNumberOfGuests(reservation.getNumberOfGuests());

        return reservationDTOResponse;
    }

    private Reservation mapToReservation(ReservationDTORequest reservationDTORequest) {
        Reservation reservation = new Reservation();
        reservation.setId(reservationDTORequest.getId());
        reservation.setCheckInDate(reservationDTORequest.getCheckInDate());
        reservation.setCheckOutDate(reservationDTORequest.getCheckOutDate());
        reservation.setNumberOfGuests(reservationDTORequest.getNumberOfGuests());

        Optional<Room> roomOptional = roomRepository.findById(reservationDTORequest.getRoomId());
        if (roomOptional.isPresent()) {
            reservation.setRoom(roomOptional.get());
        } else {
            throw new IllegalArgumentException("You haven't provided any room.");
        }

        Optional<Guest> guestOptional = guestRepository.findById(reservationDTORequest.getGuestId());
        if (guestOptional.isPresent()) {
            reservation.setGuest(guestOptional.get());
        } else {
            throw new IllegalArgumentException("You haven't provided any guest.");
        }
        return reservation;
    }

    public void deleteReservationById(Long reservationId) {
        reservationRepository.deleteById(reservationId);
    }
}