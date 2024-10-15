package com.itschool.hotelResvMgt.controllers;

import com.itschool.hotelResvMgt.models.dtos.ReservationDTORequest;
import com.itschool.hotelResvMgt.models.dtos.ReservationDTOResponse;
import com.itschool.hotelResvMgt.models.entities.RoomType;
import com.itschool.hotelResvMgt.services.ReservationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/reservations")
public class ReservationController {

    private final ReservationService reservationService;

    public ReservationController(ReservationService reservationService) {
        this.reservationService = reservationService;
    }

    @PostMapping
    public ResponseEntity<ReservationDTOResponse> createReservation(@RequestBody ReservationDTORequest reservationDTORequest) {
        ReservationDTOResponse createdReservation = reservationService.createReservationFromDTO(reservationDTORequest);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(createdReservation);
    }

    @DeleteMapping("/{reservationId}")
    public ResponseEntity<Void> deleteReservation(@PathVariable Long reservationId) {
        reservationService.deleteReservationById(reservationId);

        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{reservationId}")
    public ResponseEntity<ReservationDTOResponse> updateReservationCheckInDate(
            @PathVariable Long reservationId, @RequestBody ReservationDTORequest updateRequest) {
        ReservationDTOResponse response = reservationService.updateReservationCheckInDate(reservationId, updateRequest);

        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<List<ReservationDTOResponse>> getReservations(
            @RequestParam(value = "checkInDate", required = false) LocalDate checkInDate,
            @RequestParam(value = "checkOutDate", required = false) LocalDate checkOutDate,
            @RequestParam(value = "roomType", required = false) RoomType roomType) {
        return ResponseEntity.ok(reservationService.getReservations(checkInDate, checkOutDate, roomType));
    }
}