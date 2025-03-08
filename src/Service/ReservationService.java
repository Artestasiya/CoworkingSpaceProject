package Service;

import Data.CoworkingSpace;
import Data.Reservation;

import java.io.*;
import java.util.*;
import java.util.logging.Logger;
import java.util.stream.Collectors;

public class ReservationService {
    private Map<Integer, Reservation> reservationMap;
    private CoworkingSpaceService spaceService;
    private static final Logger logger = Logger.getLogger(ReservationService.class.getName());

    public ReservationService(CoworkingSpaceService spaceService) {
        this.reservationMap = new HashMap<>();
        this.spaceService = spaceService;
        loadReservationsFromFile();
    }

    public void addReservation(String userName, String date, String startTime, String endTime, int spaceId) {
        Optional<CoworkingSpace> spaceOpt = spaceService.getSpaceById(spaceId);
        if (spaceOpt.isEmpty() || !spaceOpt.get().isAvailable()) {
            System.out.println("Space not available or does not exist.");
            return;
        }

        int id = reservationMap.size() + 1;
        Reservation reservation = new Reservation(id, userName, date, startTime, endTime, spaceOpt.get());
        reservationMap.put(id, reservation);
        spaceOpt.get().setAvailable(false);
        saveReservationsToFile();
        logger.info("Added new reservation: " + reservation);
    }

    public void cancelReservation(int reservationId) {
        Reservation reservation = reservationMap.remove(reservationId);
        if (reservation != null) {
            reservation.getSpace().setAvailable(true);
            saveReservationsToFile();
            logger.info("Canceled reservation with ID: " + reservationId);
        } else {
            System.out.println("Reservation not found.");
        }
    }

    public List<Reservation> getReservationsByUser(String userName) {
        return reservationMap.values().stream()
                .filter(reservation -> reservation.getUserName().equals(userName))
                .collect(Collectors.toList());
    }

    public void displayAllReservations() {
        if (reservationMap.isEmpty()) {
            System.out.println("No reservations found.");
        } else {
            reservationMap.values().forEach(reservation -> {
                System.out.println("Reservation ID: " + reservation.getId());
                System.out.println("User: " + reservation.getUserName());
                System.out.println("Date: " + reservation.getDate());
                System.out.println("Time: " + reservation.getStartTime() + " - " + reservation.getEndTime());
                System.out.println("Space ID: " + reservation.getSpace().getId());
                System.out.println("------------------------");
            });
        }
    }

    private void saveReservationsToFile() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream("reservations.dat"))) {
            oos.writeObject(new ArrayList<>(reservationMap.values()));
        } catch (IOException e) {
            logger.severe("Error saving reservations to file: " + e.getMessage());
        }
    }

    @SuppressWarnings("unchecked")
    private void loadReservationsFromFile() {
        File file = new File("reservations.dat");
        if (!file.exists()) {
            return;
        }

        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream("reservations.dat"))) {
            List<Reservation> loadedReservations = (List<Reservation>) ois.readObject();
            loadedReservations.forEach(reservation -> reservationMap.put(reservation.getId(), reservation));
        } catch (IOException | ClassNotFoundException e) {
            logger.severe("Error loading reservations from file: " + e.getMessage());
        }
    }
}