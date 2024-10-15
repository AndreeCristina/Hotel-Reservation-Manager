package com.itschool.hotelResvMgt.services;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.itschool.hotelResvMgt.exceptions.GuestNotFoundException;
import com.itschool.hotelResvMgt.exceptions.RoomNotFoundException;
import com.itschool.hotelResvMgt.exceptions.UnavailableRoomException;
import com.itschool.hotelResvMgt.models.dtos.ReservationDTORequest;
import com.itschool.hotelResvMgt.models.dtos.ReservationDTOResponse;
import com.itschool.hotelResvMgt.models.entities.Guest;
import com.itschool.hotelResvMgt.models.entities.Reservation;
import com.itschool.hotelResvMgt.models.entities.Room;
import com.itschool.hotelResvMgt.models.entities.RoomType;
import com.itschool.hotelResvMgt.repositories.GuestRepository;
import com.itschool.hotelResvMgt.repositories.ReservationRepository;
import com.itschool.hotelResvMgt.repositories.RoomRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
public class ReservationServiceImpl implements ReservationService {

    private final ObjectMapper objectMapper;
    private final ReservationRepository reservationRepository;
    private final RoomRepository roomRepository;
    private final GuestRepository guestRepository;
    private final RoomService roomService;
    private final GuestService guestService;

    public ReservationServiceImpl(ObjectMapper objectMapper, ReservationRepository reservationRepository,
                                  RoomRepository roomRepository, GuestRepository guestRepository,
                                  RoomService roomService, GuestService guestService) {
        this.objectMapper = objectMapper;
        this.reservationRepository = reservationRepository;
        this.roomRepository = roomRepository;
        this.guestRepository = guestRepository;
        this.roomService = roomService;
        this.guestService = guestService;
    }

    @Override
    public ReservationDTOResponse createReservationFromDTO(ReservationDTORequest reservationDTORequest) {
        validateReservationDTO(reservationDTORequest);
        Reservation reservation = mapToReservation(reservationDTORequest);

        if (!checkRoomAvailability(reservation.getRoom(), reservation.getCheckInDate(), reservation.getCheckOutDate())) {
            throw new UnavailableRoomException(reservation.getRoom(), reservation.getCheckInDate(), reservation.getCheckOutDate());
        }
        Reservation savedReservation = reservationRepository.save(reservation);

        return createReservationDTOResponse(savedReservation);
    }

    private ReservationDTOResponse createReservationDTOResponse(Reservation reservation) {
        ReservationDTOResponse reservationDTOResponse = objectMapper.convertValue(reservation, ReservationDTOResponse.class);
        reservationDTOResponse.setRoomDTO(roomService.mapToRoomDTO(reservation.getRoom()));
        reservationDTOResponse.setGuestDTO(guestService.mapToGuestDTO(reservation.getGuest()));

        return reservationDTOResponse;
    }

    private Reservation mapToReservation(ReservationDTORequest reservationDTORequest) {
        Reservation reservation = objectMapper.convertValue(reservationDTORequest, Reservation.class);

        Optional<Room> roomOptional = roomRepository.findById(reservationDTORequest.getRoomId());
        if (roomOptional.isEmpty()) {
            throw new RoomNotFoundException(reservationDTORequest.getRoomId());
        }
        reservation.setRoom(roomOptional.get());

        Optional<Guest> guestOptional = guestRepository.findById(reservationDTORequest.getGuestId());
        if (guestOptional.isEmpty()) {
            throw new GuestNotFoundException(reservationDTORequest.getGuestId());
        }
        reservation.setGuest(guestOptional.get());

        return reservation;
    }

    private void validateReservationDTO(ReservationDTORequest reservationDTORequest) {
        if (reservationDTORequest.getRoomId() == null) {
            throw new IllegalArgumentException("Room ID is required");
        }

        if (reservationDTORequest.getGuestId() == null) {
            throw new IllegalArgumentException("Guest ID is required");
        }

        if (reservationDTORequest.getNumberOfGuests() == null || reservationDTORequest.getNumberOfGuests() <= 0) {
            throw new IllegalArgumentException("Number of guests must be a positive integer");
        }

        if (reservationDTORequest.getCheckInDate() == null || reservationDTORequest.getCheckInDate().isAfter(reservationDTORequest.getCheckOutDate())) {
            throw new IllegalArgumentException("Check-in date must be before check-out date");
        }
    }

    private boolean checkRoomAvailability(Room room, LocalDate checkInDate, LocalDate checkOutDate) {
        List<Reservation> existingReservations = reservationRepository.findByRoomIdAndCheckInDateBetween(room.getId(), checkInDate, checkOutDate);

        return existingReservations.isEmpty();
    }

    @Override
    public void deleteReservationById(Long reservationId) {
        reservationRepository.deleteById(reservationId);
    }

    @Override
    public ReservationDTOResponse updateReservationCheckInDate(Long reservationId, ReservationDTORequest updateRequest) {
        Optional<Reservation> reservationOptional = reservationRepository.findById(reservationId);

        if (reservationOptional.isEmpty()) {
            throw new IllegalArgumentException("Reservation not found");
        }
        Reservation reservation = reservationOptional.get();

        if (updateRequest.getCheckInDate() != null) {
            reservation.setCheckInDate(updateRequest.getCheckInDate());
        }
        Reservation savedReservation = reservationRepository.save(reservation);

        return createReservationDTOResponse(savedReservation);
    }

    @Override
    public List<ReservationDTOResponse> getReservations(LocalDate checkInDate, LocalDate checkOutDate, RoomType roomType) {
        Specification<Reservation> spec = Specification
                .where(ReservationSpecification.checkInDateContains(checkInDate))
                .and(ReservationSpecification.checkOutDateContains(checkOutDate))
                .and(ReservationSpecification.findByRoomType(roomType));

        List<Reservation> reservations = reservationRepository.findAll(spec);
        log.info("{} reservations found", reservations.size());

        return reservations.stream()
                .map(reservation -> objectMapper.convertValue(reservation, ReservationDTOResponse.class))
                .toList();
    }
}