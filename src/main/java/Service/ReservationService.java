package Service;

import Data.CoworkingSpace;
import Data.DatabaseManager;
import Data.Reservation;
import Exceptions.ReservationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
public class ReservationService {
    private final DatabaseManager dbManager;
    private final CoworkingSpaceService spaceService;

    public ReservationService(DatabaseManager dbManager, CoworkingSpaceService spaceService) {
        this.dbManager = dbManager;
        this.spaceService = spaceService;
    }

    @Transactional
    public void addReservation(String userName, String date, String startTime, String endTime, int spaceId)
            throws ReservationException {
        try {
            // Валидация параметров
            validateReservationParameters(userName, date, startTime, endTime);

            CoworkingSpace space = spaceService.getSpaceById(spaceId)
                    .orElseThrow(() -> new ReservationException("Space not found: " + spaceId));

            if (!space.isAvailable()) {
                throw new ReservationException("Space " + spaceId + " is not available");
            }

            if (!dbManager.isSpaceAvailable(spaceId, date, startTime, endTime)) {
                throw new ReservationException("Space is already booked for this time slot");
            }

            Reservation reservation = new Reservation(userName, date, startTime, endTime, space);
            dbManager.addReservation(reservation);
            dbManager.updateCoworkingSpaceAvailability(spaceId, false);

        } catch (ReservationException e) {
            throw e;
        } catch (Exception e) {
            throw new ReservationException("Failed to create reservation", e);
        }
    }

    @Transactional
    public void cancelReservation(int reservationId) throws ReservationException {
        try {
            Reservation reservation = dbManager.getReservationById(reservationId)
                    .orElseThrow(() -> new ReservationException("Reservation not found: " + reservationId));

            dbManager.cancelReservation(reservationId);
            dbManager.updateCoworkingSpaceAvailability(reservation.getSpace().getId(), true);

        } catch (ReservationException e) {
            throw e;
        } catch (Exception e) {
            throw new ReservationException("Failed to cancel reservation", e);
        }
    }

    public List<Reservation> getReservationsByUser(String userName) throws ReservationException {
        try {
            if (userName == null || userName.trim().isEmpty()) {
                throw new ReservationException("Invalid user name");
            }
            return dbManager.getReservationsByUser(userName);
        } catch (Exception e) {
            throw new ReservationException("Failed to get user reservations", e);
        }
    }

    public List<Reservation> getAllReservations() throws ReservationException {
        try {
            return dbManager.getAllReservations();
        } catch (Exception e) {
            throw new ReservationException("Failed to get all reservations", e);
        }
    }

    private void validateReservationParameters(String userName, String date,
                                               String startTime, String endTime)
            throws ReservationException {
        if (userName == null || userName.trim().isEmpty()) {
            throw new ReservationException("User name cannot be empty");
        }
        // Добавьте дополнительные проверки для date, startTime, endTime
    }
}