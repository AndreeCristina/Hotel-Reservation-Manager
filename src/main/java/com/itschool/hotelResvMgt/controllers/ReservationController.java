package com.itschool.hotelResvMgt.controllers;

import com.itschool.hotelResvMgt.models.dtos.ReservationDTO;
import com.itschool.hotelResvMgt.models.entities.Reservation;
import com.itschool.hotelResvMgt.services.ReservationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;

@RestController
@RequestMapping("/api/reservations")
public class ReservationController {

    @Autowired
    private ReservationService reservationService;

    @PostMapping
    public ResponseEntity<Reservation> createReservation(@RequestBody ReservationDTO reservationDTO) {
        Reservation createdReservation = reservationService.createReservation(reservationDTO);

        return ResponseEntity.created(URI.create("/reservations/" + createdReservation.getId()))
                .body(createdReservation);
    }
}