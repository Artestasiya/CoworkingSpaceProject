package com.example.coworking_web.service;

import com.example.coworking_web.model.CoworkingSpace;
import com.example.coworking_web.model.Reservation;
import com.example.coworking_web.repository.CoworkingSpaceRepository;
import com.example.coworking_web.repository.ReservationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;

@Service
public class ReservationService {
    private final ReservationRepository reservationRepository;
    private final CoworkingSpaceRepository coworkingSpaceRepository;

    @Autowired
    public ReservationService(ReservationRepository reservationRepository, CoworkingSpaceRepository coworkingSpaceRepository) {
        this.reservationRepository = reservationRepository;
        this.coworkingSpaceRepository = coworkingSpaceRepository;
    }

    @Transactional
    public boolean addReservation(String userName, String date, String startTime, String endTime, int spaceId) {
        CoworkingSpace space = coworkingSpaceRepository.findById(spaceId).orElse(null);
        if (space == null || !space.isAvailable()) {
            return false;
        }

        List<Reservation> conflictingReservations = reservationRepository.findBySpace(space);
        boolean isConflicting = conflictingReservations.stream().anyMatch(r ->
                r.getDate().equals(date) &&
                        r.getStartTime().equals(startTime) &&
                        r.getEndTime().equals(endTime)
        );

        if (isConflicting) {
            return false;
        }

        Reservation reservation = new Reservation(userName, date, startTime, endTime, space);
        reservationRepository.save(reservation);

        space.setAvailable(false);
        coworkingSpaceRepository.save(space);
        return true;
    }

    @Transactional
    public boolean cancelReservation(int reservationId) {
        Reservation reservation = reservationRepository.findById(reservationId).orElse(null);
        if (reservation == null) {
            return false;
        }

        reservationRepository.delete(reservation);
        CoworkingSpace space = reservation.getSpace();
        space.setAvailable(true);
        coworkingSpaceRepository.save(space);
        return true;
    }

    public List<Reservation> getReservationsByUser(String userName) {
        if (userName == null || userName.trim().isEmpty()) {
            return Collections.emptyList();
        }
        return reservationRepository.findByUserName(userName);
    }

    public List<Reservation> getAllReservations() {
        return reservationRepository.findAll();
    }
}
