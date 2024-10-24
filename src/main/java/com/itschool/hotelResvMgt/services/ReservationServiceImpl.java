package com.itschool.hotelResvMgt.services;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.itschool.hotelResvMgt.exceptions.GuestNotFoundException;
import com.itschool.hotelResvMgt.exceptions.ReservationNotFoundException;
import com.itschool.hotelResvMgt.exceptions.RoomNotFoundException;
import com.itschool.hotelResvMgt.exceptions.UnavailableRoomException;
import com.itschool.hotelResvMgt.models.dtos.GuestDTO;
import com.itschool.hotelResvMgt.models.dtos.RequestReservationDTO;
import com.itschool.hotelResvMgt.models.dtos.ResponseReservationDTO;
import com.itschool.hotelResvMgt.models.dtos.RoomDTO;
import com.itschool.hotelResvMgt.models.entities.Guest;
import com.itschool.hotelResvMgt.models.entities.Reservation;
import com.itschool.hotelResvMgt.models.entities.Room;
import com.itschool.hotelResvMgt.models.entities.RoomType;
import com.itschool.hotelResvMgt.repositories.GuestRepository;
import com.itschool.hotelResvMgt.repositories.ReservationRepository;
import com.itschool.hotelResvMgt.repositories.RoomRepository;
import jakarta.validation.Valid;
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

    public ReservationServiceImpl(ObjectMapper objectMapper, ReservationRepository reservationRepository, RoomRepository roomRepository, GuestRepository guestRepository) {
        this.objectMapper = objectMapper;
        this.reservationRepository = reservationRepository;
        this.roomRepository = roomRepository;
        this.guestRepository = guestRepository;
    }

    @Override
    public ResponseReservationDTO createReservation(@Valid RequestReservationDTO requestReservationDTO) {
        Reservation reservation = mapToReservation(requestReservationDTO);

        if (!isRoomAvailable(reservation.getRoom(), reservation.getCheckInDate(), reservation.getCheckOutDate())) {
            throw new UnavailableRoomException(reservation.getRoom(), reservation.getCheckInDate(), reservation.getCheckOutDate());
        }

        return createReservationDTOResponse(reservationRepository.save(reservation));
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

        return createReservationDTOResponse(reservationRepository.save(reservation));
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
                    responseReservationDTO.setRoomDTO(objectMapper.convertValue(reservation.getRoom(), RoomDTO.class));
                    responseReservationDTO.setGuestDTO(objectMapper.convertValue(reservation.getGuest(), GuestDTO.class));

                    return responseReservationDTO;
                })
                .toList();
    }

    private ResponseReservationDTO createReservationDTOResponse(Reservation reservation) {
        ResponseReservationDTO responseReservationDTO = objectMapper.convertValue(reservation, ResponseReservationDTO.class);
        responseReservationDTO.setRoomDTO(objectMapper.convertValue(reservation.getRoom(), RoomDTO.class));
        responseReservationDTO.setGuestDTO(objectMapper.convertValue(reservation.getGuest(), GuestDTO.class));

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

        return reservation;
    }

    private boolean isRoomAvailable(Room room, LocalDate checkInDate, LocalDate checkOutDate) {
        List<Reservation> existingReservations = reservationRepository.findByRoomIdAndCheckInDateBetween(room.getId(), checkInDate, checkOutDate);

        if (!existingReservations.isEmpty()) {
            throw new UnavailableRoomException(room, checkInDate, checkOutDate);
        }

        return true;
    }
}