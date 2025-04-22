package com.example.coworking_web.repository;

import com.example.coworking_web.model.CoworkingSpace;
import com.example.coworking_web.model.Reservation;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReservationRepository extends JpaRepository<Reservation, Integer> {
    List<Reservation> findByUserName(String userName);
    List<Reservation> findBySpace(CoworkingSpace space);
    boolean existsBySpaceAndDateAndStartTimeAndEndTime(
            CoworkingSpace space, String date, String startTime, String endTime);
}
