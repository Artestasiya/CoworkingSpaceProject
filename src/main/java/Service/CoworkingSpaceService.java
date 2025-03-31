package Service;

import Data.CoworkingSpace;
import Data.DatabaseManager;
import Data.Reservation;
import Exceptions.CoworkingSpaceException;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public class CoworkingSpaceService {
    private final DatabaseManager dbManager;

    public CoworkingSpaceService(DatabaseManager dbManager) {
        this.dbManager = dbManager;
    }

    public void addSpace(int typeId, double price, boolean isAvailable) throws CoworkingSpaceException {
        try {
            if (dbManager.getTypeNameById(typeId) == null) {
                throw new CoworkingSpaceException("Invalid space type ID");
            }

            CoworkingSpace space = new CoworkingSpace(0, typeId, price, isAvailable);
            dbManager.addCoworkingSpace(space);
        } catch (SQLException e) {
            throw new CoworkingSpaceException("Error adding space", e);
        }
    }

    public void removeSpace(int id) throws CoworkingSpaceException {
        try {
            List<Reservation> reservations = dbManager.getReservationsForSpace(id);
            if (!reservations.isEmpty()) {
                throw new CoworkingSpaceException("Cannot delete space with active reservations");
            }
            dbManager.deleteCoworkingSpace(id);
        } catch (SQLException e) {
            throw new CoworkingSpaceException("Error deleting space", e);
        }
    }

    public void toggleAvailability(int spaceId, boolean isAvailable) throws CoworkingSpaceException {
        try {
            dbManager.updateCoworkingSpaceAvailability(spaceId, isAvailable);
        } catch (SQLException e) {
            throw new CoworkingSpaceException("Error updating availability", e);
        }
    }

    public List<CoworkingSpace> getAllSpaces() throws CoworkingSpaceException {
        try {
            return dbManager.getAllCoworkingSpaces();
        } catch (SQLException e) {
            throw new CoworkingSpaceException("Error getting spaces", e);
        }
    }

    public List<CoworkingSpace> getAvailableSpaces() throws CoworkingSpaceException {
        try {
            return dbManager.getAllCoworkingSpaces().stream()
                    .filter(CoworkingSpace::isAvailable)
                    .toList();
        } catch (SQLException e) {
            throw new CoworkingSpaceException("Error getting available spaces", e);
        }
    }

    public Optional<CoworkingSpace> getSpaceById(int id) throws CoworkingSpaceException {
        try {
            return dbManager.getCoworkingSpaceById(id);
        } catch (SQLException e) {
            throw new CoworkingSpaceException("Error getting space by ID", e);
        }
    }
}