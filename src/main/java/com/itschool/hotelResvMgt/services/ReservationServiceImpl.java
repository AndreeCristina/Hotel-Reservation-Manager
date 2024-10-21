package com.itschool.hotelResvMgt.services;

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
    public ResponseReservationDTO createReservationFromDTO(RequestReservationDTO requestReservationDTO) {
        validateReservationDTO(requestReservationDTO);
        Reservation reservation = mapToReservation(requestReservationDTO);

        if (!checkRoomAvailability(reservation.getRoom(), reservation.getCheckInDate(), reservation.getCheckOutDate())) {
            throw new UnavailableRoomException(reservation.getRoom(), reservation.getCheckInDate(), reservation.getCheckOutDate());
        }
        Reservation savedReservation = reservationRepository.save(reservation);

        return createReservationDTOResponse(savedReservation);
    }

    private ResponseReservationDTO createReservationDTOResponse(Reservation reservation) {
        ResponseReservationDTO responseReservationDTO = objectMapper.convertValue(reservation, ResponseReservationDTO.class);
        responseReservationDTO.setRoomDTO(roomService.mapToRoomDTO(reservation.getRoom()));
        responseReservationDTO.setGuestDTO(guestService.mapToGuestDTO(reservation.getGuest()));

        return responseReservationDTO;
    }

    private Reservation mapToReservation(RequestReservationDTO requestReservationDTO) {
        Reservation reservation = objectMapper.convertValue(requestReservationDTO, Reservation.class);
        reservation.setCheckInDate(requestReservationDTO.getCheckInDate());
        reservation.setCheckOutDate(requestReservationDTO.getCheckOutDate());

        Optional<Room> roomOptional = roomRepository.findById(requestReservationDTO.getRoomId());
        if (roomOptional.isEmpty()) {
            throw new RoomNotFoundException(requestReservationDTO.getRoomId());
        }
        reservation.setRoom(roomOptional.get());

        Optional<Guest> guestOptional = guestRepository.findById(requestReservationDTO.getGuestId());
        if (guestOptional.isEmpty()) {
            throw new GuestNotFoundException(requestReservationDTO.getGuestId());
        }
        reservation.setGuest(guestOptional.get());

        RoomType roomType = reservation.getRoom().getType();

        return reservation;
    }

    private void validateReservationDTO(RequestReservationDTO requestReservationDTO) {
        if (requestReservationDTO.getRoomId() == null) {
            throw new IllegalArgumentException("Room ID is required");
        }

        if (requestReservationDTO.getGuestId() == null) {
            throw new IllegalArgumentException("Guest ID is required");
        }

        if (requestReservationDTO.getGuestsNumber() == null || requestReservationDTO.getGuestsNumber() <= 0) {
            throw new IllegalArgumentException("Number of guests must be a positive integer");
        }

        if (requestReservationDTO.getCheckInDate() == null || requestReservationDTO.getCheckInDate().isAfter(requestReservationDTO.getCheckOutDate())) {
            throw new IllegalArgumentException("Check-in date must be before check-out date");
        }
    }

    private boolean checkRoomAvailability(Room room, LocalDate checkInDate, LocalDate checkOutDate) {
        List<Reservation> existingReservations = reservationRepository.findByRoomIdAndCheckInDateBetween(room.getId(), checkInDate, checkOutDate);

        return existingReservations.isEmpty();
    }

    @Override
    public void deleteReservationById(Long reservationId) {
        reservationRepository.findById(reservationId).orElseThrow(() -> new ReservationNotFoundException("Reservation with id " + reservationId + "not found"));
        reservationRepository.deleteById(reservationId);
        log.info("Reservation with id {} was deleted", reservationId);
    }

    @Override
    public ResponseReservationDTO updateReservationCheckInDate(Long reservationId, RequestReservationDTO updateRequest) {
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
    public List<ResponseReservationDTO> getReservations(LocalDate checkInDate, LocalDate checkOutDate, RoomType roomType) {
        Specification<Reservation> spec = Specification
                .where(ReservationSpecification.checkInDateContains(checkInDate))
                .and(ReservationSpecification.checkOutDateContains(checkOutDate))
                .and(ReservationSpecification.findByRoomType(roomType));

        List<Reservation> reservations = reservationRepository.findAll(spec);
        log.info("{} reservations found", reservations.size());

        return reservations.stream()
                .map(reservation -> {
                    ResponseReservationDTO responseReservationDTO = objectMapper.convertValue(reservation, ResponseReservationDTO.class);
                    responseReservationDTO.setRoomDTO(roomService.mapToRoomDTO(reservation.getRoom()));
                    responseReservationDTO.setGuestDTO(guestService.mapToGuestDTO(reservation.getGuest()));

                    return responseReservationDTO;
                })
                .toList();
    }
}