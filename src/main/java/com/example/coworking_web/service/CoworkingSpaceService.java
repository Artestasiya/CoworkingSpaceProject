package com.example.coworking_web.service;

import com.example.coworking_web.model.CoworkingSpace;
import com.example.coworking_web.model.CoworkingType;
import com.example.coworking_web.model.Reservation;
import com.example.coworking_web.repository.CoworkingSpaceRepository;
import com.example.coworking_web.repository.CoworkingTypeRepository;
import com.example.coworking_web.repository.ReservationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
public class CoworkingSpaceService {
    private final CoworkingSpaceRepository coworkingSpaceRepository;
    private final CoworkingTypeRepository coworkingTypeRepository;

    @Autowired
    public CoworkingSpaceService(CoworkingSpaceRepository coworkingSpaceRepository, CoworkingTypeRepository coworkingTypeRepository) {
        this.coworkingSpaceRepository = coworkingSpaceRepository;
        this.coworkingTypeRepository = coworkingTypeRepository;
    }

    public boolean addSpace(int typeId, double price, boolean isAvailable) {
        Optional<CoworkingType> typeOpt = coworkingTypeRepository.findById(typeId);
        if (typeOpt.isEmpty()) {
            return false;
        }

        CoworkingSpace space = new CoworkingSpace(typeOpt.get(), price, isAvailable);
        coworkingSpaceRepository.save(space);
        return true;
    }

    public boolean removeSpace(int id) {
        Optional<CoworkingSpace> spaceOpt = coworkingSpaceRepository.findById(id);
        if (spaceOpt.isEmpty()) {
            return false;
        }

        CoworkingSpace space = spaceOpt.get();
        List<Reservation> reservations = space.getReservations();
        if (reservations != null && !reservations.isEmpty()) {
            return false;
        }

        coworkingSpaceRepository.delete(space);
        return true;
    }

    public boolean updateSpaceAvailability(int spaceId, boolean isAvailable) {
        Optional<CoworkingSpace> spaceOpt = coworkingSpaceRepository.findById(spaceId);
        if (spaceOpt.isEmpty()) {
            return false;
        }

        CoworkingSpace space = spaceOpt.get();
        space.setAvailable(isAvailable);
        coworkingSpaceRepository.save(space);
        return true;
    }

    public List<CoworkingSpace> getAllSpaces() {
        return coworkingSpaceRepository.findAll();
    }

    public Optional<CoworkingSpace> getSpaceById(int id) {
        return coworkingSpaceRepository.findById(id);
    }

    public Optional<CoworkingType> getTypeById(int typeId) {
        return coworkingTypeRepository.findById(typeId);
    }
}
