package Service;

import Data.CoworkingSpace;
import Data.CoworkingType;
import Data.DatabaseManager;
import Data.Reservation;
import Exceptions.CoworkingSpaceException;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class CoworkingSpaceService {
    private final DatabaseManager dbManager;

    public CoworkingSpaceService(DatabaseManager dbManager) {
        this.dbManager = dbManager;
    }

    public void addSpace(int typeId, double price, boolean isAvailable) throws CoworkingSpaceException {
        try {
            // Получаем полный объект типа
            CoworkingType type = dbManager.getCoworkingTypeById(typeId)
                    .orElseThrow(() -> new CoworkingSpaceException("Invalid space type ID: " + typeId));

            CoworkingSpace space = new CoworkingSpace(type, price, isAvailable);
            dbManager.addCoworkingSpace(space);

        } catch (CoworkingSpaceException e) {
            throw e;
        } catch (Exception e) {
            throw new CoworkingSpaceException("Failed to add space", e);
        }
    }

    public void removeSpace(int id) throws CoworkingSpaceException {
        try {
            CoworkingSpace space = dbManager.getCoworkingSpaceById(id)
                    .orElseThrow(() -> new CoworkingSpaceException("Space not found: " + id));

            List<Reservation> reservations = dbManager.getReservationsForSpace(id);
            if (!reservations.isEmpty()) {
                throw new CoworkingSpaceException(
                        String.format("Cannot delete space %d - it has %d active reservations",
                                id, reservations.size()));
            }

            dbManager.deleteCoworkingSpace(id);

        } catch (CoworkingSpaceException e) {
            throw e;
        } catch (Exception e) {
            throw new CoworkingSpaceException("Failed to remove space: " + id, e);
        }
    }

    public void updateSpaceAvailability(int spaceId, boolean isAvailable) throws CoworkingSpaceException {
        try {
            if (!dbManager.getCoworkingSpaceById(spaceId).isPresent()) {
                throw new CoworkingSpaceException("Space not found: " + spaceId);
            }

            dbManager.updateCoworkingSpaceAvailability(spaceId, isAvailable);

        } catch (CoworkingSpaceException e) {
            throw e;
        } catch (Exception e) {
            throw new CoworkingSpaceException(
                    String.format("Failed to update availability for space %d to %s",
                            spaceId, isAvailable), e);
        }
    }

    public List<CoworkingSpace> getAllSpaces() throws CoworkingSpaceException {
        try {
            return dbManager.getAllCoworkingSpaces();
        } catch (Exception e) {
            throw new CoworkingSpaceException("Failed to retrieve spaces", e);
        }
    }

    public List<CoworkingSpace> getAvailableSpaces() throws CoworkingSpaceException {
        try {
            return dbManager.getAllCoworkingSpaces().stream()
                    .filter(CoworkingSpace::isAvailable)
                    .toList();
        } catch (Exception e) {
            throw new CoworkingSpaceException("Failed to retrieve available spaces", e);
        }
    }

    public Optional<CoworkingSpace> getSpaceById(int id) {
        return dbManager.getCoworkingSpaceById(id);
    }

    public Optional<CoworkingType> getTypeById(int typeId) {
        return dbManager.getCoworkingTypeById(typeId);
    }
}