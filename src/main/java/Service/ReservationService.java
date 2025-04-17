package Service;

import Data.CoworkingSpace;
import Data.DatabaseManager;
import Data.Reservation;
import Exceptions.ReservationException;
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
                throw new ReservationException("Reservation with ID " + reservationId + " not found");
            }

            Reservation reservation = reservationOpt.get();
            dbManager.cancelReservation(reservationId);
            dbManager.updateCoworkingSpaceAvailability(reservation.getSpace().getId(), true);

            dbManager.commitTransaction();
        } catch (Exception e) {
            dbManager.rollbackTransaction();
            throw new ReservationException("Couldn't cancel booking: " + e.getMessage(), e);
        }
    }

    public List<Reservation> getReservationsByUser(String userName) throws ReservationException {
        try {
            return dbManager.getReservationsByUser(userName);
        } catch (Exception e) {
            throw new ReservationException("Error when receiving user's bookings", e);
        }
    }

    public List<Reservation> getAllReservations() throws ReservationException {
        try {
            return dbManager.getAllReservations();
        } catch (Exception e) {
            throw new ReservationException("Error getting all reservations", e);
        }
    }

    public void displayAllReservations() throws ReservationException {
        List<Reservation> reservations = getAllReservations();
        if (reservations.isEmpty()) {
            System.out.println("There are no active bookings");
            return;
        }

        System.out.println("\nReservations:");
        System.out.println("ID\tUser\t\tDate\t\tTime\t\tSpace ID\tType");
        System.out.println("--------------------------------------------------------------");

        for (Reservation reservation : reservations) {
            System.out.printf("%d\t%-10s\t%s\t%s-%s\t%d\t\t%s\n",
                    reservation.getId(),
                    reservation.getUserName(),
                    reservation.getDate(),
                    reservation.getStartTime(),
                    reservation.getEndTime(),
                    reservation.getSpace().getId(),
                    reservation.getSpace().getType().getName());
        }
    }
}