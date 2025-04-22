package com.example.coworking_web.service;

import com.example.coworking_web.model.CoworkingSpace;
import com.example.coworking_web.model.Reservation;
import com.example.coworking_web.repository.CoworkingSpaceRepository;
import com.example.coworking_web.repository.ReservationRepository;
import com.example.coworking_web.exceptions.ReservationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
    public void addReservation(String userName, String date, String startTime, String endTime, int spaceId)
            throws ReservationException {
        CoworkingSpace space = coworkingSpaceRepository.findById(spaceId)
                .orElseThrow(() -> new ReservationException("Space not found: " + spaceId));

        if (!space.isAvailable()) {
            throw new ReservationException("Space " + spaceId + " is not available");
        }

        List<Reservation> conflictingReservations = reservationRepository.findBySpace(space);
        boolean isConflicting = conflictingReservations.stream().anyMatch(r -> r.getDate().equals(date) &&
                r.getStartTime().equals(startTime) && r.getEndTime().equals(endTime));

        if (isConflicting) {
            throw new ReservationException("Space is already booked for this time slot");
        }

        Reservation reservation = new Reservation(userName, date, startTime, endTime, space);
        reservationRepository.save(reservation);
        space.setAvailable(false);
        coworkingSpaceRepository.save(space);
    }

    @Transactional
    public void cancelReservation(int reservationId) throws ReservationException {
        Reservation reservation = reservationRepository.findById(reservationId)
                .orElseThrow(() -> new ReservationException("Reservation not found: " + reservationId));

        reservationRepository.delete(reservation);

        CoworkingSpace space = reservation.getSpace();
        space.setAvailable(true);
        coworkingSpaceRepository.save(space);
    }

    public List<Reservation> getReservationsByUser(String userName) throws ReservationException {
        if (userName == null || userName.trim().isEmpty()) {
            throw new ReservationException("Invalid user name");
        }
        return reservationRepository.findByUserName(userName);
    }

    public List<Reservation> getAllReservations() throws ReservationException {
        return reservationRepository.findAll();
    }
}
