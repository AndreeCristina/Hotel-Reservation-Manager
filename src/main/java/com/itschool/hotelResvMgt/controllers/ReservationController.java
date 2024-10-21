package com.itschool.hotelResvMgt.controllers;

import com.itschool.hotelResvMgt.models.dtos.RequestReservationDTO;
import com.itschool.hotelResvMgt.models.dtos.ResponseReservationDTO;
import com.itschool.hotelResvMgt.models.entities.RoomType;
import com.itschool.hotelResvMgt.services.ReservationService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

import static org.springframework.http.HttpStatus.CREATED;

@RestController
@RequestMapping("/api/reservations")
public class ReservationController {

    private final ReservationService reservationService;

    public ReservationController(ReservationService reservationService) {
        this.reservationService = reservationService;
    }

    @PostMapping
    public ResponseEntity<ResponseReservationDTO> createReservation(@RequestBody RequestReservationDTO requestReservationDTO) {
        return ResponseEntity.status(CREATED)
                .body(reservationService.createReservationFromDTO(requestReservationDTO));
    }

    @GetMapping
    public ResponseEntity<List<ResponseReservationDTO>> getReservations(
            @RequestParam(value = "checkInDate", required = false) LocalDate checkInDate,
            @RequestParam(value = "checkOutDate", required = false) LocalDate checkOutDate,
            @RequestParam(value = "roomType", required = false) RoomType roomType) {
        return ResponseEntity.ok(reservationService.getReservations(checkInDate, checkOutDate, roomType));
    }

    @PatchMapping("/{reservationId}")
    public ResponseEntity<ResponseReservationDTO> updateReservationCheckInDate(
            @PathVariable Long reservationId, @RequestBody RequestReservationDTO updateRequest) {
        ResponseReservationDTO response = reservationService.updateReservationCheckInDate(reservationId, updateRequest);

        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{reservationId}")
    public ResponseEntity<Void> deleteReservation(@PathVariable Long reservationId) {
        reservationService.deleteReservationById(reservationId);

        return ResponseEntity.noContent().build();
    }
}