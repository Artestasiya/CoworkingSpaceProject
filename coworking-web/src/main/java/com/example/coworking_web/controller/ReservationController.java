package com.example.coworking_web.controller;

import com.example.coworking_web.model.Reservation;
import com.example.coworking_web.service.ReservationService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/reservations")
public class ReservationController {
    private final ReservationService reservationService;

    public ReservationController(ReservationService reservationService) {
        this.reservationService = reservationService;
    }

    @PostMapping
    public ResponseEntity<?> addReservation(@RequestBody ReservationDTO dto) {
        try {
            reservationService.addReservation(dto.getUserName(), dto.getDate(), dto.getStartTime(), dto.getEndTime(), dto.getSpaceId());
            return ResponseEntity.ok("Reservation added successfully");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error: " + e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> cancelReservation(@PathVariable int id) {
        try {
            reservationService.cancelReservation(id);
            return ResponseEntity.ok("Reservation cancelled");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error: " + e.getMessage());
        }
    }

    @GetMapping
    public List<Reservation> getAllReservations() {
        return reservationService.getAllReservations();
    }

    @GetMapping("/user/{username}")
    public List<Reservation> getUserReservations(@PathVariable String username) {
        return reservationService.getReservationsByUser(username);
    }
}
