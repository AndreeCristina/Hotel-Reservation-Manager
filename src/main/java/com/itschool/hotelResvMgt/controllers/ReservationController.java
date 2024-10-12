package com.itschool.hotelResvMgt.controllers;

import com.itschool.hotelResvMgt.models.dtos.ReservationDTORequest;
import com.itschool.hotelResvMgt.models.dtos.ReservationDTOResponse;
import com.itschool.hotelResvMgt.services.ReservationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/reservations")
public class ReservationController {

    private ReservationService reservationService;

    public ReservationController(ReservationService reservationService) {
        this.reservationService = reservationService;
    }

    @PostMapping
    public ResponseEntity<ReservationDTOResponse> createReservation(@RequestBody ReservationDTORequest reservationDTORequest) {
        ReservationDTOResponse createdReservation = reservationService.createReservation(reservationDTORequest);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(createdReservation);
    }

    @DeleteMapping("/{reservationId}")
    public ResponseEntity<Void> deleteReservation(@PathVariable Long reservationId) {
        reservationService.deleteReservationById(reservationId);

        return ResponseEntity.noContent().build();
    }
}