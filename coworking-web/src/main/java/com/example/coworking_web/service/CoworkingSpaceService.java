package com.example.coworking_web.service;

import com.example.coworking_web.model.CoworkingSpace;
import com.example.coworking_web.model.CoworkingType;
import com.example.coworking_web.model.Reservation;
import com.example.coworking_web.repository.CoworkingSpaceRepository;
import com.example.coworking_web.repository.CoworkingTypeRepository;
import com.example.coworking_web.repository.ReservationRepository;
import com.example.coworking_web.exceptions.CoworkingSpaceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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

    public void addSpace(int typeId, double price, boolean isAvailable) throws CoworkingSpaceException {
        CoworkingType type = coworkingTypeRepository.findById(typeId)
                .orElseThrow(() -> new CoworkingSpaceException("Invalid space type ID: " + typeId));

        CoworkingSpace space = new CoworkingSpace(type, price, isAvailable);
        coworkingSpaceRepository.save(space);
    }

    public void removeSpace(int id) throws CoworkingSpaceException {
        CoworkingSpace space = coworkingSpaceRepository.findById(id)
                .orElseThrow(() -> new CoworkingSpaceException("Space not found: " + id));

        List<Reservation> reservations = space.getReservations();
        if (!reservations.isEmpty()) {
            throw new CoworkingSpaceException(
                    String.format("Cannot delete space %d - it has %d active reservations", id, reservations.size()));
        }

        coworkingSpaceRepository.delete(space);
    }

    public void updateSpaceAvailability(int spaceId, boolean isAvailable) throws CoworkingSpaceException {
        CoworkingSpace space = coworkingSpaceRepository.findById(spaceId)
                .orElseThrow(() -> new CoworkingSpaceException("Space not found: " + spaceId));

        space.setAvailable(isAvailable);
        coworkingSpaceRepository.save(space);
    }

    public List<CoworkingSpace> getAllSpaces() throws CoworkingSpaceException {
        return coworkingSpaceRepository.findAll();
    }

    public List<CoworkingSpace> getAvailableSpaces() throws CoworkingSpaceException {
        return coworkingSpaceRepository.findByAvailableTrue();
    }

    public Optional<CoworkingSpace> getSpaceById(int id) {
        return coworkingSpaceRepository.findById(id);
    }

    public Optional<CoworkingType> getTypeById(int typeId) {
        return coworkingTypeRepository.findById(typeId);
    }
}
