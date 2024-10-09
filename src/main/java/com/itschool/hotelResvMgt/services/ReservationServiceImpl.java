package com.itschool.hotelResvMgt.services;

import com.itschool.hotelResvMgt.models.dtos.ReservationDTO;
import com.itschool.hotelResvMgt.models.entities.Guest;
import com.itschool.hotelResvMgt.models.entities.Reservation;
import com.itschool.hotelResvMgt.models.entities.Room;
import com.itschool.hotelResvMgt.repositories.GuestRepository;
import com.itschool.hotelResvMgt.repositories.ReservationRepository;
import com.itschool.hotelResvMgt.repositories.RoomRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Slf4j
@Service
public class ReservationServiceImpl implements ReservationService {

    @Autowired
    private ReservationRepository reservationRepository;

    @Autowired
    private RoomRepository roomRepository;

    @Autowired
    private GuestRepository guestRepository;

    @Override
    public Reservation createReservation(ReservationDTO reservationDTO) {
        Reservation reservation = new Reservation();
        reservation.setId(reservationDTO.getId());
        reservation.setCheckInDate(reservationDTO.getCheckInDate());
        reservation.setCheckOutDate(reservationDTO.getCheckOutDate());
        reservation.setNumberOfGuests(reservationDTO.getNumberOfGuests());

        Optional<Room> roomOptional = roomRepository.findById(reservationDTO.getRoomId());
        if (roomOptional.isPresent()) {
            reservation.setRoom(roomOptional.get());
        } else {
            throw new IllegalArgumentException("You haven't provided any room.");
        }

        Optional<Guest> guestOptional = guestRepository.findById(reservationDTO.getGuestId());
        if (guestOptional.isPresent()) {
            reservation.setGuest(guestOptional.get());
        } else {
            throw new IllegalArgumentException("You haven't provided any guest.");
        }

        return reservationRepository.save(reservation);
    }
}