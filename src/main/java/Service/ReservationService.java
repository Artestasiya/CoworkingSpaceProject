package Service;

import Data.CoworkingSpace;
import Data.DatabaseManager;
import Data.Reservation;
import Exceptions.ReservationException;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public class ReservationService {
    private final DatabaseManager dbManager;
    private final CoworkingSpaceService spaceService;

    public ReservationService(DatabaseManager dbManager, CoworkingSpaceService spaceService) {
        this.dbManager = dbManager;
        this.spaceService = spaceService;
    }

    public void addReservation(String userName, String date, String startTime, String endTime, int spaceId)
            throws ReservationException {
        try {
            dbManager.beginTransaction();

            Optional<CoworkingSpace> spaceOpt = spaceService.getSpaceById(spaceId);
            if (spaceOpt.isEmpty() || !spaceOpt.get().isAvailable()) {
                throw new ReservationException("The space is unavailable or does not exist");
            }

            if (!dbManager.isSpaceAvailable(spaceId, date, startTime, endTime)) {
                throw new ReservationException("The space is already occupied at the specified time");
            }

            Reservation reservation = new Reservation(0, userName, date, startTime, endTime, spaceOpt.get());
            dbManager.addReservation(reservation);
            dbManager.updateCoworkingSpaceAvailability(spaceId, false);

            dbManager.commitTransaction();
        } catch (Exception e) {
            dbManager.rollbackTransaction();
            throw new ReservationException("Couldn't create a reservation: " + e.getMessage(), e);
        }
    }

    public void cancelReservation(int reservationId) throws ReservationException {
        try {
            dbManager.beginTransaction();

            Optional<Reservation> reservationOpt = dbManager.getReservationById(reservationId);
            if (reservationOpt.isEmpty()) {
                throw new ReservationException("reservation with ID " + reservationId + " не найдено");
            }

            Reservation reservation = reservationOpt.get();
            dbManager.cancelReservation(reservationId);

            dbManager.updateCoworkingSpaceAvailability(reservation.getSpace().getId(), true);

            dbManager.commitTransaction();
        } catch (SQLException e) {
            dbManager.rollbackTransaction();
            throw new ReservationException("Couldn't cancel booking: " + e.getMessage(), e);
        }
    }

    public List<Reservation> getReservationsByUser(String userName) throws ReservationException {
        try {
            return dbManager.getReservationsByUser(userName);
        } catch (SQLException e) {
            throw new ReservationException("Error when receiving user's bookings", e);
        }
    }

    public void displayAllReservations() {
        try {
            List<Reservation> reservations = dbManager.getAllReservations();
            if (reservations.isEmpty()) {
                System.out.println("There are no active bookings");
                return;
            }

            System.out.println("\nReservation:");
            System.out.println("-----------------------");
            reservations.forEach(reservation -> {
                System.out.println("ID: " + reservation.getId());
                System.out.println("User: " + reservation.getUserName());
                System.out.println("Data: " + reservation.getDate());
                System.out.println("Time: " + reservation.getStartTime() + " - " + reservation.getEndTime());
                System.out.println("The ID space: " + reservation.getSpace().getId());
                try {
                    System.out.println("Type: " + reservation.getSpace().getType().getName());
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
                System.out.println("-----------------------");
            });
        } catch (SQLException e) {
            System.err.println("Error when receiving the booking list: " + e.getMessage());
        }
    }
}